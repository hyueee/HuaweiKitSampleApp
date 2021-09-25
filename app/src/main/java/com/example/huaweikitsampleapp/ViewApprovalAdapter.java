package com.example.huaweikitsampleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewApprovalAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewApprovalAdapter.myViewHolder> {

    public ViewApprovalAdapter(FirebaseRecyclerOptions<RoomModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewApprovalAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText(model.getName());
        holder.scdText.setText(model.getStatus());

    }

    @NonNull
    @Override
    public ViewApprovalAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lobby_design, parent, false);
        return new ViewApprovalAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView firstText, scdText, thirdText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
