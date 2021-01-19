package com.pheonix.org.hallaka.Activity.BarbersSelection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.SaloonBarbersHandler;
import com.pheonix.org.hallaka.Models.BarberDataModel;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.Models.UserDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BarbersSelectionActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SaloonBarbersHandler handler;
    List<BarberDataModel> list = new ArrayList<>();
    String saloonId;
    SpinKitView spinKitView;
    SalonDataModel salonModel;
    UserDataModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers_selection);

        try {
            saloonId = getIntent().getExtras().getString("sId");
        } catch (Exception ignore) {
        }

        spinKitView = findViewById(R.id.spin_kit_barber_selection);
        recyclerView = findViewById(R.id.recyclerBarbersSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        findViewById(R.id.blackBackgroundBarberSelection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.timeLayBarberSelection).setVisibility(View.GONE);
                findViewById(R.id.blackBackgroundBarberSelection).setVisibility(View.GONE);
            }
        });

    }

    private Context getContext() {
        return BarbersSelectionActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpBarbers();

    }

    private void setUpBarbers() {
        showLoadingBar();
        FirebaseDatabase.getInstance().getReference("saloons").child(saloonId).child("barbers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        list.add(d.getValue(BarberDataModel.class));
                    }
                    loadUserData();
                    hideLoadingBar();

                } else {
                    hideLoadingBar();
                    showSnackBar("No barbers registered!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showSnackBar(error.getMessage());
                hideLoadingBar();
            }
        });
    }

    private void showLoadingBar() {
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(getActivity());
    }

    private void hideLoadingBar() {
        spinKitView.setVisibility(View.GONE);
        Funcs.enableCurrentScreen(getActivity());
    }

    private void loadUserData() {
        showLoadingBar();
        FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setUpUserData(snapshot);
                    hideLoadingBar();
                    loadSaloonData();
                } else {
                    showSnackBar("User not found!");
                    hideLoadingBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showSnackBar(error.getMessage());
                hideLoadingBar();
            }
        });
    }

    private void loadSaloonData() {
        showLoadingBar();
        FirebaseDatabase.getInstance().getReference("saloons").child(saloonId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setUpSaloonData(snapshot);
                    handler = new SaloonBarbersHandler(list, true, getActivity(), userModel, salonModel, spinKitView);
                    recyclerView.setAdapter(handler);
                } else {
                    showSnackBar("Saloon not found!");
                }
                hideLoadingBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showSnackBar(error.getMessage());
                hideLoadingBar();
            }
        });
    }

    private void setUpSaloonData(DataSnapshot snapshot) {
        salonModel = snapshot.getValue(SalonDataModel.class);
    }

    private void setUpUserData(@NonNull DataSnapshot snapshot) {
        userModel = snapshot.getValue(UserDataModel.class);
    }

    private Activity getActivity() {
        return BarbersSelectionActivity.this;
    }


    public void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.barbersSelectionLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }

}