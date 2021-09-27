package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class SecondFragment extends Fragment {
    RecyclerView recyclerView;
    ViewChatRoomAdapter adapter;
    String gameId, userId, gameName;
    DatabaseReference myRef;

    public SecondFragment(String id, String userId) {
        this.gameId = id;
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        if (gameId.equals("csgo")) {
            gameName = "Counter-Strike: Global Offensive";
        } else if (gameId.equals("valorant")) {
            gameName = "Valorant";
        } else if (gameId.equals("dbd")) {
            gameName = "Dead by Daylight";
        } else if (gameId.equals("l4d")) {
            gameName = "Left 4 Dead";
        } else if (gameId.equals("dragonest")) {
            gameName = "Dragon Nest";
        } else if (gameId.equals("genshin")) {
            gameName = "Genshin Impact";
        } else if (gameId.equals("gta5")) {
            gameName = "Grand Theft Auto V";
        } else if (gameId.equals("maple")) {
            gameName = "Maple Story";
        }

//        getActivity().setTitle(Html.fromHtml("<font color=\"black\"> Lecturer List </font>"));
        getActivity().setTitle("Chat Rooms - " + gameName);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.SecondRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery( FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId).orderByChild("createDate"), RoomModel.class)
                        .build();

        adapter = new ViewChatRoomAdapter(options, gameId, userId);
        recyclerView.setAdapter(adapter);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(gameId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ss : snapshot.getChildren()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(ss.child("id").getValue().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                }
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }


//    public static String toTitleCase(String text) {
//        boolean whiteSpace = true;
//
//        StringBuilder builder = new StringBuilder(text);
//        final int builderLength = builder.length();
//
//        for (int i = 0; i < builderLength; ++i) {
//            char c = builder.charAt(i);
//            if (whiteSpace) {
//
//                if (!Character.isWhitespace(c)) {
//
//                    builder.setCharAt(i, Character.toTitleCase(c));
//                    whiteSpace = false;
//                }
//
//            } else if (Character.isWhitespace(c)) {
//
//                whiteSpace = true;
//
//            } else {
//
//                builder.setCharAt(i, Character.toLowerCase(c));
//            }
//        }
//
//        return builder.toString();
//    }
}