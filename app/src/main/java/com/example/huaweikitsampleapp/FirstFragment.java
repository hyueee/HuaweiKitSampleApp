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

public class FirstFragment extends Fragment {
    RecyclerView recyclerView;
    ViewLobbyAdapter adapter;
    String gameId, userId, gameName;

    public FirstFragment(String id, String userId) {
        this.gameId = id;
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

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
        getActivity().setTitle("Game Lobby - " + gameName);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.FirstRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery( FirebaseDatabase.getInstance().getReference().child("room").child(gameId).orderByChild("createDate"), RoomModel.class)
                        .build();

        adapter = new ViewLobbyAdapter(options, gameId, userId);
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

        inflater.inflate(R.menu.bar_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint("Search room id here");

//        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
//        searchIcon.setColorFilter(Color.BLACK);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                textSearch(query);
                return true;
            }

        });
    }

    private void textSearch(String str) {
        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery( FirebaseDatabase.getInstance().getReference().child("room").child(gameId).orderByChild("id").startAt(str).endAt(str + "\uf8ff"), RoomModel.class)
                        .build();

        adapter = new ViewLobbyAdapter(options, gameId, userId);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

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