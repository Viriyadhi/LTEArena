package com.example.ltearena.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.R;
import com.example.ltearena.adapters.BrandAdapter;
import com.example.ltearena.models.BrandModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = HomeScreen.class.getName();
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    private final String url = "https://api-mobilespecs.azharimm.site/v2/brands";
    private ProgressDialog progressDialog;
    private TextInputEditText txt_search;
    private BrandAdapter adapter;
    private RecyclerView recyclerView;
    private List<BrandModel> masterArrayList, arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_3:
                        Intent intent = new Intent(getApplicationContext(), ProfileSettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                }
                return true;
            }
        });

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
                for(BrandModel brandModel : masterArrayList){
                    if(brandModel.getBrandName() != null && brandModel.getBrandName().toLowerCase().contains(s)) {
                        arrayList.add(brandModel);
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
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            BrandModel brandModel = new BrandModel(object.getInt("brand_id"), object.getString("brand_name"), object.getString("detail"));
                            masterArrayList.add(brandModel);
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

    private void setAdapter(List<BrandModel> arrayList) {
        adapter = new BrandAdapter(this, arrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), PhonesActivity.class);
                intent.putExtra("brand", arrayList.get(position).getBrandName());
                intent.putExtra("url", arrayList.get(position).getDetail());
                startActivity(intent);
            }
        });
    }
}