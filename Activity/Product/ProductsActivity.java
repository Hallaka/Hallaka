package com.pheonix.org.hallaka.Activity.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.AddProduct.AddProductActivity;
import com.pheonix.org.hallaka.Activity.BookedProducts.BookedProductsActivity;
import com.pheonix.org.hallaka.Adapters.ProductsHandler;
import com.pheonix.org.hallaka.Models.ProductDataModel;
import com.pheonix.org.hallaka.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    RecyclerView products;
    FloatingActionButton addProduct;
    List<ProductDataModel> count = new ArrayList<>();
    ProductsHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        products = findViewById(R.id.productIdProducts);
        addProduct = findViewById(R.id.addProductIdProducts);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        products.setLayoutManager(layoutManager);

        handler = new ProductsHandler(count, getActivity());
        products.setAdapter(handler);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(intent);

            }
        });

    }

    private Activity getActivity() {
        return ProductsActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("products").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    count.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        ProductDataModel dataModel = d.getValue(ProductDataModel.class);
                        count.add(dataModel);
                    }
                }
                handler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void productCartClick(View view) {
        startActivity(new Intent(view.getContext(), BookedProductsActivity.class));
    }
}