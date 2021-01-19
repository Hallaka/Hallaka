package com.pheonix.org.hallaka.Activity.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.SaloonsHandler;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    RecyclerView saloonsR;
    SaloonsHandler handler;
    List<SalonDataModel> list = new ArrayList<>(), allSaloons = new ArrayList<>();
    SpinKitView spinKit;
    EditText searchView;
    ImageView search;
    boolean searched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        saloonsR = findViewById(R.id.saloonsList);
        spinKit = findViewById(R.id.spin_kit_saloon);
        searchView = findViewById(R.id.userSearchIdBooking);
        search = findViewById(R.id.searchIcon);

        saloonsR.setLayoutManager(new GridLayoutManager(getContext(), 2));
        handler = new SaloonsHandler(list);
        saloonsR.setAdapter(handler);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searched) {
                    refreshSaloonsList();
                    search.setImageResource(R.drawable.ic_search);
                    searched = false;
                } else {

                    final String searchTxt = searchView.getText().toString();
                    FirebaseDatabase.getInstance().getReference("saloons").orderByChild("city").equalTo(searchTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                searched = true;
                                list.clear();
                                for (DataSnapshot d : snapshot.getChildren()) {
                                    list.add(d.getValue(SalonDataModel.class));
                                }
                                search.setImageResource(R.drawable.ic_close);
                                handler.notifyDataSetChanged();
                            } else {
                                showSnackBar("No saloon found in city " + searchTxt);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSaloonsList();
    }

    private void refreshSaloonsList() {
        spinKit.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(getActivity());
        FirebaseDatabase.getInstance().getReference("saloons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        list.add(d.getValue(SalonDataModel.class));
                    }
                    setUpList(list);
                    spinKit.setVisibility(View.GONE);
                    Funcs.enableCurrentScreen(getActivity());
                    handler.notifyDataSetChanged();
                } else {
                    showSnackBar("No Saloon Registered!");
                    Funcs.enableCurrentScreen(getActivity());
                    spinKit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showSnackBar(error.getMessage());
                spinKit.setVisibility(View.GONE);
                Funcs.enableCurrentScreen(getActivity());
            }
        });
    }

    private void setUpList(List<SalonDataModel> list) {
        allSaloons = list;
    }

    private Activity getActivity() {
        return BookingActivity.this;
    }

    private Context getContext() {
        return BookingActivity.this;
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.bookingLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}