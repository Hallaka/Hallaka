package com.pheonix.org.hallaka.Activity.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.CartHandler;
import com.pheonix.org.hallaka.Models.CartDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecycler, recRecycler;
    CartHandler adapter, adapter2;
    SpinKitView spinKitView;
    List<CartDataModel> list = new ArrayList<>(), delList = new ArrayList<>();
    TextView nothingHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecycler = findViewById(R.id.cartRecycler);
        recRecycler = findViewById(R.id.deliverRecycler);
        spinKitView = findViewById(R.id.spin_kit_cart);
        nothingHere = findViewById(R.id.nothingHereCart);

        adapter = new CartHandler(list, this);
        adapter2 = new CartHandler(delList, this);

        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        recRecycler.setLayoutManager(new LinearLayoutManager(this));
        recRecycler.setAdapter(adapter2);
        cartRecycler.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        showLoadingBar();
        FirebaseDatabase.getInstance().getReference("cart").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nothingHere.setVisibility(View.GONE);
                    list.clear();
                    delList.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        CartDataModel c = d.getValue(CartDataModel.class);
                        if (Objects.requireNonNull(c).getStatus().equalsIgnoreCase("cart")) {
                            list.add(c);
                        } else {
                            delList.add(c);
                        }
                    }
                    if(list.isEmpty()){
                        findViewById(R.id.nothingHereCart).setVisibility(View.VISIBLE);
                    }else {
                        findViewById(R.id.nothingHereCart).setVisibility(View.GONE);
                    }
                    if (delList.isEmpty()){
                        findViewById(R.id.nothingHereReceived).setVisibility(View.VISIBLE);
                    }else {
                        findViewById(R.id.nothingHereReceived).setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                } else {
                    findViewById(R.id.nothingHereCart).setVisibility(View.VISIBLE);
                    findViewById(R.id.nothingHereReceived).setVisibility(View.VISIBLE);
                }
                hideLoadingBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nothingHere.setVisibility(View.VISIBLE);
                hideLoadingBar();
            }
        });


    }

    private void hideLoadingBar() {
        spinKitView.setVisibility(View.GONE);
        Funcs.enableCurrentScreen(this);
    }

    private void showLoadingBar() {
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(this);
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.cartMainLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}