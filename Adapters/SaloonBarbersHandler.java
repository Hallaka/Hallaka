package com.pheonix.org.hallaka.Adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.Home.HomeActivity;
import com.pheonix.org.hallaka.Models.BarberDataModel;
import com.pheonix.org.hallaka.Models.BookingDataModel;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.Models.UserDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class SaloonBarbersHandler extends RecyclerView.Adapter<SaloonBarbersViewHolder> {

    List<BarberDataModel> list;
    boolean bookAble = false;
    Activity act;
    Calendar myCalendar;
    UserDataModel userModel;
    SalonDataModel salonModel;
    int startHour;
    SpinKitView spinKitView;

    public SaloonBarbersHandler(List<BarberDataModel> list) {
        this.list = list;
    }

    public SaloonBarbersHandler(List<BarberDataModel> list, boolean bookAble, Activity act, UserDataModel userModel, SalonDataModel saloonModel, SpinKitView spinKitView) {
        this.list = list;
        this.bookAble = bookAble;
        this.act = act;
        this.userModel = userModel;
        this.salonModel = saloonModel;
        myCalendar = Calendar.getInstance();
        this.spinKitView = spinKitView;
    }


    @NonNull
    @Override
    public SaloonBarbersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.barber_lay, viewGroup, false);

        return new SaloonBarbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SaloonBarbersViewHolder v, final int i) {

        v.name.setText(list.get(i).getName());
        Picasso.get().load(list.get(i).getImage()).placeholder(R.drawable.ic_person).into(v.img);

        if (bookAble) {
            v.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUpTimeLay(list.get(i));
                    act.findViewById(R.id.timeLayBarberSelection).setVisibility(View.VISIBLE);
                    act.findViewById(R.id.blackBackgroundBarberSelection).setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setUpTimeLay(final BarberDataModel dataModel) {
        final TextView timeT, dateT, timeLine;
        Button book;


        book = act.findViewById(R.id.bookBarberBooking);
        timeT = act.findViewById(R.id.startTimeBarberBooking);
        dateT = act.findViewById(R.id.startDateBarberBooking);
        timeLine = act.findViewById(R.id.availableTimeLine);

        int start_hours = Integer.parseInt(dataModel.getsTime());
        int close_hours = Integer.parseInt(dataModel.getcTime());

        StringBuilder startTime = new StringBuilder();
        StringBuilder closeTime = new StringBuilder();

        if (start_hours == 0) {
            startTime.append("12:").append("00").append(" am");
        } else if (start_hours == 12) {
            startTime.append("12:").append("00").append(" pm");
        } else if (start_hours > 12) {
            startTime.append(start_hours - 12).append(":").append("00").append(" pm");
        } else {
            startTime.append(start_hours).append(":").append("00").append(" am");
        }

        if (close_hours == 0) {
            closeTime.append("12:").append("00").append(" am");
        } else if (close_hours == 12) {
            closeTime.append("12:").append("00").append(" pm");
        } else if (close_hours > 12) {
            closeTime.append(close_hours - 12).append(":").append("00").append(" pm");
        } else {
            closeTime.append(close_hours).append(":").append("00").append(" am");
        }

        timeLine.setText("Rate for the selected barber is " + dataModel.getRate() + " Rs\nPlease select time between " + startTime.toString() + " and " + closeTime.toString());


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myFormat = "dd/MM/yy"; //In which you need put here
                final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                if (!timeT.getText().toString().isEmpty()) {
                    int start_hours = Integer.parseInt(dataModel.getsTime());
                    int close_hours = Integer.parseInt(dataModel.getcTime());
                    if (startHour >= start_hours && startHour < close_hours) {
                        if (!dateT.getText().toString().isEmpty()) {
                            Calendar c = Calendar.getInstance();
                            int res = myCalendar.compareTo(c);
                            if (res >= 0) {
                                if (userModel != null && salonModel != null) {

                                    enableLoadingBar();

                                    Random r = new Random();
                                    final String bookId = salonModel.getId() + r.nextInt(99999);

                                    FirebaseDatabase.getInstance().getReference("Bookings").orderByChild("uid").equalTo(userModel.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                int count = 0;
                                                for (DataSnapshot d : snapshot.getChildren()) {
                                                    if (Objects.requireNonNull(d.child("status").getValue(String.class)).equalsIgnoreCase("upcoming")) {
                                                        count++;
                                                    }
                                                }
                                                if (count >= 3) {
                                                    showSnackBar("Booking limit reached!");
                                                    disableLoadingBar();
                                                } else {
                                                    registerBooking(sdf, bookId, dataModel);
                                                }

                                            } else {
                                                registerBooking(sdf, bookId, dataModel);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            disableLoadingBar();
                                            showSnackBar(error.getMessage());
                                        }
                                    });

                                } else {
                                    showSnackBar("Please wait!");
                                }
                            } else {
                                showSnackBar("Selected date not valid!");
                            }
                        } else {
                            showSnackBar("Select a date!");
                        }
                    } else {
                        showSnackBar("Time not available!");
                    }
                } else {
                    showSnackBar("Select Appointment Date!");
                }
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateT.setText(sdf.format(myCalendar.getTime()));

            }

        };

        dateT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(act, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        timeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(act, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder time = new StringBuilder();
                        startHour = selectedHour;
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
                        timeT.setText(time.toString());
                    }
                }, 8, 0, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private void registerBooking(final SimpleDateFormat sdf, final String bookId, final BarberDataModel dataModel) {

        FirebaseDatabase.getInstance().getReference("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAvailable = true;
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        BookingDataModel b = d.getValue(BookingDataModel.class);
                        if (b.getbId().equalsIgnoreCase(dataModel.getId())) {
                            if (b.getDate().equalsIgnoreCase(sdf.format(myCalendar.getTime()))) {
                                if (b.getTime().equalsIgnoreCase(startHour + "")) {
                                    isAvailable = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (isAvailable) {

                        FirebaseDatabase.getInstance().getReference("users").child("profiles").child(salonModel.getOwnerId()).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                BookingDataModel d = new BookingDataModel(sdf.format(myCalendar.getTime()), startHour + "", dataModel.getName(), userModel.getName(), salonModel.getName(), snapshot.getValue(String.class), salonModel.getId(), dataModel.getId(), userModel.getUid(), "upcoming", dataModel.getRate(), bookId);
                                FirebaseDatabase.getInstance().getReference("Bookings").child(bookId).setValue(d).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            showSnackBar("Booking Added to Cart");
                                            disableLoadingBar();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent i = new Intent(act, HomeActivity.class);
                                                    i.putExtra("data", userModel.getEmail());
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    act.startActivity(i);
                                                }
                                            }, 1000);

                                        } else {
                                            showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                            disableLoadingBar();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        showSnackBar("Appointment time not available!");
                        disableLoadingBar();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void disableLoadingBar() {
        spinKitView.setVisibility(View.GONE);
        Funcs.enableCurrentScreen(act);
    }

    private void enableLoadingBar() {
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(act);
    }

    public void showSnackBar(String s) {
        Snackbar.make(act.findViewById(R.id.barbersSelectionLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(act.getResources().getColor(R.color.colorBlue))
                .setTextColor(act.getResources().getColor(R.color.colorWhite))
                .show();
    }
}

class SaloonBarbersViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView name;

    public SaloonBarbersViewHolder(@NonNull final View v) {
        super(v);

        img = v.findViewById(R.id.barberPicBarberLay);
        name = v.findViewById(R.id.barberNameBarberLay);

    }


}
