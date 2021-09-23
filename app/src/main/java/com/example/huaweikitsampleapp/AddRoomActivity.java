package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AddRoomActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2, autoCompleteTextView3;
    ImageView btnBack;
    Button btnAdd;
    CheckBox checkBox;
    TextInputLayout name, numPlayer, language, server, time, note;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        String gameId = getIntent().getStringExtra("gameId");
        String userId = getIntent().getStringExtra("userId");

        autoCompleteTextView1 = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView3 = findViewById(R.id.autoCompleteTextView3);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        checkBox = findViewById(R.id.checkBox);
        name = findViewById(R.id.name);
        numPlayer = findViewById(R.id.numPlayer);
        language = findViewById(R.id.language);
        server = findViewById(R.id.server);
        time = findViewById(R.id.time);
        note = findViewById(R.id.note);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddRoomActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("New Room")
                        .setCancelable(false)
                        .setMessage("Please ensure your details are correct.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String roomName = name.getEditText().getText().toString();
                                String roomPlayer = numPlayer.getEditText().getText().toString();
                                String roomLanguage = language.getEditText().getText().toString();
                                String roomServer = server.getEditText().getText().toString();
                                String roomTime = time.getEditText().getText().toString();
                                String roomNote = note.getEditText().getText().toString();
                                String description1 = autoCompleteTextView1.getText().toString();
                                String description2 = autoCompleteTextView2.getText().toString();
                                String description3 = autoCompleteTextView3.getText().toString();

                                if (TextUtils.isEmpty(roomName)) {
                                    name.setError("required.");
                                } else {
                                    name.setError(null);
                                }

                                if (TextUtils.isEmpty(roomPlayer)) {
                                    numPlayer.setError("required.");
                                } else {
                                    numPlayer.setError(null);
                                }

                                if (TextUtils.isEmpty(roomLanguage)) {
                                    language.setError("required.");
                                } else {
                                    language.setError(null);
                                }

                                if (TextUtils.isEmpty(roomServer)) {
                                    server.setError("required.");
                                } else {
                                    server.setError(null);
                                }

                                if (TextUtils.isEmpty(roomTime)) {
                                    time.setError("required.");
                                } else {
                                    time.setError(null);
                                }

                                String description = "";

                                if (TextUtils.isEmpty(description1)) {
                                    autoCompleteTextView1.setError("required.");
                                } else {
                                    autoCompleteTextView1.setError(null);
                                    description = description1 + " , " + description2 + " , " + description3;

                                }

                                DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                String current = now.format(dtfDate);

                                long dateMillis = 0;

                                //convert date to milliseconds
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
                                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    Date date = sdf.parse(current);
                                    dateMillis = date.getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Map<String, Object> newRoom = new HashMap<>();

                                if (!TextUtils.isEmpty(roomName) && !TextUtils.isEmpty(roomPlayer) && !TextUtils.isEmpty(roomLanguage) && !TextUtils.isEmpty(roomServer) && !TextUtils.isEmpty(roomTime) && !TextUtils.isEmpty(description)) {
                                    if (checkBox.isChecked()) {
                                        newRoom.put("name", roomName);
                                        newRoom.put("id", "none");
                                        newRoom.put("numPlayer", roomPlayer);
                                        newRoom.put("currentPlayer", "1");
                                        newRoom.put("language", roomLanguage);
                                        newRoom.put("server", roomServer);
                                        newRoom.put("time", roomTime);
                                        newRoom.put("createDate", dateMillis);
                                        newRoom.put("note", roomNote);
                                        newRoom.put("requestToJoin", "Yes");
                                        newRoom.put("description", description);
                                    } else {
                                        newRoom.put("name", roomName);
                                        newRoom.put("id", "none");
                                        newRoom.put("numPlayer", roomPlayer);
                                        newRoom.put("currentPlayer", "1");
                                        newRoom.put("language", roomLanguage);
                                        newRoom.put("server", roomServer);
                                        newRoom.put("time", roomTime);
                                        newRoom.put("createDate", dateMillis);
                                        newRoom.put("note", roomNote);
                                        newRoom.put("requestToJoin", "No");
                                        newRoom.put("description", description);
                                    }

                                    addNewRoom(newRoom, gameId, userId);
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }

    private void addNewRoom(Map<String, Object> newRoom, String gameId, String userId) {
        String id = getAlphaNumericString(5);

        newRoom.replace("id", id);

        myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    addNewRoom(newRoom, gameId, userId);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(id)
                            .setValue(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(id)
                                    .setValue(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    new AlertDialog.Builder(AddRoomActivity.this)
                                            .setIcon(R.drawable.ic_warning)
                                            .setTitle("Thank you.")
                                            .setMessage("You have successfully created new room.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            }).show();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // function to generate a random string of length n
    static String getAlphaNumericString(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
