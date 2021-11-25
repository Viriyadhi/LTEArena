package com.example.ltearena.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ltearena.R;
import com.example.ltearena.activity.PhonesActivity;
import com.example.ltearena.adapters.BrandAdapter;
import com.example.ltearena.models.BrandModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RequestQueue mRequestQueue;
    private JsonObjectRequest mRequest;
    private final String url = "https://api-mobilespecs.azharimm.site/v2/brands";
    private TextInputEditText txt_search;
    private BrandAdapter adapter;
    private RecyclerView recyclerView;
    private List<BrandModel> masterArrayList, arrayList;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.home_recycler);
        txt_search = view.findViewById(R.id.txt_search_brand);
        scrollView = view.findViewById(R.id.scroll_brand);
        progressBar = view.findViewById(R.id.progress_circular_brand);

        scrollView.setVisibility(View.GONE);

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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        runnable.run();
    }

    private void getData() {
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getActivity());
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
                    } catch (Exception error) {
                        System.out.println("Error: " + error.toString());
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                }, error -> {
                    System.out.println("Error: " + error.toString());
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                });


        mRequestQueue.add(mRequest);
    }

    private void setAdapter(List<BrandModel> arrayList) {
        adapter = new BrandAdapter(getActivity(), arrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), PhonesActivity.class);
                intent.putExtra("brand", arrayList.get(position).getBrandName());
                intent.putExtra("url", arrayList.get(position).getDetail());
                startActivity(intent);
            }
        });

        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }
}