package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FirstFragment extends Fragment {

    RecyclerView recyclerView;
    ViewLobbyAdapter adapter;

    public FirstFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

//        getActivity().setTitle(Html.fromHtml("<font color=\"black\"> Lecturer List </font>"));
        getActivity().setTitle("Game Lobby");

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.FirstRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<LobbyModel> options =
                new FirebaseRecyclerOptions.Builder<LobbyModel>()
                        .setQuery( FirebaseDatabase.getInstance().getReference().child("User").child("Lecturer").orderByChild("name"), LobbyModel.class)
                        .build();

        adapter = new ViewLobbyAdapter(options);
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

        inflater.inflate(R.menu.bar_add_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.search);
        MenuItem itemAdd = menu.findItem(R.id.add);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint("Search name here");

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

        itemAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(getContext(), AddRoomActivity.class);
                getContext().startActivity(myIntent);

                return false;
            }
        });

    }

    private void textSearch(String str) {
        str = toTitleCase(str);

        FirebaseRecyclerOptions<LobbyModel> options =
                new FirebaseRecyclerOptions.Builder<LobbyModel>()
                        .setQuery( FirebaseDatabase.getInstance().getReference().child("User").child("Lecturer").orderByChild("name").startAt(str).endAt(str + "\uf8ff"), LobbyModel.class)
                        .build();

        adapter = new ViewLobbyAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public static String toTitleCase(String text) {
        boolean whiteSpace = true;

        StringBuilder builder = new StringBuilder(text);
        final int builderLength = builder.length();

        for (int i = 0; i < builderLength; ++i) {
            char c = builder.charAt(i);
            if (whiteSpace) {

                if (!Character.isWhitespace(c)) {

                    builder.setCharAt(i, Character.toTitleCase(c));
                    whiteSpace = false;
                }

            } else if (Character.isWhitespace(c)) {

                whiteSpace = true;

            } else {

                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }
}