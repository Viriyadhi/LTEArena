package com.example.ltearena.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.R;
import com.example.ltearena.models.PhoneModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhoneDetailActivity extends AppCompatActivity {
    private String image, phone, brand, releaseDate, storage, operatingSystem, dimension;
    private TextView tv_phone, tv_brand, tv_releaseDate, tv_storage, tv_operatingSystem, tv_dimension;
    private ImageView img_phone;
    private ImageButton btn_back;
    private String url;
    private ProgressDialog progressDialog;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        img_phone = findViewById(R.id.img_phone_detail);
        tv_phone = findViewById(R.id.tv_phone_detail);
        tv_brand = findViewById(R.id.tv_brand_detail);
        tv_dimension = findViewById(R.id.tv_dimension_detail);
        tv_storage = findViewById(R.id.tv_storage_detail);
        tv_operatingSystem = findViewById(R.id.tv_operatingSystem_detail);
        tv_releaseDate = findViewById(R.id.tv_releaseDate_detail);
        btn_back = findViewById(R.id.btn_back_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            System.out.println("url: " + url);
            url = url.replace("http", "https");
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
    }

    private void getData() {
        progressDialog.show();

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mRequest = new JsonObjectRequest
                (url, response -> {
                    try {
                        System.out.println("Response " + response.toString());
                        JSONObject jsonObject = response.getJSONObject("data");
                        phone = jsonObject.getString("phone_name");
                        brand = jsonObject.getString("brand");
                        image = jsonObject.getString("thumbnail");
                        releaseDate = jsonObject.getString("release_date");
                        dimension = jsonObject.getString("dimension");
                        operatingSystem = jsonObject.getString("os");
                        storage = jsonObject.getString("storage");

                        setContent();

                        progressDialog.dismiss();
                    } catch (Exception error) {
                        System.out.println("Error: " + error.getMessage());
                        progressDialog.dismiss();
                    }
                }, error -> {
                    System.out.println("Error: " + error.getMessage());
                    progressDialog.dismiss();
                });


        mRequestQueue.add(mRequest);
    }

    private void setContent() {
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.ic_baseline_phone_android_24)
                .error(R.drawable.ic_baseline_phone_android_24)
                .into(img_phone);
        tv_phone.setText(phone);
        tv_brand.setText(brand);
        tv_releaseDate.setText(releaseDate);
        tv_storage.setText(storage);
        tv_operatingSystem.setText(operatingSystem);
        tv_dimension.setText(dimension);
    }
}
