package com.example.huaweikitsampleapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class JoinRoomApprovalActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewApprovalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_approval);

        setTitle("Approval to join others' room");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));

        String userId = getIntent().getStringExtra("userId");
        String gameId = getIntent().getStringExtra("gameId");

        recyclerView = findViewById(R.id.approvalRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("approvalRoom").child(gameId), RoomModel.class)
                        .build();

        adapter = new ViewApprovalAdapter(options, userId, gameId);
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
        return true;
    }

}
