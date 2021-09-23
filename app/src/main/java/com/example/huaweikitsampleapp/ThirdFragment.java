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

import androidx.appcompat.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdFragment extends Fragment {
    RecyclerView recyclerView;
    ViewOwnRoomsAdapter adapter;
    String gameId, userId, gameName;

    public ThirdFragment(String id, String userId) {
        this.gameId = id;
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

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

        getActivity().setTitle("My Rooms - " + gameName);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.ThirdRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").orderByChild("createDate"), RoomModel.class)
                        .build();

        adapter = new ViewOwnRoomsAdapter(options, gameId, userId);
        recyclerView.setAdapter(adapter);

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

}