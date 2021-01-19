package com.pheonix.org.hallaka.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.pheonix.org.hallaka.Models.BookingDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryHandler extends RecyclerView.Adapter<HistoryViewHolder> {

    List<BookingDataModel> list;
    Calendar today;

    public HistoryHandler(List<BookingDataModel> list) {
        this.list = list;
        today = Calendar.getInstance(TimeZone.getDefault());
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_lay, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder v, final int i) {
        final BookingDataModel d = list.get(i);
        v.date.setText(d.getDate());
        v.time.setText(d.getTime() + " : 00");
        v.userName.setText(d.getCustomerName());
        v.barberName.setText(d.getBarberName());
        v.saloonName.setText(d.getSaloonName());
        v.bookNo.setText("Booking #" + (i+1));
        v.status.setText(d.getStatus());

    }


    @Override
    public int getItemCount() {

        return list.size();
    }
}

class HistoryViewHolder extends RecyclerView.ViewHolder {
    TextView bookNo, saloonName, barberName, userName, status, date, time;

    public HistoryViewHolder(@NonNull final View v) {
        super(v);

        bookNo = v.findViewById(R.id.bookingNoHistoryLay);
        saloonName = v.findViewById(R.id.saloonNameHistoryLay);
        barberName = v.findViewById(R.id.barberNameHistoryLay);
        userName = v.findViewById(R.id.userNameHistoryLay);
        status = v.findViewById(R.id.statusHistoryLay);
        date = v.findViewById(R.id.dateHistoryLay);
        time = v.findViewById(R.id.timeHistoryLay);

    }

}
