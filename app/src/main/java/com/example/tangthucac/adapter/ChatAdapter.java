package com.example.tangthucac.adapter;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tangthucac.R;
import com.example.tangthucac.model.Message;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Message> messages;
    private static final int TYPE_BOT = 1;
    private static final int TYPE_USER = 2;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getSender().equals("bot") ? TYPE_BOT : TYPE_USER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BOT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_message, parent, false);
            return new BotMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getItemViewType() == TYPE_BOT) {
            ((BotMessageViewHolder) holder).bind(message);
        } else {
            ((UserMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.bot_message_text);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());
        }
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.user_message_text);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());
        }
    }
}