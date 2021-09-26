package com.example.huaweikitsampleapp;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class ViewGameAdapter extends FirebaseRecyclerAdapter<GameModel, ViewGameAdapter.myViewHolder> {
    String userId;
    DatabaseReference myRef;

    public ViewGameAdapter(FirebaseRecyclerOptions<GameModel> options, String userId) {
        super(options);
        this.userId = userId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewGameAdapter.myViewHolder holder, int position, @NonNull GameModel model) {
        holder.firstText.setText(model.getName());
        holder.scdText.setText(model.getDetails());

        if (model.getName().equals("Counter-Strike: Global Offensive")) {
            holder.imageView.setBackgroundResource(R.drawable.csgo);
        } else if (model.getName().equals("Valorant")) {
            holder.imageView.setBackgroundResource(R.drawable.valorant);
        } else if (model.getName().equals("Dead by Daylight")) {
            holder.imageView.setBackgroundResource(R.drawable.dbd);
        } else if (model.getName().equals("Left 4 Dead")) {
            holder.imageView.setBackgroundResource(R.drawable.l4d);
        } else if (model.getName().equals("Dragon Nest")) {
            holder.imageView.setBackgroundResource(R.drawable.dragonest);
        } else if (model.getName().equals("Genshin Impact")) {
            holder.imageView.setBackgroundResource(R.drawable.genshin);
        } else if (model.getName().equals("Grand Theft Auto V")) {
            holder.imageView.setBackgroundResource(R.drawable.gta5);
        } else if (model.getName().equals("Maple Story")) {
            holder.imageView.setBackgroundResource(R.drawable.maple);
        }

        holder.thirdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), GameProfileActivity.class);
                myIntent.putExtra("id", model.getId());
                v.getContext().startActivity(myIntent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("chatRoom").child(model.getId());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                FirebaseMessaging.getInstance().subscribeToTopic(ss.child("id").getValue().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                            }
                        }
                        myRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent myIntent = new Intent(v.getContext(), GameLobbyActivity.class);
                myIntent.putExtra("frag", "first");
                myIntent.putExtra("gameId", model.getId());
                myIntent.putExtra("userId", userId);
                v.getContext().startActivity(myIntent);
            }
        });
    }

    @NonNull
    @Override
    public ViewGameAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_game_row_design, parent, false);
        return new ViewGameAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView firstText, scdText, thirdText;
        ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.gameImg);
            firstText = itemView.findViewById(R.id.firstText);
            scdText = itemView.findViewById(R.id.scdText);
            thirdText = itemView.findViewById(R.id.thirdText);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
