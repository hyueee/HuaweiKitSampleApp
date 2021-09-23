package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRoomDetailsActivity extends AppCompatActivity {
    TextInputLayout name, numPlayer, language, server, time, note;
    Button btnJoin, btnRequestJoin;
    TextView description1, description2, description3, ownRoom;
    ImageView btnBack;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_details);

        String id = getIntent().getStringExtra("id");
        String gameId = getIntent().getStringExtra("gameId");
        String userId = getIntent().getStringExtra("userId");

        btnBack = findViewById(R.id.btnBack);
        btnJoin = findViewById(R.id.btnJoin);
        btnRequestJoin = findViewById(R.id.btnRequestJoin);
        name = findViewById(R.id.name);
        numPlayer = findViewById(R.id.numPlayer);
        language = findViewById(R.id.language);
        server = findViewById(R.id.server);
        time = findViewById(R.id.time);
        note = findViewById(R.id.note);
        description1 = findViewById(R.id.description);
        description2 = findViewById(R.id.description2);
        description3 = findViewById(R.id.description3);
        ownRoom = findViewById(R.id.ownRoom);

        myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String roomName = snapshot.child("name").getValue().toString();
                    String roomCurrentPlayer = snapshot.child("currentPlayer").getValue().toString();
                    String roomNumPlayer = snapshot.child("numPlayer").getValue().toString();
                    String roomLanguage = snapshot.child("language").getValue().toString();
                    String roomServer = snapshot.child("server").getValue().toString();
                    String roomTime = snapshot.child("time").getValue().toString();
                    String roomNote = snapshot.child("note").getValue().toString();
                    String roomRequestToJoin = snapshot.child("requestToJoin").getValue().toString();

                    String description = snapshot.child("description").getValue().toString();
                    String[] splitDesc = description.split(" , ");

                    name.getEditText().setText(roomName);
                    numPlayer.getEditText().setText(roomCurrentPlayer + "/" + roomNumPlayer);
                    language.getEditText().setText(roomLanguage);
                    server.getEditText().setText(roomServer);
                    time.getEditText().setText(roomTime);


                    description1.setText("- " + splitDesc[0]);

                    if (roomNote.equals("")) {
                        note.getEditText().setText("None");
                    } else {
                        note.getEditText().setText(roomNote);
                    }

                    if (splitDesc[1].equals("-") && splitDesc[2].equals("-")) {
                        description2.setVisibility(View.GONE);
                        description3.setVisibility(View.GONE);

                    } else if (!splitDesc[1].equals("-") && splitDesc[2].equals("-")){
                        description2.setVisibility(View.VISIBLE);
                        description2.setText("- " + splitDesc[1]);
                        description3.setVisibility(View.GONE);

                    } else if (splitDesc[1].equals("-") && !splitDesc[2].equals("-")) {
                        description2.setVisibility(View.VISIBLE);
                        description2.setText("- " + splitDesc[2]);
                        description3.setVisibility(View.GONE);

                    } else {
                        description2.setVisibility(View.VISIBLE);
                        description2.setText("- " + splitDesc[1]);
                        description3.setVisibility(View.VISIBLE);
                        description3.setText("- " + splitDesc[2]);
                    }

                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(id);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ownRoom.setVisibility(View.VISIBLE);
                                btnRequestJoin.setVisibility(View.GONE);
                                btnJoin.setVisibility(View.GONE);

                            } else if (roomRequestToJoin.equals("Yes")) {
                                btnRequestJoin.setVisibility(View.VISIBLE);
                                btnJoin.setVisibility(View.GONE);

                            } else {
                                btnRequestJoin.setVisibility(View.GONE);
                                btnJoin.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRoomDetailsActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Join Room")
                        .setMessage("Are you confirm to join this room?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                TODO: add into user
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        btnRequestJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRoomDetailsActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Join Room")
                        .setMessage("Are you confirm to join this room? \n(*Require approval from room admin)")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                                TODO: add into user
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
}
