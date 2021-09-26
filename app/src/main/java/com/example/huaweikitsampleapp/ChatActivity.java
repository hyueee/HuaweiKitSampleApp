package com.example.huaweikitsampleapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewChatAdapter adapter;
    TextView roomTitle;
    ImageView btnBack, btnSend;
    EditText message;
    DatabaseReference myRef;
//    ICFMService icfmService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        icfmService = RetrofitFCMClient.getInstance().create(ICFMService.class);

        String userId = getIntent().getStringExtra("userId");
        String gameId = getIntent().getStringExtra("gameId");
        String roomId = getIntent().getStringExtra("roomId");
        String roomName = getIntent().getStringExtra("roomName");

        roomTitle = findViewById(R.id.chatUser);
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        message = findViewById(R.id.message);
        recyclerView = findViewById(R.id.recyclerView);

        roomTitle.setText("Room " + roomName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        FirebaseRecyclerOptions<ChatModel> options =
                new FirebaseRecyclerOptions.Builder<ChatModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("chat").child(gameId).child(roomId), ChatModel.class)
                        .build();

        adapter = new ViewChatAdapter(options, userId);
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = message.getText().toString();

                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "Type something!", Toast.LENGTH_SHORT).show();

                } else {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.getDefault());
                    String current = dateFormat.format(new Date());
                    String[] splitCurr = current.split(" ");
                    String date = splitCurr[0];
                    String time = splitCurr[1].substring(0, 5) + " " + splitCurr[2].toUpperCase();

                    long dateMillis = 0;

                    //convert date to milliseconds
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date getDate = sdf.parse(date);
                        assert getDate != null;
                        dateMillis = getDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String output = null;
                    try{
                        DateFormat df = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                        //Desired format: 24 hour format: Change the pattern as per the need
                        DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        Date getDate2 = df.parse(splitCurr[1] + " " + splitCurr[2]);
                        //Changing the format of date and storing it in String
                        assert getDate2 != null;
                        output = outputFormat.format(getDate2);

                    }catch(ParseException pe){
                        pe.printStackTrace();
                    }

                    String timeStamp = dateMillis + "-" + output;
                    String key = timeStamp + "@" + userId;

                    Map<String, Object> messageSend = new HashMap<>();
                    messageSend.put("sender", userId);
                    messageSend.put("date", date);
                    messageSend.put("time", time);
                    messageSend.put("message", text);
                    messageSend.put("image", "None");
                    messageSend.put("timeStamp", timeStamp);
                    messageSend.put("userName", "user");
                    messageSend.put("top", "None");

                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userName = snapshot.child("username").getValue().toString();
                            messageSend.replace("userName", userName);

                            myRef.removeEventListener(this);

                            Query query= FirebaseDatabase.getInstance().getReference().child("chat").child(gameId).child(roomId).orderByKey().limitToLast(1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String lastKey = "";
                                        for(DataSnapshot ss : snapshot.getChildren()) {
                                            lastKey = ss.getKey();
                                        }

                                        if (lastKey.substring(0, 13).equals(key.substring(0, 13))) {
                                            messageSend.replace("top", lastKey);
                                            query.removeEventListener(this);
                                        }
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("chat").child(gameId).child(roomId).child(key).setValue(messageSend)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    message.setText(null);
//                                                    Map<String, String> notiData = new HashMap<>();
//                                                    notiData.put(NotificationModel.NOTI_ID, id);
//                                                    notiData.put(NotificationModel.NOTI_TITLE, "Message from " + courseName);
//                                                    notiData.put(NotificationModel.NOTI_CONTENT, arrayList.get(0).getUserName() + ": [image]");
//                                                    notiData.put(NotificationModel.NOTI_SENDER, arrayList.get(0).getSender());
//                                                    notiData.put(NotificationModel.NOTI_COURSE, courseSec);
//                                                    notiData.put(NotificationModel.NOTI_COURSE_NAME, courseName);
//
//                                                    FCMSendData fcmSendData = new FCMSendData("/topics/"+ courseSec, notiData);
//
//                                                    compositeDisposable.add(
//                                                            icfmService.sendNotification(fcmSendData)
//                                                                    .subscribeOn(Schedulers.newThread())
//                                                                    .observeOn(AndroidSchedulers.mainThread())
//                                                                    .subscribe(new Consumer<FCMResponse>() {
//                                                                                   @Override
//                                                                                   public void accept(FCMResponse fcmResponse) {
//
//                                                                                   }
//                                                                               }, new Consumer<Throwable>() {
//                                                                                   @Override
//                                                                                   public void accept(Throwable throwable) {
//                                                                                       Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                                   }
//                                                                               }
//                                                                    )
//                                                    );
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
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
