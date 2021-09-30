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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdFragment extends Fragment {
    RecyclerView recyclerView;
    ViewOwnRoomsAdapter adapter;
    String gameId, userId, gameName;
    FloatingActionButton fabMain, fab1, fab2, fab3;
    boolean clickFab = true;
    Animation animation1, animation2, animation3, animation4;

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

        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.open_anim);
        animation4 = AnimationUtils.loadAnimation(getContext(), R.anim.close_anim);

        fabMain = view.findViewById(R.id.fabMain);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        fab3 = view.findViewById(R.id.fab3);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabVisibility(clickFab);
                fabAnimation(clickFab);
                fabClickable(clickFab);
                clickFab = !clickFab;
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), AddRoomActivity.class);
                myIntent.putExtra("gameId", gameId);
                myIntent.putExtra("userId", userId);
                v.getContext().startActivity(myIntent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), JoinRoomApprovalActivity.class);
                myIntent.putExtra("userId", userId);
                myIntent.putExtra("gameId", gameId);
                v.getContext().startActivity(myIntent);
            }
        });


        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), JoinRoomRequestActivity.class);
                myIntent.putExtra("userId", userId);
                myIntent.putExtra("gameId", gameId);
                v.getContext().startActivity(myIntent);
            }
        });

        recyclerView = view.findViewById(R.id.ThirdRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).orderByChild("createDate"), RoomModel.class)
                        .build();

        adapter = new ViewOwnRoomsAdapter(options, gameId, userId);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fabClickable(boolean clickFab) {
        if (!clickFab) {
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
        } else {
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
        }
    }

    private void fabVisibility(boolean clickFab) {
        if (!clickFab) {
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
        } else {
            fab1.setVisibility(View.INVISIBLE);
            fab2.setVisibility(View.INVISIBLE);
            fab3.setVisibility(View.INVISIBLE);
        }
    }

    private void fabAnimation(boolean clickFab) {
        if (!clickFab) {
            fabMain.startAnimation(animation2);
            fab1.startAnimation(animation3);
            fab2.startAnimation(animation3);
            fab3.startAnimation(animation3);
        } else {
            fabMain.startAnimation(animation1);
            fab1.startAnimation(animation4);
            fab2.startAnimation(animation4);
            fab3.startAnimation(animation4);
        }
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