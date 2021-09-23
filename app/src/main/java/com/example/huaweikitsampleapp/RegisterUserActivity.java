package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_r_name, et_r_email, et_r_password;
    private TextView btn_r_register;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        btn_r_register = (Button) findViewById(R.id.btn_r_register);
        btn_r_register.setOnClickListener(this);

        et_r_name = (EditText) findViewById(R.id.et_r_name);
        et_r_email = (EditText) findViewById(R.id.et_r_email);
        et_r_password = (EditText) findViewById(R.id.et_r_password);

        progressbar=(ProgressBar) findViewById(R.id.progressbar);
        setTitle("Register Page");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_r_register:
                registerUser();
                break;
        }

    }
    private void registerUser(){
        String name=et_r_name.getText().toString().trim();
        String email=et_r_email.getText().toString().trim();
        String password=et_r_password.getText().toString().trim();

        if(name.isEmpty()){
            et_r_name.setError("Username is required");
            et_r_name.requestFocus();
            return;
        }
        if(email.isEmpty()){
            et_r_email.setError("Email is required");
            et_r_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_r_email.setError("Please provide valid email!");
            et_r_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_r_password.setError("Password is required");
            et_r_password.requestFocus();
            return;
        }
        if(password.length()<6){
            et_r_password.setError("Password Should Be 6 Character ");
            et_r_password.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user= new User(name,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUserActivity.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                        et_r_email.setText("");
                                        et_r_password.setText("");
                                        et_r_name.setText("");
                                        Intent tomain = new Intent(RegisterUserActivity.this,MainActivity.class);
                                        startActivity(tomain);
                                        progressbar.setVisibility(View.VISIBLE);
                                    }else{
                                        Toast.makeText(RegisterUserActivity.this,"Failed register! Try Again",Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);

                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterUserActivity.this,"Failed register, email already exist",Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });




    }
}