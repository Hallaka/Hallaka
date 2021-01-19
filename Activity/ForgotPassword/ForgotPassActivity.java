package com.pheonix.org.hallaka.Activity.ForgotPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.pheonix.org.hallaka.Activity.Login.LoginActivity;
import com.pheonix.org.hallaka.R;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    Button sendPass, back;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailForgotPass);
        sendPass = findViewById(R.id.sendForgotPass);
        back = findViewById(R.id.backForgotPass);

        sendPass.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == sendPass) {

            String uEmail = email.getText().toString();
            if (!uEmail.isEmpty()) {
                auth.sendPasswordResetEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new AlertDialog.Builder(ForgotPassActivity.this).setTitle("Note").setMessage("Password reset link is sent to you on your email. Follow the instructions to Login.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setCancelable(false).show();
                        } else {
                            String m = task.getException().getMessage();
                            showSnackBar(m);
                        }
                    }
                });

            } else {
                showSnackBar("Please enter email first");
            }

        }

        if (v == back) {
            finish();
        }
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(R.id.forgotPassLay), s, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBlue))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .show();
    }
}