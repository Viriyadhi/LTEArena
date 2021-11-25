package com.example.ltearena.fragments;

import static android.app.Activity.RESULT_OK;

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

import com.example.ltearena.R;
import com.example.ltearena.activity.PhoneDetailActivity;
import com.example.ltearena.adapters.PhoneAdapter;
import com.example.ltearena.models.PhoneModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int REQUEST_CODE = 101;

    private String slug;
    private TextInputEditText txt_search;
    private RecyclerView recyclerView;
    private PhoneAdapter adapter;
    private List<PhoneModel> masterArrayList, arrayList;
    private PhoneModel phoneModel;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.favorites_recycler);
        txt_search = view.findViewById(R.id.txt_search_favorites);
        scrollView = view.findViewById(R.id.scroll_favorites);
        progressBar = view.findViewById(R.id.progress_circular_favorites);

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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        runnable.run();
    }

    private void getData() {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.getUid());

            favoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    masterArrayList = new ArrayList<>();
                    arrayList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PhoneModel phoneModel = dataSnapshot.getValue(PhoneModel.class);
                        masterArrayList.add(phoneModel);
                    }

                    arrayList = masterArrayList;
                    setAdapter(arrayList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(List<PhoneModel> arrayList) {
        adapter = new PhoneAdapter(getActivity(), arrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PhoneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                phoneModel = arrayList.get(position);
                Intent intent = new Intent(getActivity(), PhoneDetailActivity.class);
                intent.putExtra("url", phoneModel.getDetailUrl());
                intent.putExtra("slug", phoneModel.getSlug());
                startActivityForResult(intent, REQUEST_CODE);

                slug = arrayList.get(position).getSlug();
            }
        });

        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }
}