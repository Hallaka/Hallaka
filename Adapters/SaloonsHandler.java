package com.pheonix.org.hallaka.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pheonix.org.hallaka.Activity.BarbersSelection.BarbersSelectionActivity;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SaloonsHandler extends RecyclerView.Adapter<SaloonViewHolder> {

    List<SalonDataModel> list;

    public SaloonsHandler(List<SalonDataModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SaloonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saloon_lay, viewGroup, false);

        return new SaloonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SaloonViewHolder v, final int i) {

        v.name.setText(list.get(i).getName());
        Picasso.get().load(list.get(i).getImage()).placeholder(R.drawable.ic_person).into(v.img);

        v.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(v.getContext(), BarbersSelectionActivity.class);
                in.putExtra("sId",list.get(i).getId());
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SaloonViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView name;

    public SaloonViewHolder(@NonNull final View v) {
        super(v);

        img = v.findViewById(R.id.saloonImageProLay);
        name = v.findViewById(R.id.saloonNameProLay);

    }

}
