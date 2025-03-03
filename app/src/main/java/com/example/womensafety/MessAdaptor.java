package com.example.womensafety;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessAdaptor extends RecyclerView.Adapter<MessAdaptor.MyViewHolder> {

    private final List<Message> messageList;

    public MessAdaptor(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, parent, false);
        return new MyViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.getSentby().equals(Message.SENT_BY_ME)) { // ✅ Corrected logic
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightTV.setText(message.getMessage());
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTV.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView, rightChatView;
        TextView leftTV, rightTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.leftChatview);
            rightChatView = itemView.findViewById(R.id.RightChatview);
            leftTV = itemView.findViewById(R.id.LeftMessage);
            rightTV = itemView.findViewById(R.id.RightMessage);
        }
    }
}
