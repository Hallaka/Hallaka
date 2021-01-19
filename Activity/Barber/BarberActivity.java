package com.pheonix.org.hallaka.Activity.Barber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.AddSaloon.AddSalonActivity;
import com.pheonix.org.hallaka.Activity.BarberHistory.BarberHistoryActivity;
import com.pheonix.org.hallaka.Activity.Covid.CovidActivity;
import com.pheonix.org.hallaka.Activity.Login.LoginActivity;
import com.pheonix.org.hallaka.Activity.Product.ProductsActivity;
import com.pheonix.org.hallaka.Activity.Salon.SalonActivity;
import com.pheonix.org.hallaka.Activity.ViewBooking.ViewBookingActivity;
import com.pheonix.org.hallaka.R;

public class BarberActivity extends AppCompatActivity {

    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView name;
    LinearLayout productsLay;
    LinearLayout salonLay,bookingLay,historyLay,covidLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber);

        setUpDrawerListeners();

        imageView = findViewById(R.id.menuBtnBarber);
        drawerLayout = findViewById(R.id.drawerLayBarber);
        navigationView = findViewById(R.id.navLayBarber);
        name = findViewById(R.id.userNameBarber);

        bookingLay=findViewById(R.id.bookingLayBarber);
        historyLay=findViewById(R.id.historyLayBarber);
        productsLay = findViewById(R.id.productLayBarber);
        salonLay = findViewById(R.id.salonLayBarber);
        covidLay=findViewById(R.id.covidLayBarber);

        FirebaseDatabase.getInstance().getReference("users").child("profiles").child(FirebaseAuth.getInstance().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        historyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), BarberHistoryActivity.class));
            }
        });
        bookingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ViewBookingActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(navigationView);

            }
        });

        productsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BarberActivity.this, ProductsActivity.class);
                startActivity(intent);

            }
        });
        covidLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BarberActivity.this, CovidActivity.class);
                startActivity(intent);

            }
        });

        salonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("saloons").orderByChild("ownerId").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            startActivity(new Intent(BarberActivity.this, SalonActivity.class));

                        } else {
                            addSaloonIntent();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    void setUpDrawerListeners() {
        TextView exit, signOut;

        exit = findViewById(R.id.exitIdBarber);
        signOut = findViewById(R.id.signOutBarber);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BarberActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void addSaloonIntent() {
        Intent intent = new Intent(BarberActivity.this, AddSalonActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Alert").setMessage("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }
}