package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewLobbyAdapter extends FirebaseRecyclerAdapter<LobbyModel, ViewLobbyAdapter.myViewHolder> {

    public ViewLobbyAdapter(@NonNull FirebaseRecyclerOptions<LobbyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewLobbyAdapter.myViewHolder holder, int position, @NonNull LobbyModel model) {
        

        

        
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_design, parent, false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView nameText, idText, emailText;
        ImageView img;

        public myViewHolder(@NonNull  View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.firsttext);
            idText = itemView.findViewById(R.id.scdtext);
            emailText = itemView.findViewById(R.id.thirdtext);
            img = itemView.findViewById(R.id.img1);
        }
    }
}
