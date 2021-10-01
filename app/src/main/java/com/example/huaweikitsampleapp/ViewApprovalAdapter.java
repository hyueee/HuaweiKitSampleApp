package com.example.huaweikitsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ViewApprovalAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewApprovalAdapter.myViewHolder> {
    String userId, gameId;
    DatabaseReference myRef;
    Task<Void> myTask;
    TextView text;
    RecyclerView recyclerView;

    public ViewApprovalAdapter(FirebaseRecyclerOptions<RoomModel> options, String userId, String gameId, TextView text, RecyclerView recyclerView) {
        super(options);
        this.userId = userId;
        this.gameId = gameId;
        this.text = text;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewApprovalAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText("Waiting approval for Room : " + model.getName());
        holder.scdText.setText("Request date : " + model.getRequestDate());
        holder.thirdText.setText("Request status : " + model.getStatus());

        if (!model.getStatus().equals("Requesting")) {
            holder.cardView.setEnabled(false);
            holder.cancelText.setText("Delete");
            holder.cardView.setCardBackgroundColor(Color.parseColor("#f2f2f2"));

            holder.cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setIcon(R.drawable.ic_warning)
                            .setTitle("Delete Room Request")
                            .setMessage("Are you confirm to delete this room request?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("approvalRoom").child(gameId).child(model.getId());
                                    myTask = myRef.removeValue();
                                    myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            new AlertDialog.Builder(v.getContext())
                                                    .setIcon(R.drawable.ic_check)
                                                    .setTitle("Delete Room Request Success")
                                                    .setMessage("You have deleted this room request successfully.")
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
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        } else {
            holder.cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setIcon(R.drawable.ic_warning)
                            .setTitle("Cancel Room Request")
                            .setMessage("Are you confirm to cancel this room request?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myRef = FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(model.getId());
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String ownerId = snapshot.child("owner").getValue().toString();

                                            myRef.removeEventListener(this);

                                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ownerId).child("requestNum");
                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int num = Integer.parseInt(snapshot.getValue().toString());
                                                    num -= 1;

                                                    myRef.removeEventListener(this);

                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(ownerId).child("requestNum").setValue(Integer.toString(num))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(ownerId).child("requestJoinRoom").child(gameId).orderByChild("requestUser").equalTo(userId)
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    for (DataSnapshot ss :snapshot.getChildren()) {
                                                                                        String key = ss.getKey();
                                                                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ownerId).child("requestJoinRoom").child(gameId).child(key);
                                                                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                String roomId = snapshot.child("id").getValue().toString();
                                                                                                if (roomId.equals(model.getId())) {
                                                                                                    snapshot.getRef().removeValue();
                                                                                                }
                                                                                                myRef.removeEventListener(this);
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                                                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });

                                                                                    }

                                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser").child(userId);
                                                                                    myTask = myRef.removeValue();
                                                                                    myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            Map<String, Object> cancelRoom = new HashMap<>();
                                                                                            cancelRoom.put("status", "Request Canceled");

                                                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("approvalRoom").child(gameId).child(model.getId())
                                                                                                    .updateChildren(cancelRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    new AlertDialog.Builder(v.getContext())
                                                                                                            .setIcon(R.drawable.ic_check)
                                                                                                            .setTitle("Cancel Room Request Success")
                                                                                                            .setCancelable(false)
                                                                                                            .setMessage("You have canceled this room request successfully.")
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
                                                                                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                }
                                                            });

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
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
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
