package com.example.huaweikitsampleapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;

public class SecondActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;
    ViewGameAdapter adapter;
    String userId;

    AGConnectAppMessaging appMessaging = AGConnectAppMessaging.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        appMessaging.trigger("custom_message");
        appMessaging.setFetchMessageEnable(true);
        appMessaging.setDisplayEnable(true);
        appMessaging.setForceFetch();

        recyclerView = findViewById(R.id.gameRecycle);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);

        collapsingToolbarLayout.setTitle("Games");
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            userId = getIntent().getStringExtra("userId");
        } else {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        FirebaseRecyclerOptions<GameModel> options =
                new FirebaseRecyclerOptions.Builder<GameModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("game"), GameModel.class)
                        .build();

        adapter = new ViewGameAdapter(options, userId);
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
}
