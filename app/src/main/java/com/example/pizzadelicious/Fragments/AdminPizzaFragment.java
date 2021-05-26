package com.example.pizzadelicious.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pizzadelicious.Adapters.AdminPizzaAdapter;
import com.example.pizzadelicious.Models.JSONResponseProduct;
import com.example.pizzadelicious.Models.Product;
import com.example.pizzadelicious.R;
import com.example.pizzadelicious.Retrofit.ApiInterface;
import com.example.pizzadelicious.Retrofit.Common;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminPizzaFragment extends Fragment {
    RecyclerView recyclerView_pizza;
    ArrayList<Product> pizzaList;
    ApiInterface service;

    public AdminPizzaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service = Common.getGsonService();
        setControl(view);
        loadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_pizza, container, false);
    }
    private void loadData() {
        //Pizza Recycler View
        if (Common.isConnectedToInternet(getActivity().getBaseContext())){
            service.getProductByType("1").enqueue(new Callback<JSONResponseProduct>() {
                @Override
                public void onResponse(Call<JSONResponseProduct> call, Response<JSONResponseProduct> response) {
                    JSONResponseProduct jsonResponsePizza = response.body();
                    pizzaList = new ArrayList<>(Arrays.asList(jsonResponsePizza.getData()));
                    AdminPizzaAdapter adminPizzaAdapter  = new AdminPizzaAdapter( pizzaList, AdminPizzaFragment.this);
                    recyclerView_pizza.setAdapter(adminPizzaAdapter);
                    adminPizzaAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<JSONResponseProduct> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(getActivity(), "Please check your internet!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void setControl(View view) {
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        if (getActivity() != null) {
//            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        }

        recyclerView_pizza = view.findViewById(R.id.recyclerView_pizza);
        recyclerView_pizza.setHasFixedSize(true);
        recyclerView_pizza.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}