package com.pheonix.org.hallaka.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.pheonix.org.hallaka.Activity.Cart.CartActivity;
import com.pheonix.org.hallaka.Models.CartDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class BookedProductsHandler extends RecyclerView.Adapter<BookedPViewHolder> {

    List<CartDataModel> list;

    Activity act;

    public BookedProductsHandler(List<CartDataModel> list, Activity act) {
        this.list = list;
        this.act = act;
    }

    @NonNull
    @Override
    public BookedPViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookedp_item_lay, viewGroup, false);

        return new BookedPViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BookedPViewHolder v, final int i) {
        final CartDataModel d = list.get(i);

        final int baseRate = Integer.parseInt(d.gettPrice());
        int quantityI = Integer.parseInt(d.getQuantity());

        v.name.setText(d.getProductName());
        v.price.setText((quantityI * baseRate) + " Rs");
        v.quantity.setText("Quantity: " + d.getQuantity());
        v.bPhone.setText("Buyer Contact: "+d.getBuyerPhone());
        Picasso.get().load(d.getProductImage()).into(v.img);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}

class BookedPViewHolder extends RecyclerView.ViewHolder {

    TextView name, price, quantity, bPhone;
    ImageView img;

    public BookedPViewHolder(@NonNull final View v) {
        super(v);

        name = v.findViewById(R.id.productNameBookedP);
        price = v.findViewById(R.id.productPriceBookedP);
        quantity = v.findViewById(R.id.productQuantBookedP);
        img = v.findViewById(R.id.productImageBookedP);
        bPhone = v.findViewById(R.id.buyerPhoneBookedP);
    }
}
