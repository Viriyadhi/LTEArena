package com.example.ltearena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.adapters.BrandAdapter;
import com.example.ltearena.models.BrandModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = HomeScreen.class.getName();
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    private String url = "https://api-mobilespecs.azharimm.site/v2/brands";
    private ProgressDialog progressDialog;
    private BrandAdapter adapter;
    private RecyclerView recyclerView;
    private List<BrandModel> arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        recyclerView = findViewById(R.id.home_recycler);
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
        arrayList = new ArrayList<>();

        //String Request initialized
        mRequest = new JsonObjectRequest
                (url, response -> {
                    try {
                        System.out.println("Response " + response.toString());
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            BrandModel brandModel = new BrandModel(object.getInt("brand_id"), object.getString("brand_name"), object.getString("detail"));
                            arrayList.add(brandModel);
                        }
                        adapter = new BrandAdapter(this, arrayList);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

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
}