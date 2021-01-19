package com.pheonix.org.hallaka.Activity.Home.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.Booking.BookingActivity;
import com.pheonix.org.hallaka.Activity.Cart.CartActivity;
import com.pheonix.org.hallaka.Activity.Covid.CovidActivity;
import com.pheonix.org.hallaka.Activity.History.HistoryActivity;
import com.pheonix.org.hallaka.Activity.Login.LoginActivity;
import com.pheonix.org.hallaka.Activity.Upcoming.UpcomingActivity;
import com.pheonix.org.hallaka.Adapters.ViewAdapter;
import com.pheonix.org.hallaka.Models.UserDataModel;
import com.pheonix.org.hallaka.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    int NUM_PAGES, NUM_PAGES2;
    LinearLayout bookingLay, upcomingLay, historyLay, cartLay , covidLay;
    TextView userName, type;
    ViewPager viewPager, lookBook;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ViewAdapter viewAdapter, viewAdapter2;
    private int currentPage, currentPage2;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setUpDrawerListeners();

        viewPager = view.findViewById(R.id.pager);
        lookBook = view.findViewById(R.id.lookBookIdHome);
        Integer[] lBook = {R.drawable.look_book, R.drawable.look_book_2, R.drawable.book2};
        Integer[] images = {R.drawable.poster1, R.drawable.rate_2};

        viewAdapter2 = new ViewAdapter(getContext(), lBook);
        viewAdapter = new ViewAdapter(getContext(), images);

        lookBook.setAdapter(viewAdapter2);
        viewPager.setAdapter(viewAdapter);
        viewPager.requestFocus();


        currentPage = 0;
        currentPage2 = 0;
        NUM_PAGES = images.length;
        NUM_PAGES2 = lBook.length;

        try {

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    try {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                viewPager.setCurrentItem(currentPage++, true);
                            }
                        });
                    } catch (Exception ignore) {
                    }
                }
            }, 100, 6000);

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    try {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPage2 == NUM_PAGES2) {
                                    currentPage2 = 0;
                                }
                                lookBook.setCurrentItem(currentPage2++, true);
                            }
                        });
                    } catch (Exception ignore) {
                    }
                }
            }, 0, 3200);

        } catch (Exception ignore) {
        }

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        upcomingLay = view.findViewById(R.id.upComingLayHome);
        historyLay = view.findViewById(R.id.historyLayHome);
        bookingLay = view.findViewById(R.id.bookingLayHome);
        cartLay = view.findViewById(R.id.cartLayHome);
        covidLay=view.findViewById(R.id.covidLayHome);
        userName = view.findViewById(R.id.userNameHome);
        type = view.findViewById(R.id.typeHome);

        final Bundle s = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        FirebaseDatabase.getInstance().getReference("users").child("profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    UserDataModel userDataModel = d.getValue(UserDataModel.class);
                    if (Objects.requireNonNull(userDataModel).getEmail().equals(s.getString("data"))) {
                        userName.setText(userDataModel.getName());
                        type.setText(userDataModel.getType());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showSnackBar(databaseError.getMessage());
            }
        });

        view.findViewById(R.id.menuBtnHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        bookingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                startActivity(intent);
            }
        });
        upcomingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpcomingActivity.class));
            }
        });
        historyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
        cartLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        });
        covidLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CovidActivity.class));
            }
        });
        return view;
    }

    public void openDrawer() {
        ((DrawerLayout) Objects.requireNonNull(getActivity()).findViewById(R.id.homeLay)).openDrawer(GravityCompat.START);
    }

    void setUpDrawerListeners() {
        TextView exit, signOut;

        exit = getActivity().findViewById(R.id.exitIdHome);
        signOut = getActivity().findViewById(R.id.signOutHome);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

    }

    private void showSnackBar(String s) {
        Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.homeFragLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}