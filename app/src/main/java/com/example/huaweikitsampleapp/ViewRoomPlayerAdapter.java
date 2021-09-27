package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.LongFunction;

public class ViewRoomPlayerAdapter extends RecyclerView.Adapter<ViewRoomPlayerAdapter.myViewHolder> {
    String gameId, userId, roomId, owner;
    private ArrayList<User> arrayList;
    DatabaseReference myRef;
    Task<Void> myTask;

    public ViewRoomPlayerAdapter(@NonNull ArrayList<User> arrayList, String userId, String gameId, String roomId, String owner) {
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
        String id = arrayList.get(position).getId();

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
                    new AlertDialog.Builder(v.getContext())
                            .setIcon(R.drawable.ic_warning)
                            .setTitle("Kick Player?")
                            .setMessage("Are you confirm to kick this player?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Query query = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser").orderByValue().equalTo(id);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot ss : snapshot.getChildren()) {
                                                    String key = ss.getKey();

                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(roomId).child("joinedUser").child(key);
                                                    myTask = myRef.removeValue();
                                                    myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Map<String, Object> kickPlayer = new HashMap<>();
                                                            kickPlayer.put("status", "You have been kicked");
                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("chatRoom").child(gameId).child(roomId).updateChildren(kickPlayer);
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(roomId);
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int player = Integer.parseInt(snapshot.child("currentPlayer").getValue().toString());
                                            player -= 1;

                                            Map<String, Object> updatePlayer = new HashMap<>();
                                            updatePlayer.put("currentPlayer", Integer.toString(player));

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(roomId)
                                                    .updateChildren(updatePlayer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(roomId).updateChildren(updatePlayer)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    new AlertDialog.Builder(v.getContext())
                                                                            .setIcon(R.drawable.ic_warning)
                                                                            .setTitle("Kick Player Success")
                                                                            .setMessage("You have kicked this player successfully.")
                                                                            .setCancelable(false)
                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            }).show();
                                                                }
                                                            });

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
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
