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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ViewRoomDetailsActivity extends AppCompatActivity {
    TextInputLayout name, numPlayer, language, server, time, note;
    Button btnJoin, btnRequestJoin;
    TextView description1, description2, description3, ownRoom, chatRoom, roomFull, approvalRoom;
    ImageView btnBack;
    DatabaseReference myRef;
    String roomOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_details);

        String roomId = getIntent().getStringExtra("id");
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
        chatRoom = findViewById(R.id.chatRoom);
        roomFull = findViewById(R.id.roomFull);
        approvalRoom =  findViewById(R.id.approvalRoom);

        Map<String, Object> joinedRoom = new HashMap<>();
        Map<String, Object> requestJoinRoomForOwner = new HashMap<>();
        Map<String, Object> requestJoinRoom = new HashMap<>();

        myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId);
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
                    roomOwner = snapshot.child("owner").getValue().toString();

                    String description = snapshot.child("description").getValue().toString();
                    String[] splitDesc = description.split(" , ");

                    joinedRoom.put("name", roomName);
                    joinedRoom.put("server", roomServer);
                    joinedRoom.put("language", roomLanguage);
                    joinedRoom.put("time", roomTime);
                    joinedRoom.put("id", roomId);

                    requestJoinRoomForOwner.put("name", roomName);
                    requestJoinRoomForOwner.put("server", roomServer);
                    requestJoinRoomForOwner.put("time", roomTime);
                    requestJoinRoomForOwner.put("language", roomLanguage);
                    requestJoinRoomForOwner.put("id", roomId);
                    requestJoinRoomForOwner.put("requestUser", userId);

                    requestJoinRoom.put("id", roomId);
                    requestJoinRoom.put("name", roomName);
                    requestJoinRoom.put("requestDate", "None");
                    requestJoinRoom.put("status", "Requesting");

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

                    if (Integer.parseInt(roomCurrentPlayer) == Integer.parseInt(roomNumPlayer)) {
                        roomFull.setVisibility(View.VISIBLE);
                    }

                    if (roomOwner.equals(userId)) {
                        ownRoom.setVisibility(View.VISIBLE);

                    } else {
                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId).child(roomId);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    chatRoom.setVisibility(View.VISIBLE);
                                    myRef.removeEventListener(this);
                                } else {
                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("approvalRoom").child(gameId).child(roomId);
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                approvalRoom.setVisibility(View.VISIBLE);
                                            } else if (roomRequestToJoin.equals("Yes")) {
                                                btnRequestJoin.setVisibility(View.VISIBLE);

                                            } else {
                                                btnJoin.setVisibility(View.VISIBLE);
                                            }
                                            myRef.removeEventListener(this);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                myRef.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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

                                myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int num = (int) snapshot.getChildrenCount();
                                        num += 1;

                                        String addPlayer = Integer.toString(num);
                                        Map<String, Object> updateUser = new HashMap<>();
                                        updateUser.put(addPlayer, userId);

                                        myRef.removeEventListener(this);

                                        FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser")
                                                .updateChildren(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Map<String, Object> updateRoom = new HashMap<>();
                                                updateRoom.put("currentPlayer", addPlayer);

                                                FirebaseDatabase.getInstance().getReference().child("Users").child(roomOwner).child("room").child(gameId).child(roomId)
                                                        .updateChildren(updateRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId)
                                                                        .updateChildren(updateRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId).child(roomId)
                                                                                .setValue(joinedRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                new AlertDialog.Builder(ViewRoomDetailsActivity.this)
                                                                                        .setIcon(R.drawable.ic_check)
                                                                                        .setTitle("Thank you.")
                                                                                        .setCancelable(false)
                                                                                        .setMessage("You have successfully joined this room.")
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
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                                requestJoin(gameId, roomId, userId, requestJoinRoomForOwner, requestJoinRoom);
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

    private void requestJoin(String gameId, String roomId, String userId, Map<String, Object> requestJoinRoomForOwner, Map<String, Object> requestJoinRoom) {
        myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("requestUser");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> user = new HashMap<>();
                user.put(userId, "1");

                FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("requestUser")
                        .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(roomOwner).child("requestNum");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int number;
                                if (snapshot.exists()) {
                                    String num = snapshot.getValue().toString();
                                    number = Integer.parseInt(num);
                                    number += 1;

                                    myRef.removeEventListener(this);
                                } else {
                                    number = 1;
                                }

                                DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                String current = now.format(dtfDate);

                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                                String current2 = dateFormat.format(new Date());

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

                                String dateRequest = Long.toString(dateMillis);

                                FirebaseDatabase.getInstance().getReference().child("Users").child(roomOwner).child("requestNum").setValue(Integer.toString(number))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(roomOwner).child("requestJoinRoom").child(gameId).child(dateRequest)
                                                        .setValue(requestJoinRoomForOwner).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        requestJoinRoom.replace("requestDate", current2);

                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("approvalRoom").child(gameId).child(roomId)
                                                                .setValue(requestJoinRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                new AlertDialog.Builder(ViewRoomDetailsActivity.this)
                                                                        .setIcon(R.drawable.ic_check)
                                                                        .setTitle("Thank you.")
                                                                        .setMessage("You have successfully request to join this room. Please wait for approval from room admin.")
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
