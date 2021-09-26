package com.example.huaweikitsampleapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ViewRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewRoomPlayerAdapter adapter;
    DatabaseReference myRef;
    ArrayList<UserModel> arrayList = new ArrayList<>();
    String owner;

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

        recyclerView = findViewById(R.id.RoomUserRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.add(snapshot.getValue(UserModel.class));
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
