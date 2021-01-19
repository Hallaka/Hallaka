package com.pheonix.org.hallaka.Activity.AddBarber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pheonix.org.hallaka.Models.BarberDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Random;

public class AddBarberActivity extends AppCompatActivity {

    private static final int BARBER_IMAGE_REQUEST_CODE = 9901;
    ImageView image;
    EditText name, cRate;
    TextView sTiming, cTiming;
    Button submit;
    String saloonId;
    Uri img;

    SpinKitView spinKitView;
    private int startHour;
    private int endHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barber);

        try {
            saloonId = getIntent().getExtras().getString("sId");
        } catch (Exception ignore) {
        }


        cRate = findViewById(R.id.barberRateAddBarber);
        image = findViewById(R.id.barberImageAddBarber);
        name = findViewById(R.id.barberNameAddBarber);
        sTiming = findViewById(R.id.barberSTimingAddBarber);
        cTiming = findViewById(R.id.barberCTimingAddBarber);
        submit = findViewById(R.id.addBarberId);
        spinKitView = findViewById(R.id.spin_kit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Submit();

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        ), BARBER_IMAGE_REQUEST_CODE
                );
            }
        });

        sTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuilder time = new StringBuilder();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

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
                        sTiming.setText(time.toString());
                    }
                }, 8, 0, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        cTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder time = new StringBuilder();
                        endHour = selectedHour;
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
                        cTiming.setText(time.toString());
                    }
                }, 8, 0, false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


    }

    private Context getContext() {
        return AddBarberActivity.this;
    }

    public void Submit() {
        final String sName = name.getText().toString();
        final String rate = cRate.getText().toString();
        final String sSTiming = startHour + "";
        final String sCTiming = endHour + "";
        Random r = new Random();
        final String bId = saloonId + r.nextInt(9999);

        if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sTiming.getText().toString()) && !TextUtils.isEmpty(cTiming.getText().toString()) && !TextUtils.isEmpty(rate)) {
            if (endHour - startHour > 2) {
                if (img != null) {
                    spinKitView.setVisibility(View.VISIBLE);
                    Funcs.disableCurrentScreen(getActivity());
                    final StorageReference pic1Ref = FirebaseStorage.getInstance().getReference("saloons").child("barbers").child(bId + ".jpg");

                    pic1Ref.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                pic1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        BarberDataModel b = new BarberDataModel(sName, rate, uri.toString(), sSTiming, sCTiming, bId);
                                        FirebaseDatabase.getInstance().getReference("saloons").child(saloonId).child("barbers").child(b.getId()).setValue(b).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    showSnackBar("Barber Added!");
                                                    spinKitView.setVisibility(View.GONE);

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Funcs.enableCurrentScreen(getActivity());
                                                            finish();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    spinKitView.setVisibility(View.GONE);
                                                    Funcs.enableCurrentScreen(getActivity());
                                                    showSnackBar(task.getException().getMessage());
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                Snackbar.make(findViewById(R.id.addSaloonLay), "Failed to upload image", Snackbar.LENGTH_LONG)
                                        .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                                        .setTextColor(getResources().getColor(R.color.colorWhite))
                                        .show();
                                spinKitView.setVisibility(View.GONE);
                                Funcs.enableCurrentScreen(getActivity());
                            }
                        }
                    });

                } else {
                    showSnackBar("Image not uploaded!");
                }
            }else{
                showSnackBar("Working hours should be greater than 2 hours");
            }
        } else {
            showSnackBar("Enter required values in above fields");
        }

    }

    private Activity getActivity() {
        return AddBarberActivity.this;
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.addBarberLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARBER_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri dataR = data.getData();

            if (dataR != null) {

                image.setImageURI(dataR);
                img = dataR;

            }

        }
    }
}