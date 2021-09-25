package com.example.huaweikitsampleapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewApprovalAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewApprovalAdapter.myViewHolder> {
    String userId, gameId;

    public ViewApprovalAdapter(FirebaseRecyclerOptions<RoomModel> options, String userId, String gameId) {
        super(options);
        this.userId = userId;
        this.gameId = gameId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewApprovalAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText("Waiting approval for Room : " + model.getName());
        holder.scdText.setText("Request date : " + model.getRequestDate());
        holder.thirdText.setText("Request status : " + model.getStatus());

        if (!model.getStatus().equals("Requesting")) {
            holder.cardView.setEnabled(false);
            holder.cancelText.setEnabled(false);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#f2f2f2"));
        }

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

        holder.cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewApprovalAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_approval_design, parent, false);
        return new ViewApprovalAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView firstText, scdText, thirdText, cancelText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            cancelText = itemView.findViewById(R.id.canceltext);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
