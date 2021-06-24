package com.example.pizzadelicious.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.pizzadelicious.Models.JSONResponseProduct;
import com.example.pizzadelicious.R;
import com.example.pizzadelicious.Retrofit.ApiInterface;
import com.example.pizzadelicious.Retrofit.Common;
import com.example.pizzadelicious.Utils.ReadPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AdminInsertProductFragment extends Fragment {
    EditText et_name, et_price, et_type;
    Button btn_chooseImage, btn_insert;
    ImageButton btn_back;
    ImageView imageView;
    RadioButton radioButton_pizza, getRadioButton_cake;
    RadioGroup radioGroup;

    String IMAGE_PATH = "";
    Uri saveUri;
    ApiInterface service;
    private final int PICK_IMAGE_REQUEST = 1;

    public AdminInsertProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_insert_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        setEvent();
        service = Common.getGsonService();
    }

    private void setControl(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        et_name = view.findViewById(R.id.et_name);
        et_price = view.findViewById(R.id.et_price);
//        et_type = view.findViewById(R.id.et_type);
        btn_chooseImage = view.findViewById(R.id.btn_chooseImage);
        btn_insert = view.findViewById(R.id.btn_insert);
        btn_back = view.findViewById(R.id.btn_back);
        imageView = view.findViewById(R.id.imageView);

        radioButton_pizza = view.findViewById(R.id.radioPizza);
        getRadioButton_cake = view.findViewById(R.id.radioCake);
        radioGroup = view.findViewById(R.id.radio_Gr);
    }

    private void setEvent() {

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
                        Fragment someFragment = new DashboardFragment();
                        FragmentTransaction transaction = AdminInsertProductFragment.this.getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
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

        btn_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!et_name.getText().toString().equals("") && !et_price.getText().toString().equals("")
//                        && !et_type.getText().toString().equals("") && !IMAGE_PATH.equals("")) {
                if ( !IMAGE_PATH.equals("")) {

//                    if (et_type.getText().toString().equals("pizza") || et_type.getText().toString().equals("Pizza")) {
                    if (radioButton_pizza.isChecked()==true) {

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();

                        File file = new File(IMAGE_PATH);
                        // create RequestBody instance from file
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);


                        service.postProduct("" + et_name.getText(), "" + et_price.getText(),
                                "1", body).enqueue(new Callback<JSONResponseProduct>() {
                            @Override
                            public void onResponse(Call<JSONResponseProduct> call, Response<JSONResponseProduct> response) {

                                progressDialog.dismiss();
                                Log.d("uri1", "---" + body.toString());

                                Toast.makeText(getActivity(), "Insert Product successfully!", Toast.LENGTH_SHORT).show();
                                Fragment someFragment = new AdminPizzaFragment();
                                FragmentTransaction transaction = AdminInsertProductFragment.this.getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }

                            @Override
                            public void onFailure(Call<JSONResponseProduct> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Insert Product failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (getRadioButton_cake.isChecked()==true) {

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();

                        File file = new File(IMAGE_PATH);
                        // create RequestBody instance from file
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part image =
                                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                        service.postProduct("" + et_name.getText().toString(), "" + et_price.getText().toString(),
                                "2", image).enqueue(new Callback<JSONResponseProduct>() {
                            @Override
                            public void onResponse(Call<JSONResponseProduct> call, Response<JSONResponseProduct> response) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Insert Product successfully!", Toast.LENGTH_SHORT).show();
                                Fragment someFragment = new AdminCakeFragment();
                                FragmentTransaction transaction = AdminInsertProductFragment.this.getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout, someFragment); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }

                            @Override
                            public void onFailure(Call<JSONResponseProduct> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Insert Product failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(getContext(), "Please input type again!", Toast.LENGTH_SHORT).show();
                    }

                } else if (IMAGE_PATH.equals("")) {
                    Toast.makeText(getContext(), "Please choose image!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Insert Product failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            saveUri = data.getData();
            Log.d("uri2", "" + saveUri);
            IMAGE_PATH = ReadPathUtil.getPath(getContext(), saveUri);

            Picasso.get().load(saveUri)
                    .into(imageView);

        }
    }

}