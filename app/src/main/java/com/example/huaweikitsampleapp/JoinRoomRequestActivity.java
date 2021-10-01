package com.example.huaweikitsampleapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class JoinRoomRequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewRequestAdapter adapter;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_request);

        setTitle("Request to join your room");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));

        String userId = getIntent().getStringExtra("userId");
        String gameId = getIntent().getStringExtra("gameId");

        text =  findViewById(R.id.text);
        recyclerView = findViewById(R.id.approvalRecycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId), RoomModel.class)
                        .build();

        adapter = new ViewRequestAdapter(options, userId, gameId, text, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_navigation, menu);

        menu.clear();
        return true;
    }

}
