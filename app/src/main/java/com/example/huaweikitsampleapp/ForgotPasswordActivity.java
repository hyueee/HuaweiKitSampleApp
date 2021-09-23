package com.example.huaweikitsampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btn_f_reset;
    private EditText et_f_email;
    private ImageView f_banner;
    private ProgressBar f_progressbar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_f_email= (EditText) findViewById((R.id.et_f_email));
        btn_f_reset= (Button) findViewById((R.id.btn_f_reset));
        f_progressbar= (ProgressBar) findViewById((R.id.f_progressbar));

        auth = FirebaseAuth.getInstance();

        btn_f_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }
    private void resetPassword(){
        String email = et_f_email.getText().toString().trim();

        if(email.isEmpty()){
            et_f_email.setError("Email is Required");
            et_f_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_f_email.setError("Please provide valid email");
            et_f_email.requestFocus();
            return;
        }
        f_progressbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    et_f_email.setText("");
                    Intent tomain = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                    startActivity(tomain);
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Error! Email doest exist or wrong email", Toast.LENGTH_LONG).show();
                    f_progressbar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}