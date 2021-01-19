package com.pheonix.org.hallaka.Activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.Barber.BarberActivity;
import com.pheonix.org.hallaka.Activity.Home.HomeActivity;
import com.pheonix.org.hallaka.Activity.Login.LoginActivity;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE = 342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isNetworkConnected()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String type = snapshot.child("type").getValue(String.class);

                                if (Objects.requireNonNull(type).equalsIgnoreCase("Customer")) {

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    intent.putExtra("data", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(MainActivity.this, BarberActivity.class);
                                    intent.putExtra("data", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nextIntent();
                        }
                    }, 1000);
                }
            } else {
                requestPermission();
            }
        }
        else {
            showSnackBar("<b>Internet not connected!</b>");

            try {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if(isNetworkConnected()){
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                            finish();
                            cancel();
                        }
                    }
                },0,2000);
            }catch (Exception ignore){}


        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
    }

    private void nextIntent() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                nextIntent();
            } else {
                requestPermission();
            }
        }
    }
    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.mainLay), Html.fromHtml(s), Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(getResources().getColor(R.color.colorWhite))
                .setTextColor(getResources().getColor(R.color.colorBlue))
                .show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}