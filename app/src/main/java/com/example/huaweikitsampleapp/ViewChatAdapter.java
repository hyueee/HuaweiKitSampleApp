package com.example.huaweikitsampleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewChatAdapter extends FirebaseRecyclerAdapter<ChatModel, ViewChatAdapter.myViewHolder> {
    String userId;

    public ViewChatAdapter(@NonNull FirebaseRecyclerOptions<ChatModel> options, String userId) {
        super(options);
        this.userId =userId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewChatAdapter.myViewHolder holder, int position, @NonNull ChatModel model) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        if (getItemViewType(position) == 1) {

            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.message.setText(model.getMessage());
            holder.time.setText(model.getTime());

            if (model.getTop().equals("None")) {
                if (currentDate.equals(model.getDate())) {
                    holder.date.setVisibility(View.VISIBLE);
                    holder.date.setText("TODAY");
                } else {
                    holder.date.setVisibility(View.VISIBLE);
                    holder.date.setText(model.getDate());
                }
            } else {
                holder.date.setVisibility(View.GONE);
            }

//            String key = model.getTimeStamp() + "@" + model.getSender();
//
//            holder.binding.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    new AlertDialog.Builder(v.getContext())
//                            .setIcon(R.drawable.ic_error)
//                            .setTitle("Delete?")
//                            .setCancelable(false)
//                            .setMessage("Are you confirm to delete this message?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    manageChatViewModel.deleteMessage(course, key);
//                                }
//                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//
//                    return false;
//                }
//            });

        } else if (getItemViewType(position) == 2){

            if (model.getTop().equals("None")) {
                if (currentDate.equals(model.getDate())) {
                    holder.date.setVisibility(View.VISIBLE);
                    holder.date.setText("TODAY");
                } else {
                    holder.date.setVisibility(View.VISIBLE);
                    holder.date.setText(model.getDate());
                }
            } else {
                holder.date.setVisibility(View.GONE);
            }

            holder.linearLayout2.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.message.setText(model.getMessage());
            holder.username.setText(model.getUserName());
            holder.time.setText(model.getTime());

        }

    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = getItem(position);

        if (chatModel.getSender().equals(userId)) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public ViewChatAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_sender, parent, false);
            return new ViewChatAdapter.myViewHolder(view);
        } else {
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_receiver, parent, false);
            return new ViewChatAdapter.myViewHolder(view2);
        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        Button date;
        LinearLayout linearLayout, linearLayout2;
        TextView time, message, username;

        public myViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            linearLayout = view.findViewById(R.id.linearLayout);
            linearLayout2 = view.findViewById(R.id.messageLayout2);
            time = view.findViewById(R.id.displayTime);
            message = view.findViewById(R.id.showMessage);
            username = view.findViewById(R.id.userName);
        }

    }

//    public class myViewHolder2 extends myViewHolder {
//        Button date_receive;
//        LinearLayout linearLayout_receive;
//        TextView time_receive, message_receive, username_receive;
//
//        public myViewHolder2(View view2) {
//            super(view2);
//
//            date_receive = view2.findViewById(R.id.date);
//            linearLayout_receive = view2.findViewById(R.id.messageLayout2);
//            time_receive = view2.findViewById(R.id.displayTime);
//            username_receive = view2.findViewById(R.id.userName);
//            message_receive = view2.findViewById(R.id.showMessage);
//        }
//    }
}
