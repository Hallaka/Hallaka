package com.pheonix.org.hallaka.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.pheonix.org.hallaka.Models.BookingDataModel;
import com.pheonix.org.hallaka.Models.ProductDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class UpcomingHandler extends RecyclerView.Adapter<UpcomingViewHolder> {

    List<BookingDataModel> list;

    Activity act;
    Calendar today;
    SpinKitView spinKitView;

    public UpcomingHandler(List<BookingDataModel> list, Activity act, SpinKitView spinKitView) {
        this.list = list;
        this.act = act;
        this.spinKitView = spinKitView;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_lay, viewGroup, false);
        today = Calendar.getInstance(TimeZone.getDefault());
        return new UpcomingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final UpcomingViewHolder v, final int i) {
        final BookingDataModel d = list.get(i);
        v.date.setText(d.getDate());
        v.time.setText(d.getTime() + " : 00");
        v.userName.setText(d.getCustomerName());
        v.barberName.setText(d.getBarberName());
        v.saloonName.setText(d.getSaloonName());
        v.bookNo.setText("Booking #" + (i+1));
        v.phone.setText(d.getBarberPhone());
        v.price.setText(d.getPrice());

        String myFormat = "dd/MM/yy"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String todayDate = sdf.format(today.getTime());

        String[] parts = d.getDate().split("/", 3);
        int aDay = Integer.parseInt(parts[0]);
        int aMonth = Integer.parseInt(parts[1]);
        int aYear = Integer.parseInt(parts[2]);

        Calendar c = Calendar.getInstance();
        c.set(aYear, aMonth, aDay, 23, 59);

        v.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking(i,d);
            }
        });

        if (todayDate.equalsIgnoreCase(d.getDate())) {
            v.markAsDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeBooking(d, i);
                }
            });
        } else if (c.compareTo(today) < 0) {
            v.markAsDone.setVisibility(View.GONE);
        } else {
            cancelBooking(i, d);
        }
    }

    private void completeBooking(BookingDataModel d, final int i) {
        showLoadingBar();
        FirebaseDatabase.getInstance().getReference("Bookings").child(d.getId()).child("status").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    list.remove(i);
                    notifyDataSetChanged();
                    showSnackBar("Booking #"+(i+1)+" moved to history!");
                    hideLoadingBar();
                    if(list.isEmpty()){
                        act.findViewById(R.id.nothingHereUpcoming).setVisibility(View.VISIBLE);
                    }
                } else {
                    showSnackBar("Failed to update!");
                    hideLoadingBar();
                }
            }
        });
    }

    private void cancelBooking(final int i, BookingDataModel d) {
        FirebaseDatabase.getInstance().getReference("Bookings").child(d.getId()).child("status").setValue("Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    list.remove(i);
                    notifyDataSetChanged();
                    showSnackBar("Booking #"+(i+1)+" moved to history!");
                    if(list.isEmpty()){
                        act.findViewById(R.id.nothingHereUpcoming).setVisibility(View.VISIBLE);
                    }
                } else {
                    showSnackBar("Failed to  move");
                }
            }
        });
    }

    private void hideLoadingBar() {
        spinKitView.setVisibility(View.GONE);
        Funcs.enableCurrentScreen(act);
    }

    private void showLoadingBar() {
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(act);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }
    public void showSnackBar(String s) {
        Snackbar.make(act.findViewById(R.id.upcomingLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(act.getResources().getColor(R.color.colorBlue))
                .setTextColor(act.getResources().getColor(R.color.colorWhite))
                .show();
    }
}

class UpcomingViewHolder extends RecyclerView.ViewHolder {
    TextView bookNo, saloonName, barberName, userName, price, date, time,phone;
    Button markAsDone,cancel;

    public UpcomingViewHolder(@NonNull final View v) {
        super(v);
        cancel=v.findViewById(R.id.cancelBooking);
        bookNo = v.findViewById(R.id.bookingNoLay);
        saloonName = v.findViewById(R.id.saloonNameLay);
        barberName = v.findViewById(R.id.barberNameLay);
        userName = v.findViewById(R.id.userNameLay);
        price = v.findViewById(R.id.priceLay);
        date = v.findViewById(R.id.dateLay);
        phone=v.findViewById(R.id.barberPhoneLay);
        time = v.findViewById(R.id.timeLay);
        markAsDone = v.findViewById(R.id.markAsDone);

    }

}
