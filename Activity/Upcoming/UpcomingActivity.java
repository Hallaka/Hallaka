package com.pheonix.org.hallaka.Activity.Upcoming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Adapters.UpcomingHandler;
import com.pheonix.org.hallaka.Models.BookingDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.ArrayList;
import java.util.List;

public class UpcomingActivity extends AppCompatActivity {
    RecyclerView upcomingRecycler;
    SpinKitView spinKitView;
    List<BookingDataModel> list = new ArrayList<>();
    UpcomingHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        upcomingRecycler = findViewById(R.id.upcomingRecycler);
        spinKitView = findViewById(R.id.spin_kit_upcoming);

        upcomingRecycler.setLayoutManager(new LinearLayoutManager(this));
        handler = new UpcomingHandler(list, getActivity(), spinKitView);
        upcomingRecycler.setAdapter(handler);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingBar();

        FirebaseDatabase.getInstance().getReference("Bookings").orderByChild("status").equalTo("upcoming").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    findViewById(R.id.nothingHereUpcoming).setVisibility(View.GONE);
                    list.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        BookingDataModel b = d.getValue(BookingDataModel.class);
                        if (b.getUid().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())) {
                            list.add(b);
                        }
                    }
                } else {
                    findViewById(R.id.nothingHereUpcoming).setVisibility(View.VISIBLE);
                }
                handler.notifyDataSetChanged();
                hideLoadingBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    private Activity getActivity() {
        return UpcomingActivity.this;
    }

    public void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.upcomingLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}