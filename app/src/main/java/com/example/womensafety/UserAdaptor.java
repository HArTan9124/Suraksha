package com.example.womensafety;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.ViewHolder> {

    private List<UserData> userList;

    public UserAdaptor(List<UserData> userList, Addfrnd addfrnd, Addfrnd addfrnd1) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intruder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData userData = userList.get(position);

        holder.usernameTextView.setText(userData.name);
        holder.contactNumberTextView.setText(userData.contactNumber);
        if ("dpb".equals(userData.profilePicture)) {
            holder.profileImageView.setImageResource(R.drawable.dpb);
        } else if ("dpg".equals(userData.profilePicture)) {
            holder.profileImageView.setImageResource(R.drawable.dpg);
        } else {
            holder.profileImageView.setImageResource(R.drawable.viewprofiler);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), Chat.class);
            intent.putExtra("userId", userData.id);
            intent.putExtra("userName", userData.name);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnUserClickListener {
        void onUserClick(UserData userData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView;
        TextView contactNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileShower);
            usernameTextView = itemView.findViewById(R.id.usernameshower);
            contactNumberTextView = itemView.findViewById(R.id.noshower);
        }
    }
}