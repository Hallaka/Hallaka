package com.pheonix.org.hallaka.Activity.Salon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.AddBarber.AddBarberActivity;
import com.pheonix.org.hallaka.Adapters.SaloonBarbersHandler;
import com.pheonix.org.hallaka.Models.BarberDataModel;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalonActivity extends AppCompatActivity implements View.OnClickListener {

    SpinKitView spinKitView;
    Button addBarber;
    TextView sName, sCity;
    ImageView sCover;
    SalonDataModel salonDataModel;
    RecyclerView barbersR;

    List<BarberDataModel> barbersList = new ArrayList<>();
    SaloonBarbersHandler barbersHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        setUpTimingLayandListeners();

        barbersR = findViewById(R.id.barberDataSalon);
        sName = findViewById(R.id.salonNameSalon);
        sCity = findViewById(R.id.salonCitySalon);
        sCover = findViewById(R.id.salonImageSalon);
        spinKitView = findViewById(R.id.spin_kit);
        addBarber = findViewById(R.id.AddBarberIdSalon);
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(getActivity());

        barbersR.setLayoutManager(new GridLayoutManager(getContext(), 2));
        barbersHandler = new SaloonBarbersHandler(barbersList);
        barbersR.setAdapter(barbersHandler);
        addBarber.setOnClickListener(this);

    }

    private void updateSaloonData() {
        FirebaseDatabase.getInstance().getReference("saloons").orderByChild("ownerId").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    spinKitView.setVisibility(View.GONE);
                    Funcs.enableCurrentScreen(getActivity());
                    for (DataSnapshot d : snapshot.getChildren()) {
                        SalonDataModel sData = d.getValue(SalonDataModel.class);
                        setValues(Objects.requireNonNull(sData));
                        if (!d.child("barbers").exists()) {
                            findViewById(R.id.timingLaySaloon).setVisibility(View.VISIBLE);
                            findViewById(R.id.blackBackSaloon).setVisibility(View.VISIBLE);
                        } else {
                            barbersList.clear();
                            d.child("barbers").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot d : snapshot.getChildren()) {
                                        barbersList.add(d.getValue(BarberDataModel.class));
                                    }
                                    barbersHandler.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    showSnackBar(error.getMessage());
                                }
                            });
                        }
                    }
//
                } else {
                    showSnackBar("Saloon not found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                spinKitView.setVisibility(View.GONE);
                Funcs.enableCurrentScreen(getActivity());
                showSnackBar(error.getMessage());
            }
        });
    }

    public void setUpTimingLayandListeners() {
        //Timing Lay Attrs
        final TextView startT, endT;
        final EditText rateT;
        Button setT;

        final Integer[] startHour = new Integer[1];
        final Integer[] endHour = new Integer[1];
        rateT = findViewById(R.id.rateBarberSaloon);
        startT = findViewById(R.id.startTimeBarberSaloon);
        endT = findViewById(R.id.endTimeBarberSaloon);
        setT = findViewById(R.id.setBarberTiming);

        startT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder time = new StringBuilder();
                        startHour[0] = selectedHour;
                        String selectedMins = selectedMinute + "";
                        if (selectedMins.length() == 1) {
                            selectedMins = "0" + selectedMinute;
                        }
                        if (selectedHour == 0) {
                            time.append("12:").append(selectedMins).append(" am");
                        } else if (selectedHour == 12) {
                            time.append("12:").append(selectedMins).append(" pm");
                        } else if (selectedHour > 12) {
                            time.append(selectedHour - 12).append(":").append(selectedMins).append(" pm");
                        } else {
                            time.append(selectedHour).append(":").append(selectedMins).append(" am");
                        }
                        startT.setText(time.toString());
                    }
                }, 8, 0, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        endT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder time = new StringBuilder();
                        endHour[0] = selectedHour;
                        String selectedMins = selectedMinute + "";
                        if (selectedMins.length() == 1) {
                            selectedMins = "0" + selectedMinute;
                        }
                        if (selectedHour == 0) {
                            time.append("12:").append(selectedMins).append(" am");
                        } else if (selectedHour == 12) {
                            time.append("12:").append(selectedMins).append(" pm");
                        } else if (selectedHour > 12) {
                            time.append(selectedHour - 12).append(":").append(selectedMins).append(" pm");
                        } else {
                            time.append(selectedHour).append(":").append(selectedMins).append(" am");
                        }
                        endT.setText(time.toString());
                    }
                }, 8, 0, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        setT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startHour[0] != null && endHour[0] != null) {
                    if (!rateT.getText().toString().isEmpty()) {
                        if (endHour[0] - startHour[0] > 2) {
                            spinKitView.setVisibility(View.VISIBLE);
                            Funcs.disableCurrentScreen(getActivity());
                            FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String name = snapshot.child("name").getValue(String.class);
                                        String img = snapshot.child("img").getValue(String.class);
                                        BarberDataModel b = new BarberDataModel(name, rateT.getText().toString(), img, startHour[0] + "", endHour[0] + "", FirebaseAuth.getInstance().getUid());
                                        FirebaseDatabase.getInstance().getReference("saloons").child(salonDataModel.getId()).child("barbers").child(b.getId()).setValue(b).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    showSnackBar("Saloon updated!");
                                                    findViewById(R.id.timingLaySaloon).setVisibility(View.GONE);
                                                    findViewById(R.id.blackBackSaloon).setVisibility(View.GONE);
                                                    //update recycler view items
                                                    updateSaloonData();
                                                    spinKitView.setVisibility(View.GONE);
                                                    Funcs.enableCurrentScreen(getActivity());
                                                } else {
                                                    spinKitView.setVisibility(View.GONE);
                                                    Funcs.enableCurrentScreen(getActivity());
                                                    showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                                }
                                            }
                                        });
                                    } else {
                                        showSnackBar("User not found!");
                                        spinKitView.setVisibility(View.GONE);
                                        Funcs.enableCurrentScreen(getActivity());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    spinKitView.setVisibility(View.GONE);
                                    Funcs.enableCurrentScreen(getActivity());
                                    showSnackBar(error.getMessage());
                                }
                            });
                        } else {
                            showSnackBar("Working hours should be greater than 2 hours");
                        }
                    } else {
                        showSnackBar("Rate cannot be empty!");
                    }
                } else {
                    showSnackBar("Time fields not set!");
                }

            }
        });
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return SalonActivity.this;
    }

    private void setValues(SalonDataModel sData) {
        salonDataModel = sData;
        sName.setText(sData.getName());
        sCity.setText(sData.getCity());
        Picasso.get().load(sData.getImage()).priority(Picasso.Priority.HIGH).placeholder(R.drawable.loading_img).into(sCover);
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.salonActLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v == addBarber) {
            Intent intent = new Intent(SalonActivity.this, AddBarberActivity.class);
            intent.putExtra("sId", salonDataModel.getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSaloonData();
    }
}