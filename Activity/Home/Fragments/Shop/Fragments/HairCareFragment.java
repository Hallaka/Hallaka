package com.pheonix.org.hallaka.Activity.Home.Fragments.Shop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.ProductsHandler;
import com.pheonix.org.hallaka.Models.ProductDataModel;
import com.pheonix.org.hallaka.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HairCareFragment extends Fragment {
    RecyclerView recycler;
    ProductsHandler handler;
    View v;
    List<ProductDataModel> list = new ArrayList<>();

    public HairCareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_hair_care, container, false);
        recycler = v.findViewById(R.id.hairCareRecycler);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(manager);

        handler = new ProductsHandler(list,getActivity(),true);
        recycler.setAdapter(handler);

        return v;
    }

    public void onResume() {
        super.onResume();

        FirebaseDatabase.getInstance().getReference("products").orderByChild("tag").equalTo("Hair Care").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        ProductDataModel model = d.getValue(ProductDataModel.class);
                        list.add(model);

                        handler.notifyDataSetChanged();
                    }
                    somethinghere();
                }
                else nothinghere();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
nothinghere();
            }
        });

    }
    private void nothinghere() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.nothingHereHair).setVisibility(View.VISIBLE);
    }
    private void somethinghere() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.nothingHereHair).setVisibility(View.GONE);
    }
}