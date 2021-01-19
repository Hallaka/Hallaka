package com.pheonix.org.hallaka.Activity.BookedProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.BookedProductsHandler;
import com.pheonix.org.hallaka.Models.CartDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.ArrayList;
import java.util.List;

public class BookedProductsActivity extends AppCompatActivity {

    RecyclerView bookedRecycler;
    BookedProductsHandler adapter;
    List<CartDataModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_products);

        bookedRecycler = findViewById(R.id.productIdbProducts);

        bookedRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookedProductsHandler(list, getActivity());

        bookedRecycler.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLoadingBar();
        FirebaseDatabase.getInstance().getReference("cart").orderByChild("oid").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    findViewById(R.id.nothinghereproduct).setVisibility(View.GONE);
                    for (DataSnapshot d : snapshot.getChildren()) {
                        CartDataModel c = d.getValue(CartDataModel.class);
                        list.add(c);
                    }
                } else {
                    findViewById(R.id.nothinghereproduct).setVisibility(View.VISIBLE);
                }
                disableLoadingBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                disableLoadingBar();
                findViewById(R.id.nothinghereproduct).setVisibility(View.VISIBLE);
            }
        });


    }

    private void disableLoadingBar() {
        findViewById(R.id.spin_kit_bproduct).setVisibility(View.GONE);
        Funcs.enableCurrentScreen(getActivity());
    }

    private void enableLoadingBar() {
        findViewById(R.id.spin_kit_bproduct).setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(getActivity());
    }

    private Activity getActivity() {
        return this;
    }
}