package com.example.huaweikitsampleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRoomPlayerAdapter extends RecyclerView.Adapter<ViewRoomPlayerAdapter.myViewHolder> {
    String gameId, userId, roomId, owner;
    private ArrayList<UserModel> arrayList;
    DatabaseReference myRef;

    public ViewRoomPlayerAdapter(@NonNull ArrayList<UserModel> arrayList, String userId, String gameId, String roomId, String owner) {
        this.arrayList = arrayList;
        this.gameId = gameId;
        this.userId = userId;
        this.roomId = roomId;
        this.owner = owner;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_room_owner_design, parent, false);
            return new ViewRoomPlayerAdapter.myViewHolder(view);
        } else {
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_room_user_design, parent, false);
            return new ViewRoomPlayerAdapter.myViewHolder(view2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String name = arrayList.get(position).getUsername();
        holder.firstText.setText(name);

        if (getItemViewType(position) == 1) {
            myRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("username").getValue().toString();
                    if (username.equals(name)) {
                        holder.scdText.setVisibility(View.VISIBLE);
                        holder.scdText.setClickable(false);
                    } else {
                        holder.scdText.setVisibility(View.VISIBLE);
                        holder.scdText.setText("Kick");
                        holder.scdText.setClickable(true);
                    }

                    myRef.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.scdText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "hiiihiih", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            myRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(owner);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("username").getValue().toString();
                    if (username.equals(name)) {
                        holder.scdText.setVisibility(View.VISIBLE);
                    }

                    myRef.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (owner.equals(userId)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstText, scdText;

        public myViewHolder(View view) {
            super(view);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
        }
    }
}
