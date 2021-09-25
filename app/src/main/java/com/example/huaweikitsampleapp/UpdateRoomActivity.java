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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class UpdateRoomActivity extends AppCompatActivity {
    DatabaseReference myRef;
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2, autoCompleteTextView3;
    ImageView btnBack;
    Button btnAdd;
    CheckBox checkBox;
    TextInputLayout name, numPlayer, language, server, time, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        String roomId = getIntent().getStringExtra("roomId");
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

        btnAdd.setText("Update");

        Resources res = getResources();
        String[] description1 = res.getStringArray(R.array.item1);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description1);
        String[] description2 = res.getStringArray(R.array.item2);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description2);
        String[] description3 = res.getStringArray(R.array.item3);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, R.layout.dropdown_text, description3);

        myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String roomName = snapshot.child("name").getValue().toString();
                    name.getEditText().setText(roomName);
                    String roomPlayer = snapshot.child("numPlayer").getValue().toString();
                    numPlayer.getEditText().setText(roomPlayer);
                    String roomLanguage = snapshot.child("language").getValue().toString();
                    language.getEditText().setText(roomLanguage);
                    String roomServer = snapshot.child("server").getValue().toString();
                    server.getEditText().setText(roomServer);
                    String roomTime = snapshot.child("time").getValue().toString();
                    time.getEditText().setText(roomTime);
                    String roomNote = snapshot.child("note").getValue().toString();
                    note.getEditText().setText(roomNote);

                    String[] description = snapshot.child("description").getValue().toString().split(" , ");

                    autoCompleteTextView1.setText(description[0], false);
                    autoCompleteTextView1.setAdapter(arrayAdapter1);
                    autoCompleteTextView2.setText(description[1], false);
                    autoCompleteTextView2.setAdapter(arrayAdapter2);
                    autoCompleteTextView3.setText(description[2], false);
                    autoCompleteTextView3.setAdapter(arrayAdapter3);

                    myRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateRoomActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Update Room")
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
                                        newRoom.put("id", roomId);
                                        newRoom.put("numPlayer", roomPlayer);
                                        newRoom.put("language", roomLanguage);
                                        newRoom.put("server", roomServer);
                                        newRoom.put("time", roomTime);
                                        newRoom.put("createDate", dateMillis);
                                        newRoom.put("note", roomNote);
                                        newRoom.put("requestToJoin", "Yes");
                                        newRoom.put("description", description);
                                    } else {
                                        newRoom.put("name", roomName);
                                        newRoom.put("id", roomId);
                                        newRoom.put("numPlayer", roomPlayer);
                                        newRoom.put("language", roomLanguage);
                                        newRoom.put("server", roomServer);
                                        newRoom.put("time", roomTime);
                                        newRoom.put("createDate", dateMillis);
                                        newRoom.put("note", roomNote);
                                        newRoom.put("requestToJoin", "No");
                                        newRoom.put("description", description);
                                    }

                                    Map<String, Object> updateChatRoom = new HashMap<>();
                                    updateChatRoom.put("name", roomName);
                                    updateChatRoom.put("server", roomServer);
                                    updateChatRoom.put("language", roomLanguage);
                                    updateChatRoom.put("time", roomTime);
                                    updateChatRoom.put("id", roomId);

                                    updateRoom(newRoom, gameId, userId, roomId, updateChatRoom);
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

    private void updateRoom(Map<String, Object> newRoom, String gameId, String userId, String roomId, Map<String, Object> updateChatRoom) {
        FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId)
                .updateChildren(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId).child(roomId)
                        .updateChildren(updateChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(roomId)
                                .updateChildren(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                new AlertDialog.Builder(UpdateRoomActivity.this)
                                        .setIcon(R.drawable.ic_check)
                                        .setTitle("Thank you.")
                                        .setMessage("You have successfully updated this room.")
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
        });
    }
}
