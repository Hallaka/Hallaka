package com.pheonix.org.hallaka.Activity.AddSaloon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pheonix.org.hallaka.Models.SalonDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Random;

public class AddSalonActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SALOON_IMAGE_REQUEST_CODE = 28913;
    ImageView image;
    EditText name, city;
    Button submit;

    SpinKitView spinKitView;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon);

        image = findViewById(R.id.salonImageAddSalon);
        name = findViewById(R.id.salonNameAddSalon);
        city = findViewById(R.id.salonCityAddSalon);
        submit = findViewById(R.id.addSalonId);
        spinKitView = findViewById(R.id.spin_kit);

        submit.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    public void Submit() {
        final String sName = name.getText().toString();
        final String sCity = city.getText().toString();

        if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sCity)) {
            if (imgUri != null) {
                spinKitView.setVisibility(View.VISIBLE);
                Funcs.disableCurrentScreen(getActivity());

                final Random r = new Random();
                final String pId = FirebaseAuth.getInstance().getUid() + r.nextInt(99999);

                final StorageReference pic1Ref = FirebaseStorage.getInstance().getReference("saloons").child(pId + ".jpg");

                pic1Ref.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            pic1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    SalonDataModel product = new SalonDataModel(sName,sCity,uri.toString(),pId,FirebaseAuth.getInstance().getUid());
                                    FirebaseDatabase.getInstance().getReference("saloons").child(pId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                showSnackBar("Product added!");

                                                finish();
                                            } else {
                                                showSnackBar("Failed to add product!");
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else{
                            Snackbar.make(findViewById(R.id.addSaloonLay), "Failed to upload image", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                                    .setTextColor(getResources().getColor(R.color.colorWhite))
                                    .show();
                        }
                    }
                });

            } else {
                showSnackBar("Saloon Cover not added!");
            }
        } else {
            showSnackBar("Enter required values in above fields");
        }

    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.addSaloonLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }

    private Activity getActivity() {
        return AddSalonActivity.this;
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            Submit();
        }
        if (v == image) {
            startActivityForResult(
                    new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    ), SALOON_IMAGE_REQUEST_CODE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SALOON_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri dataR = data.getData();

            if (dataR != null) {

                image.setImageURI(dataR);
                imgUri = dataR;

            }

        }
    }
}