package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewOwnRoomsAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewOwnRoomsAdapter.myViewHolder> {
    String userId, gameId;
    Task<Void> myTask;
    DatabaseReference myRef;
    int childNum;

    public ViewOwnRoomsAdapter(@NonNull FirebaseRecyclerOptions<RoomModel> options, String gameId, String userId) {
        super(options);
        this.gameId = gameId;
        this.userId = userId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewOwnRoomsAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText(model.getName());
        holder.scdText.setText(model.getCurrentPlayer() + "/" + model.getNumPlayer());
        holder.thirdText.setText("Room ID : " + model.getId());
        holder.fourthText.setText("Game Server : " + model.getServer());
        holder.fifthText.setText("Available Playing Day / Time : " + model.getTime());

        holder.updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UpdateRoomActivity.class);
                myIntent.putExtra("gameId", gameId);
                myIntent.putExtra("roomId", model.getId());
                myIntent.putExtra("userId", userId);
                v.getContext().startActivity(myIntent);
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
                                myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(model.getId());
                                myTask = myRef.removeValue();
                                myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser");
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Map<String, Object> deleteRoom = new HashMap<>();
                                                    deleteRoom.put("status", "This room has been deleted.");

                                                    childNum = (int) snapshot.getChildrenCount();

                                                    for (DataSnapshot ss : snapshot.getChildren()) {
                                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString()).child("approvalRoom").child(gameId).child(model.getId());
                                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString()).child("approvalRoom").child(gameId).child(model.getId())
                                                                            .updateChildren(deleteRoom);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("joinedUser");
                                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int numPlayer = (int) snapshot.getChildrenCount();
                                                        int num = 0;

                                                        for (DataSnapshot ss : snapshot.getChildren()) {
                                                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString()).child("chatRoom").child(gameId).child(model.getId());
                                                            myRef.removeValue();

                                                            num++;

                                                            if (num == numPlayer) {
                                                                myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId());
                                                                myTask = myRef.removeValue();
                                                                myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(model.getId());
                                                                        myTask = myRef.removeValue();
                                                                        myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum");
                                                                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        if (snapshot.exists()) {
                                                                                            int num = Integer.parseInt(snapshot.getValue().toString());
                                                                                            num = num - childNum;

                                                                                            myRef.removeEventListener(this);

                                                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum").setValue(Integer.toString(num));
                                                                                        }

                                                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).orderByChild("id").equalTo(model.getId())
                                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                        if (snapshot.exists()) {
                                                                                                            for (DataSnapshot ss :snapshot.getChildren()) {
                                                                                                                String key = ss.getKey();
                                                                                                                snapshot.getRef().removeValue();
                                                                                                            }
                                                                                                        }

                                                                                                        new AlertDialog.Builder(v.getContext())
                                                                                                                .setIcon(R.drawable.ic_check)
                                                                                                                .setTitle("Delete Room Success")
                                                                                                                .setCancelable(false)
                                                                                                                .setMessage("You have deleted this room.")
                                                                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                                    @Override
                                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                                        dialog.dismiss();
                                                                                                                    }
                                                                                                                }).show();
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                                    }
                                                                                                });
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                                        Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
        TextView firstText, scdText, thirdText, fourthText, fifthText, updateText, deleteText;
        CardView cardView;

        public myViewHolder(@NonNull  View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            fourthText = itemView.findViewById(R.id.fourthtext);
            fifthText = itemView.findViewById(R.id.fifthtext);
            cardView = itemView.findViewById(R.id.cardView);
            updateText = itemView.findViewById(R.id.updatetext);
            deleteText = itemView.findViewById(R.id.deletetext);
        }
    }
}
