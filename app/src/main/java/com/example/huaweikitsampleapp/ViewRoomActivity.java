package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewRoomPlayerAdapter adapter;
    DatabaseReference myRef;
    ArrayList<User> arrayList = new ArrayList<>();
    String owner;
    Button btnLeave;
    Task<Void> myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);

        String userId = getIntent().getStringExtra("userId");
        String gameId = getIntent().getStringExtra("gameId");
        String roomId = getIntent().getStringExtra("roomId");
        String roomName = getIntent().getStringExtra("roomName");

        Query query= FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser").orderByKey().limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    owner = ss.getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setTitle("Room Player for " + roomName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));

        btnLeave = findViewById(R.id.btnLeave);
        recyclerView = findViewById(R.id.RoomUserRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ss : snapshot.getChildren()) {
                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            arrayList.add(snapshot.getValue(User.class));
                            adapter = new ViewRoomPlayerAdapter(arrayList, userId, gameId, roomId, owner);
                            recyclerView.setAdapter(adapter);
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

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRoomActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Leave Room?")
                        .setMessage("Are you confirm to leave this room? \nIf you are the room owner, please proceed to 'My Rooms' to delete the room if you plan to leave.")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (owner.equals(userId)) {
                                    new AlertDialog.Builder(ViewRoomActivity.this)
                                            .setIcon(R.drawable.ic_warning)
                                            .setTitle("Leave Room Failed")
                                            .setMessage("Since you are the room owner, please proceed to 'My Rooms' to delete the room if you plan to leave.")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                } else {
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(roomId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });

                                    Query query = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser").orderByValue().equalTo(userId);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot ss : snapshot.getChildren()) {
                                                    String key = ss.getKey();

                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser").child(key);
                                                    myTask = myRef.removeValue();
                                                    myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId).child(roomId);
                                                            myTask = myRef.removeValue();
                                                            myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(owner).child("room").child(gameId).child(roomId);
                                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            int player = Integer.parseInt(snapshot.child("currentPlayer").getValue().toString());
                                                                            player -= 1;

                                                                            Map<String, Object> updatePlayer = new HashMap<>();
                                                                            updatePlayer.put("currentPlayer", Integer.toString(player));

                                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(owner).child("room").child(gameId).child(roomId)
                                                                                    .updateChildren(updatePlayer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId).updateChildren(updatePlayer)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    new AlertDialog.Builder(v.getContext())
                                                                                                            .setIcon(R.drawable.ic_warning)
                                                                                                            .setTitle("Leave Room Success")
                                                                                                            .setMessage("You have left this room successfully.")
                                                                                                            .setCancelable(false)
                                                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                    Intent myIntent = new Intent(ViewRoomActivity.this, GameLobbyActivity.class);
                                                                                                                    myIntent.putExtra("gameId", gameId);
                                                                                                                    myIntent.putExtra("userId", userId);
                                                                                                                    myIntent.putExtra("frag", "second");
                                                                                                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                                    startActivity(myIntent);
                                                                                                                    finish();
                                                                                                                }
                                                                                                            }).show();
                                                                                                }
                                                                                            });

                                                                                }
                                                                            });

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_navigation, menu);

        menu.clear();
        return true;
    }

}
