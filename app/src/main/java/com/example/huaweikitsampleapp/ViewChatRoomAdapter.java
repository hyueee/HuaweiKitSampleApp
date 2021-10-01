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

public class ViewChatRoomAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewChatRoomAdapter.myViewHolder> {
    String gameId, userId;
    TextView text;
    RecyclerView recyclerView;

    public ViewChatRoomAdapter(@NonNull FirebaseRecyclerOptions<RoomModel> options, String gameId, String userId, TextView text, RecyclerView recyclerView) {
        super(options);
        this.gameId = gameId;
        this.userId = userId;
        this.recyclerView = recyclerView;
        this.text = text;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewChatRoomAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText("Room : " + model.getName());
        holder.thirdText.setText("Game Server : " + model.getServer());
        holder.fourthText.setText("Available Playing Day / Time : " + model.getTime());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationModel.room_selected = model.getId();
                Intent myIntent = new Intent(v.getContext(), ChatActivity.class);
                myIntent.putExtra("roomId", model.getId());
                myIntent.putExtra("roomName", model.getName());
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_design, parent, false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstText, thirdText, fourthText;
        CardView cardView;

        public myViewHolder(@NonNull  View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            fourthText = itemView.findViewById(R.id.fourthtext);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
