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

public class ViewRequestAdapter extends FirebaseRecyclerAdapter<RoomModel, ViewRequestAdapter.myViewHolder> {
    DatabaseReference myRef;
    String userId, gameId;
    Task<Void> myTask;

    public ViewRequestAdapter(FirebaseRecyclerOptions<RoomModel> options, String userId, String gameId) {
        super(options);
        this.userId = userId;
        this.gameId = gameId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewRequestAdapter.myViewHolder holder, int position, @NonNull RoomModel model) {
        holder.firstText.setText("Request to join Room : " + model.getName());
        holder.scdText.setText("Room Id : " + model.getId());

        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getRequestUser());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue().toString();
                holder.thirdText.setText("Request by : " + name);
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.acceptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Accept Player")
                        .setMessage("Are you confirm to accept this player?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(model.getId());
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int totalPlayer = Integer.parseInt(snapshot.child("numPlayer").getValue().toString());
                                        myRef.removeEventListener(this);

                                        myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("joinedUser");
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int currentJoinedUser = (int) snapshot.getChildrenCount();

                                                if (currentJoinedUser == totalPlayer) {
                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser");
                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Map<String, Object> fullRoom = new HashMap<>();
                                                            fullRoom.put("status", "This room is full");

                                                            int childNum = (int) snapshot.getChildrenCount();
                                                            int total = 0;

                                                            myRef.removeEventListener(this);

                                                            for (DataSnapshot ss : snapshot.getChildren()) {
                                                                myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString()).child("approvalRoom").child(gameId).child(model.getId());
                                                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(ss.getValue().toString()).child("approvalRoom").child(gameId).child(model.getId())
                                                                                    .updateChildren(fullRoom);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                total++;

                                                                if (total == childNum) {
                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser");
                                                                    myTask = myRef.removeValue();
                                                                    myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum");
                                                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    int num = Integer.parseInt(snapshot.getValue().toString());
                                                                                    num = num - childNum;

                                                                                    myRef.removeEventListener(this);

                                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum").setValue(Integer.toString(num))
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).orderByChild("id").equalTo(model.getId())
                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    for (DataSnapshot ss :snapshot.getChildren()) {
                                                                                                                        String key = ss.getKey();
                                                                                                                        snapshot.getRef().removeValue();
                                                                                                                    }

                                                                                                                    new AlertDialog.Builder(v.getContext())
                                                                                                                            .setIcon(R.drawable.ic_warning)
                                                                                                                            .setTitle("Room is Full")
                                                                                                                            .setCancelable(false)
                                                                                                                            .setMessage("Sorry, you can't accept more player as the room is full.")
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
                                                                                            });

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                } else {
                                                    Map<String, Object> joinRoom = new HashMap<>();
                                                    joinRoom.put("name", model.getName());
                                                    joinRoom.put("server", model.getServer());
                                                    joinRoom.put("language", model.getLanguage());
                                                    joinRoom.put("time", model.getTime());
                                                    joinRoom.put("id", model.getId());

                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(model.getRequestUser()).child("chatRoom").child(gameId).child(model.getId())
                                                            .setValue(joinRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Map<String, Object> acceptRoom = new HashMap<>();
                                                            acceptRoom.put("status", "Accepted");

                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(model.getRequestUser()).child("approvalRoom").child(gameId).child(model.getId())
                                                                    .updateChildren(acceptRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum");
                                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            int num = Integer.parseInt(snapshot.getValue().toString());
                                                                            num -= 1;

                                                                            myRef.removeEventListener(this);

                                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum").setValue(Integer.toString(num))
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(model.getId());
                                                                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    int addPlayer = Integer.parseInt(snapshot.child("currentPlayer").getValue().toString());
                                                                                                    addPlayer += 1;

                                                                                                    Map<String, Object> addIntoRoom = new HashMap<>();
                                                                                                    addIntoRoom.put("currentPlayer", Integer.toString(addPlayer));

                                                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("room").child(gameId).child(model.getId()).updateChildren(addIntoRoom)
                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                    FirebaseDatabase.getInstance().getReference().child("room").child(gameId).child(model.getId()).updateChildren(addIntoRoom)
                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser").child(model.getRequestUser())
                                                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                    snapshot.getRef().removeValue();

                                                                                                                                                    myRef.removeEventListener(this);

                                                                                                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("joinedUser");
                                                                                                                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                            int num = (int) snapshot.getChildrenCount();
                                                                                                                                                            num += 1;

                                                                                                                                                            String addPlayer = Integer.toString(num);
                                                                                                                                                            Map<String, Object> updateUser = new HashMap<>();
                                                                                                                                                            updateUser.put(addPlayer, model.getRequestUser());

                                                                                                                                                            myRef.removeEventListener(this);

                                                                                                                                                            FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("joinedUser")
                                                                                                                                                                    .updateChildren(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).orderByChild("requestUser").equalTo(model.getRequestUser())
                                                                                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                    for (DataSnapshot ss :snapshot.getChildren()) {
                                                                                                                                                                                        String key = ss.getKey();
                                                                                                                                                                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).child(key);
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

                                                                                                                                                                                            }
                                                                                                                                                                                        });

                                                                                                                                                                                    }

                                                                                                                                                                                    new AlertDialog.Builder(v.getContext())
                                                                                                                                                                                            .setIcon(R.drawable.ic_check)
                                                                                                                                                                                            .setTitle("Accept Player Success")
                                                                                                                                                                                            .setCancelable(false)
                                                                                                                                                                                            .setMessage("You have accepted this player to your room.")
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
                                                                                                                                                            });
                                                                                                                                                        }

                                                                                                                                                        @Override
                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                }
                                                                                                                                            });

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
                                                                                    });

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

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

        holder.rejectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Reject Player")
                        .setMessage("Are you confirm to reject this player?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> rejectRoom = new HashMap<>();
                                rejectRoom.put("status", "Rejected");

                                FirebaseDatabase.getInstance().getReference().child("Users").child(model.getRequestUser()).child("approvalRoom").child(gameId).child(model.getId())
                                        .updateChildren(rejectRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum");
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int num = Integer.parseInt(snapshot.getValue().toString());
                                                num -= 1;

                                                myRef.removeEventListener(this);

                                                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestNum").setValue(Integer.toString(num))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).orderByChild("requestUser").equalTo(model.getRequestUser())
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                for (DataSnapshot ss :snapshot.getChildren()) {
                                                                                    String key = ss.getKey();
                                                                                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requestJoinRoom").child(gameId).child(key);
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

                                                                                        }
                                                                                    });

                                                                                }

                                                                                myRef = FirebaseDatabase.getInstance().getReference().child("roomUser").child(gameId).child(model.getId()).child("requestUser").child(model.getRequestUser());
                                                                                myTask = myRef.removeValue();
                                                                                myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        new AlertDialog.Builder(v.getContext())
                                                                                                .setIcon(R.drawable.ic_check)
                                                                                                .setTitle("Reject Player Success")
                                                                                                .setCancelable(false)
                                                                                                .setMessage("You have rejected this player to your room.")
                                                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                        dialog.dismiss();
                                                                                                    }
                                                                                                }).show();
                                                                                    }
                                                                                });

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

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

    @NonNull
    @Override
    public ViewRequestAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request_design, parent, false);
        return new ViewRequestAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView firstText, scdText, thirdText, acceptText, rejectText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            firstText = itemView.findViewById(R.id.firsttext);
            scdText = itemView.findViewById(R.id.scdtext);
            thirdText = itemView.findViewById(R.id.thirdtext);
            acceptText = itemView.findViewById(R.id.accepttext);
            rejectText = itemView.findViewById(R.id.rejecttext);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
