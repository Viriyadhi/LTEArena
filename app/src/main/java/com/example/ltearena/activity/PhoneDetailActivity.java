package com.example.ltearena.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.R;
import com.example.ltearena.models.PhoneModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class PhoneDetailActivity extends AppCompatActivity {
    private String image, phone, brand, releaseDate, storage, operatingSystem, dimension;
    private TextView tv_phone, tv_brand, tv_releaseDate, tv_storage, tv_operatingSystem, tv_dimension;
    private ImageView img_phone;
    private ImageButton btn_back, btn_favorite;
    private String url, slug = "";
    private boolean isFavorite;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private PhoneModel phoneModel;

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
        btn_favorite = findViewById(R.id.btn_favorite_detail);
        scrollView = findViewById(R.id.scroll_detail);
        progressBar = findViewById(R.id.progress_circular_detail);

        scrollView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            image = bundle.getString("image");
            slug = bundle.getString("slug");
            url = bundle.getString("url");
            url = url.replace("http", "https");
        }

        System.out.println("slug "+slug);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) {
                    isFavorite = false;
                    btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    isFavorite = true;
                    btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                toggleFavorite(isFavorite, phoneModel);
            }
        });

        getData();
    }

    private void toggleFavorite(boolean isFavorite, PhoneModel phoneModel) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites").child(currentUser.getUid()).child(phoneModel.getSlug());

        if (isFavorite) {
            favoritesRef.setValue(phoneModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        } else {
            favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dataSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void getData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites").child(currentUser.getUid());
        favoritesRef.child(slug).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    isFavorite = true;
                }

                System.out.println("is favorite " + isFavorite);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                        releaseDate = jsonObject.getString("release_date");
                        dimension = jsonObject.getString("dimension");
                        operatingSystem = jsonObject.getString("os");
                        storage = jsonObject.getString("storage");
                        if (jsonObject.has("thumbnail")) {
                            image = jsonObject.getString("thumbnail");
                        }

                        phoneModel = new PhoneModel(phone, image, url, slug);

                        setContent();

                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    } catch (Exception error) {
                        System.out.println("Error: " + error.getMessage());
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                }, error -> {
                    System.out.println("Error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isFavorite", isFavorite);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
