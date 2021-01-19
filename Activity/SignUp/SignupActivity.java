package com.pheonix.org.hallaka.Activity.SignUp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pheonix.org.hallaka.Activity.Login.LoginActivity;
import com.pheonix.org.hallaka.Models.UserDataModel;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PROFILE_IMAGE_REQUEST_CODE = 1923;
    FirebaseAuth auth;
    EditText userName, email, phoneNumber, address;
    TextInputLayout password;
    RadioGroup radioGroup;
    Button signUpBtn;
    TextView alreadyHaveAccount;
    SpinKitView spinKitView;
    String uName, uEmail, uPhone, uAddress, uPass, type;
    ImageView imageView;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imageView = findViewById(R.id.logoImageSignUp);
        userName = findViewById(R.id.userNameSignUp);
        email = findViewById(R.id.emailSignUp);
        phoneNumber = findViewById(R.id.numberSignUp);
        address = findViewById(R.id.addressSignUp);
        password = findViewById(R.id.passwordSignUp);
        signUpBtn = findViewById(R.id.signUpBtnSignUp);
        alreadyHaveAccount = findViewById(R.id.haveAccountSignUp);
        spinKitView = findViewById(R.id.spin_kit);
        radioGroup = findViewById(R.id.typeLaySignUp);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        imageView.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        alreadyHaveAccount.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == signUpBtn) {
            uName = userName.getText().toString();
            uEmail = email.getText().toString();
            uPhone = phoneNumber.getText().toString();
            uAddress = address.getText().toString();
            uPass = password.getEditText().getText().toString();
            type = getSelectedRadioButtonText();

            if (!uName.isEmpty()) {
                if (!uEmail.isEmpty()) {
                    if (isEmailValid(uEmail)) {
                        if (uPhone.length() == 11) {
                            if (!uAddress.isEmpty()) {
                                if (uPass.length() >= 6) {
                                    progressVisible();
                                    auth.createUserWithEmailAndPassword(uEmail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                final String uid = auth.getUid();
                                                if (imgUri == null) {
                                                    UserDataModel sd = new UserDataModel(uName, uPass, uEmail, uPhone, uAddress, uid, type, "null");
                                                    FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(uid)).setValue(sd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                auth.signOut();
                                                                showSnackBar("Account created!");
                                                                finish();
                                                            } else {
                                                                showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    final StorageReference pic1Ref = FirebaseStorage.getInstance().getReference("profiles").child(FirebaseAuth.getInstance() + ".jpg");

                                                    pic1Ref.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                pic1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        UserDataModel sd = new UserDataModel(uName, uPass, uEmail, uPhone, uAddress, uid, type, uri.toString());
                                                                        FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(uid)).setValue(sd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    auth.signOut();
                                                                                    showSnackBar("Account created!");
                                                                                    finish();
                                                                                } else {
                                                                                    showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                                                                    progressGone();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            } else {
                                                                showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                                                progressGone();
                                                            }
                                                        }
                                                    });

                                                }
                                            } else {
                                                showSnackBar(Objects.requireNonNull(task.getException()).getMessage());
                                                progressGone();
                                            }
                                        }
                                    });
                                } else {
                                    showSnackBar("Password cannot be smaller than six digits");
                                    progressGone();
                                }
                            } else {
                                showSnackBar("Address cannot be empty");
                                progressGone();
                            }
                        } else {
                            showSnackBar("Phone Number format not right");
                            progressGone();
                        }

                    } else {
                        showSnackBar("Email format is not right");
                        progressGone();
                    }
                } else {
                    showSnackBar("Email cannot be empty");
                    progressGone();
                }
            } else {
                showSnackBar("Name cannot be empty");
                progressGone();
            }

        }
        if (v == alreadyHaveAccount) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == imageView) {
            startActivityForResult(
                    new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    ), PROFILE_IMAGE_REQUEST_CODE
            );
        }
    }

    private void progressGone() {
        spinKitView.setVisibility(View.GONE);
        Funcs.enableCurrentScreen(getActivity());
    }

    private Activity getActivity() {
        return SignupActivity.this;
    }

    private void progressVisible() {
        spinKitView.setVisibility(View.VISIBLE);
        Funcs.disableCurrentScreen(getActivity());
    }

    private String getSelectedRadioButtonText() {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(checkedId);
        return radioButton.getText().toString();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.signUpLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri dataR = data.getData();

            if (dataR != null) {

                imageView.setImageURI(dataR);
                imgUri = dataR;

            }

        }
    }
}