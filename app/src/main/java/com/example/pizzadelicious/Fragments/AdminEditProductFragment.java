package com.example.pizzadelicious.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pizzadelicious.Models.JSONResponseAccounts;
import com.example.pizzadelicious.Models.JSONResponseProduct;
import com.example.pizzadelicious.Models.Product;
import com.example.pizzadelicious.Models.User;
import com.example.pizzadelicious.R;
import com.example.pizzadelicious.Retrofit.ApiInterface;
import com.example.pizzadelicious.Retrofit.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminEditProductFragment extends Fragment {

    ApiInterface service;
    EditText et_name, et_type, et_price;
    ImageView imageProduct;
    Button btn_save;
    ImageButton btn_back;
    String productId;
    String productTypeId = "";

    RadioButton radioButton_pizza, getRadioButton_cake;
    RadioGroup radioGroup;
    private ArrayList<Product> products;

    public AdminEditProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_edit_product, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        radioButton_pizza = view.findViewById(R.id.radioPizza);
        getRadioButton_cake = view.findViewById(R.id.radioCake);
        radioGroup = view.findViewById(R.id.radio_Gr);
        et_name = view.findViewById(R.id.et_name);
//        et_type = view.findViewById(R.id.et_type);
        et_price = view.findViewById(R.id.et_price);
        imageProduct = view.findViewById(R.id.imageProduct);

        btn_save = view.findViewById(R.id.btn_save);
        btn_back = view.findViewById(R.id.btn_back);

        service = Common.getGsonService();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productId = bundle.getString("productId");
            Log.d("id", "onResponse: " + productId);
            load(productId);
        }
        return view;
    }

    String checkRadio(String id){
        if(id.compareTo("1")==0){
            radioButton_pizza.setChecked(true);
            return id;
        }
        else if(id.compareTo("2")==0) {
            getRadioButton_cake.setChecked(true);
            return id;
        }
        return "";
    }

    private void load(String productId) {
        service.getProductById("" + productId).enqueue(new Callback<JSONResponseProduct>() {
            @Override
            public void onResponse(Call<JSONResponseProduct> call, Response<JSONResponseProduct> response) {
                JSONResponseProduct jsonResponseProduct = response.body();
                products = new ArrayList<>(Arrays.asList(jsonResponseProduct.getData()));
                Log.d("idP", "onResponse: " + products.get(0).getId());
                et_name.setText(products.get(0).getName());
                et_price.setText(products.get(0).getPrice());
//                et_type.setText(products.get(0).getType().getId());
                productTypeId = products.get(0).getType().getId();
                checkRadio(productTypeId);
                Picasso.get().load("" + products.get(0).getImage())
                        .into(imageProduct);

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String idProductTemp = "";
                        if(radioButton_pizza.isChecked()==true){
                            idProductTemp += "1";
                        }else idProductTemp += "2";
                        service.updateProduct(""+ productId, "" + et_name.getText().toString(), "" + et_price.getText().toString(),
                                idProductTemp).enqueue(new Callback<JSONResponseProduct>() {
                            @Override
                            public void onResponse(Call<JSONResponseProduct> call, Response<JSONResponseProduct> response) {
                                Toast.makeText(getActivity(), "Updated   successfully!", Toast.LENGTH_SHORT).show();
                                if(products.get(0).getType().getId().equals("1")){
                                    Fragment someFragment = new AdminPizzaFragment();
                                    FragmentTransaction transaction = AdminEditProductFragment.this.getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                    transaction.commit();
                                }
                                else{
                                    Fragment someFragment = new AdminCakeFragment();
                                    FragmentTransaction transaction = AdminEditProductFragment.this.getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                    transaction.commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponseProduct> call, Throwable t) {
                                Toast.makeText(getActivity(), "Update failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<JSONResponseProduct> call, Throwable t) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
                        if(productTypeId.equals("1")){
                            Fragment someFragment = new AdminPizzaFragment();
                            FragmentTransaction transaction = AdminEditProductFragment.this.getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();
                        }
                        else{
                            Fragment someFragment = new AdminCakeFragment();
                            FragmentTransaction transaction = AdminEditProductFragment.this.getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}