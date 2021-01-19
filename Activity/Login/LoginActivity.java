package com.pheonix.org.hallaka.Activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pheonix.org.hallaka.Activity.Barber.BarberActivity;
import com.pheonix.org.hallaka.Activity.ForgotPassword.ForgotPassActivity;
import com.pheonix.org.hallaka.Activity.Home.HomeActivity;
import com.pheonix.org.hallaka.Activity.SignUp.SignupActivity;
import com.pheonix.org.hallaka.R;
import com.pheonix.org.hallaka.Utils.Funcs;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    EditText userName;
    TextInputLayout pass;
    Button signInBtn;
    CheckBox rememberPass;
    Button newUser, forgotPass;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userNameSignIn);
        pass = findViewById(R.id.passwordSignIn);
        signInBtn = findViewById(R.id.signInBtnSignIn);
        rememberPass = findViewById(R.id.rememberPassIdSignIn);
        newUser = findViewById(R.id.newUserIdSignIn);
        forgotPass = findViewById(R.id.forgotPassIdSignIn);
        spinKitView = findViewById(R.id.spin_kit);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        newUser.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        rememberPass.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == signInBtn) {

            Login();

        }

        if (v == newUser) {

            startActivity(new Intent(this, SignupActivity.class));
        }

        if (v == forgotPass) {

            startActivity(new Intent(this, ForgotPassActivity.class));
        }
        if (v == rememberPass) {


        }
    }

    public void Login() {
        final String uEmail = userName.getText().toString();
        String uPassword = pass.getEditText().getText().toString();

        if (!TextUtils.isEmpty(uEmail) && !TextUtils.isEmpty(uPassword)) {

            spinKitView.setVisibility(View.VISIBLE);
            Funcs.disableCurrentScreen(getActivity());
            if (uEmail.equalsIgnoreCase("admin@hallaka.com")) {

                if (uPassword.equals("1234567890")) {

                    showSnackBar("Logged in as Admin!");
                    spinKitView.setVisibility(View.GONE);
                    Funcs.enableCurrentScreen(getActivity());
                } else {
                    showSnackBar("Admin Password not right!");
                    spinKitView.setVisibility(View.GONE);
                    Funcs.enableCurrentScreen(getActivity());
                }

            } else {

                auth.signInWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseDatabase.getInstance().getReference("users").child("profiles").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        spinKitView.setVisibility(View.GONE);
                                        Funcs.enableCurrentScreen(getActivity());
                                        String type = snapshot.child("type").getValue(String.class);

                                        if (type.equalsIgnoreCase("Customer")) {
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.putExtra("data", uEmail);
                                            finish();
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, BarberActivity.class);
                                            intent.putExtra("data", uEmail);
                                            finish();
                                            startActivity(intent);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            spinKitView.setVisibility(View.INVISIBLE);
                            Funcs.enableCurrentScreen(getActivity());
                            showSnackBar(task.getException().getMessage());
                        }

                    }
                });
            }

        } else {
            showSnackBar("Enter required values in above fields");
            spinKitView.setVisibility(View.GONE);
            Funcs.enableCurrentScreen(getActivity());
        }

    }

    private Activity getActivity() {
        return LoginActivity.this;
    }



    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.loginLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}