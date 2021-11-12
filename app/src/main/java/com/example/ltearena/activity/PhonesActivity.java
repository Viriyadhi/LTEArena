package com.example.ltearena.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.R;
import com.example.ltearena.adapters.PhoneAdapter;
import com.example.ltearena.models.BrandModel;
import com.example.ltearena.models.PhoneModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhonesActivity extends AppCompatActivity {
    private TextView tv_brand;
    private TextInputEditText txt_search;
    private RecyclerView recyclerView;
    private String brandName, url;
    private ProgressDialog progressDialog;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    private PhoneAdapter adapter;
    private List<PhoneModel> masterArrayList, arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phones);

        tv_brand = findViewById(R.id.tv_brand_phones);
        recyclerView = findViewById(R.id.phones_recycler);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            brandName = bundle.getString("brand");
            url = bundle.getString("url");
            System.out.println("url: " + url);

            tv_brand.setText(brandName + " Phones");
        }

        recyclerView = findViewById(R.id.home_recycler);
        txt_search = findViewById(R.id.txt_search_brand);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                arrayList = new ArrayList();
                arrayList = masterArrayList;
                setAdapter(arrayList);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList = new ArrayList();
                for(PhoneModel phoneModel : masterArrayList){
                    if(phoneModel.getPhoneName() != null && phoneModel.getPhoneName().toLowerCase().contains(s)) {
                        arrayList.add(phoneModel);
                    }
                }

                setAdapter(arrayList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        runnable.run();
    }

    private void getData() {
        progressDialog.show();
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        masterArrayList = new ArrayList<>();


        //String Request initialized
        mRequest = new JsonObjectRequest
                (url, response -> {
                    try {
                        System.out.println("Response " + response.toString());
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonPhoneArray = jsonObject.getJSONArray("phones");
                        for (int i = 0; i < jsonPhoneArray.length(); i++) {
                            JSONObject object = jsonPhoneArray.getJSONObject(i);
                            PhoneModel phoneModel = new PhoneModel(object.getString("phone_name"), object.getString("image"), object.getString("detail"));
                            masterArrayList.add(phoneModel);
                        }

                        arrayList = masterArrayList;
                        setAdapter(arrayList);

                        progressDialog.dismiss();
                    } catch (Exception error) {
                        System.out.println("Error: " + error.toString());
                    }
                }, error -> {
                    System.out.println("Error: " + error.toString());
                    progressDialog.dismiss();
                });


        mRequestQueue.add(mRequest);
    }

    private void setAdapter(List<PhoneModel> arrayList) {
        adapter = new PhoneAdapter(this, arrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}