package com.pheonix.org.hallaka.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Models.CartDataModel;
import com.pheonix.org.hallaka.Models.ProductDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;
import com.squareup.picasso.Picasso;

public class ProductsHandler extends RecyclerView.Adapter<ProductsViewHolder> {

    List<ProductDataModel> list;

    Activity act;
    boolean buyAble;

    public ProductsHandler(List<ProductDataModel> list, Activity act, boolean buyAble) {
        this.act = act;
        this.list = list;
        this.buyAble = buyAble;
    }

    public ProductsHandler(List<ProductDataModel> list, Activity act) {
        this.list = list;
        this.act = act;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_lay, viewGroup, false);

        return new ProductsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder v, final int i) {

        v.price.setText(list.get(i).getPrice() + " " + "RS");
        v.name.setText(list.get(i).getName());

        Picasso.get().load(list.get(i).getImage()).into(v.img);
        if (buyAble) {
            v.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUpProductLay(list.get(i));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void setUpProductLay(final ProductDataModel productDataModel) {

        showProductLay();

        final int basePrice = Integer.parseInt(productDataModel.getPrice());
        ImageView image, plus, minus;
        final TextView name, price, count;
        Button addToCart;

        image = act.findViewById(R.id.productImage);
        plus = act.findViewById(R.id.productCountPlus);
        minus = act.findViewById(R.id.productCountMin);
        name = act.findViewById(R.id.productName);
        price = act.findViewById(R.id.productPrice);
        count = act.findViewById(R.id.productCount);
        addToCart = act.findViewById(R.id.addToCartButton);

        Picasso.get().load(productDataModel.getImage()).into(image);
        name.setText(productDataModel.getName());
        price.setText(productDataModel.getPrice() + " RS");

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int countI = Integer.parseInt(count.getText().toString());

                if (countI != 0) {
                    final String cartId = FirebaseAuth.getInstance().getUid() + productDataModel.getName() + new Random().nextInt(9999);
                    enableLoadingBar();
                    FirebaseDatabase.getInstance().getReference("users").child("profiles").child(FirebaseAuth.getInstance().getUid()).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userPhone = snapshot.getValue(String.class);

                            CartDataModel cm = new CartDataModel(FirebaseAuth.getInstance().getUid(),productDataModel.getUid(), cartId, productDataModel.getName(), productDataModel.getImage(), productDataModel.getPrice(), countI + "", "cart", userPhone);
                            act.findViewById(R.id.spin_kit_home).setVisibility(View.VISIBLE);
                            Funcs.disableCurrentScreen(act);

                            FirebaseDatabase.getInstance().getReference("cart").child(cartId).setValue(cm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        showSnackBar(productDataModel.getName() + " added to cart!");

                                        hideProductLay();
                                        disableLoadingBar();
                                    } else {
                                        disableLoadingBar();
                                        showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            showSnackBar(error.getMessage());
                        }
                    });
                } else {
                    showSnackBar("Quantity is zero!");
                }
            }

        });

        plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int countI = Integer.parseInt(count.getText().toString());

                if (countI <= 10) {
                    count.setText((countI + 1) + "");
                    price.setText((basePrice * (countI + 1)) + " RS");

                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int countI = Integer.parseInt(count.getText().toString());
                if (countI > 0) {
                    count.setText((countI - 1) + "");
                    price.setText((basePrice * (countI - 1)) + " RS");
                }
            }
        });
    }

    private void showProductLay() {
        act.findViewById(R.id.productLayId).setVisibility(View.VISIBLE);
        act.findViewById(R.id.blackBackProduct).setVisibility(View.VISIBLE);
    }

    private void hideProductLay() {
        act.findViewById(R.id.blackBackProduct).setVisibility(View.GONE);
        act.findViewById(R.id.productLayId).setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void disableLoadingBar() {
        act.findViewById(R.id.spin_kit_home).setVisibility(View.GONE);
        Funcs.enableCurrentScreen(act);
    }

    private void enableLoadingBar() {
        act.findViewById(R.id.spin_kit_home).setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(act);
    }

    private void showSnackBar(String s) {
        Snackbar.make(act.findViewById(R.id.homeLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(act.getResources().getColor(R.color.colorBlue))
                .setTextColor(act.getResources().getColor(R.color.colorWhite))
                .show();
    }
}

class ProductsViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView name, price;

    public ProductsViewHolder(@NonNull final View v) {
        super(v);

        img = v.findViewById(R.id.productImageProLay);
        name = v.findViewById(R.id.productNameProLay);
        price = v.findViewById(R.id.productPriceProLay);

    }

}
