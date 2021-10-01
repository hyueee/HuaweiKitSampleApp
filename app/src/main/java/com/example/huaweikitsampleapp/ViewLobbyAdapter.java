package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewLobbyAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewLobbyAdapter.myViewHolder> {
    String gameId, userId;
    TextView text;
    RecyclerView recyclerView;

    public ViewLobbyAdapter(@NonNull FirebaseRecyclerOptions<RoomModel> options, String gameId, String userId, TextView text, RecyclerView recyclerView) {
        super(options);
        this.gameId = gameId;
        this.userId = userId;
        this.text = text;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewLobbyAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText(model.getName());
        holder.scdText.setText(model.getCurrentPlayer() + "/" + model.getNumPlayer());
        holder.thirdText.setText("Game Server : " + model.getServer());
        holder.fourthText.setText("Available Playing Day / Time : " + model.getTime());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ViewRoomDetailsActivity.class);
                myIntent.putExtra("id", model.getId());
                myIntent.putExtra("gameId", gameId);
                myIntent.putExtra("userId", userId);
                v.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public void onDataChanged() {
        if(getItemCount() == 0) {
            text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lobby_design, parent, false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstText, scdText, thirdText, fourthText;
        CardView cardView;

        public myViewHolder(@NonNull  View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            fourthText = itemView.findViewById(R.id.fourthtext);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
