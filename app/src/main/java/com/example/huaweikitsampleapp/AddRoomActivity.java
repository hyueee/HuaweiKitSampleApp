package com.example.huaweikitsampleapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddRoomActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2, autoCompleteTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        autoCompleteTextView1 = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView3 = findViewById(R.id.autoCompleteTextView3);
        Resources res = getResources();
        String[] user = res.getStringArray(R.array.description);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_text, user);

        autoCompleteTextView1.setText(arrayAdapter.getItem(0), false);
        autoCompleteTextView1.setAdapter(arrayAdapter);
        autoCompleteTextView2.setText(arrayAdapter.getItem(0), false);
        autoCompleteTextView2.setAdapter(arrayAdapter);
        autoCompleteTextView3.setText(arrayAdapter.getItem(0), false);
        autoCompleteTextView3.setAdapter(arrayAdapter);


    }
}
