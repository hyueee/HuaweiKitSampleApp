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

        String[] description1 = res.getStringArray(R.array.item1);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description1);
        String[] description2 = res.getStringArray(R.array.item2);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description2);
        String[] description3 = res.getStringArray(R.array.item3);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description3);

        autoCompleteTextView1.setText(arrayAdapter1.getItem(0), false);
        autoCompleteTextView1.setAdapter(arrayAdapter1);
        autoCompleteTextView2.setText(arrayAdapter2.getItem(0), false);
        autoCompleteTextView2.setAdapter(arrayAdapter2);
        autoCompleteTextView3.setText(arrayAdapter3.getItem(0), false);
        autoCompleteTextView3.setAdapter(arrayAdapter3);


    }
}
