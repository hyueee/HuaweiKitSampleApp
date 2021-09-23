package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ViewOwnRoomsAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewOwnRoomsAdapter.myViewHolder> {
    String userId, gameId;

    public ViewOwnRoomsAdapter(@NonNull FirebaseRecyclerOptions<RoomModel> options, String gameId, String userId) {
        super(options);
        this.gameId = gameId;
        this.userId = userId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewOwnRoomsAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText(model.getName());
        holder.scdText.setText(model.getCurrentPlayer() + "/" + model.getNumPlayer());
        holder.thirdText.setText("Game Server : " + model.getServer());
        holder.fourthText.setText("Available Playing Day / Time : " + model.getTime());

        holder.updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Delete Room?")
                        .setCancelable(false)
                        .setMessage("Are you confirm to delete this room?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

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

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_own_rooms_design, parent, false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstText, scdText, thirdText, fourthText, updateText, deleteText;
        CardView cardView;

        public myViewHolder(@NonNull  View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            fourthText = itemView.findViewById(R.id.fourthtext);
            cardView = itemView.findViewById(R.id.cardView);
            updateText = itemView.findViewById(R.id.updatetext);
            deleteText = itemView.findViewById(R.id.deletetext);
        }
    }
}
