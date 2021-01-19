package com.pheonix.org.hallaka.Activity.AddProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.pheonix.org.hallaka.Models.ProductDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Random;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PRODUCT_IMAGE_REQUEST_CODE = 2312;
    ImageView image;
    EditText name, price;
    Button submit;
    SpinKitView spinKitView;
    Spinner sp;
    String[] spinnerItems = new String[]{"Wax", "Spray", "Body Care", "Hair Care"};

    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        image = findViewById(R.id.productImageAddPro);
        name = findViewById(R.id.productNameAddPro);
        price = findViewById(R.id.productPriceAddPro);
        submit = findViewById(R.id.addProductId);
        spinKitView = findViewById(R.id.spin_kit);

        sp = findViewById(R.id.productTypeSp);

        submit.setOnClickListener(this);
        image.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductActivity.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerItems);

        sp.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            final String sName = name.getText().toString();
            final String sPrice = price.getText().toString();

            if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sPrice)) {
                spinKitView.setVisibility(View.VISIBLE);
                Funcs.disableCurrentScreen(getActivity());
                if (imgUri != null) {
                    final Random r = new Random();
                    final String pId = FirebaseAuth.getInstance().getUid() + r.nextInt(99999);

                    final StorageReference pic1Ref = FirebaseStorage.getInstance().getReference("products").child(pId + ".jpg");

                    pic1Ref.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                pic1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        ProductDataModel product = new ProductDataModel(sName, sPrice, uri.toString(), FirebaseAuth.getInstance().getUid(), pId, sp.getSelectedItem().toString());
                                        FirebaseDatabase.getInstance().getReference("products").child(pId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            else {
                                spinKitView.setVisibility(View.GONE);
                                Funcs.enableCurrentScreen(getActivity());
                            }

                        }
                    });

                } else {
                    showSnackBar("Add an image for product!");
                    spinKitView.setVisibility(View.GONE);
                    Funcs.enableCurrentScreen(getActivity());
                }
            }
        }

        if (image == v) {
            startActivityForResult(
                    new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    ), PRODUCT_IMAGE_REQUEST_CODE
            );
        }

    }

    private Activity getActivity() {
        return AddProductActivity.this;
    }
    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.addProductLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PRODUCT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri dataR = data.getData();

            if (dataR != null) {

                image.setImageURI(dataR);
                imgUri = dataR;

            }

        }
    }
}
