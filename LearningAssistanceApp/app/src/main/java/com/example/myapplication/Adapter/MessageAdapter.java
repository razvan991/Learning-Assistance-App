package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Chat;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
    Context context;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final String imageurl;
    FirebaseUser fuser;
    List<Chat> mChat;

    public MessageAdapter(Context context, List<Chat> mChat,String imageurl) {
        this.context = context;
        this.mChat=mChat;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
        } else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageurl).into(holder.profile_image);
        }
        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            show_message=itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }
    @Override
    public int getItemViewType(int position){
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

}
