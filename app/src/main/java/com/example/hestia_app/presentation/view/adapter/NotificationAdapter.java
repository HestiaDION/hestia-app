package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;

    public NotificationAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_notificacao_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.titleNotification.setText(notification.getTitle());
        holder.dateNotification.setText(notification.getDate());
        holder.descNotification.setText(notification.getDescription());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleNotification, dateNotification, descNotification;
        CardView cardView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleNotification = itemView.findViewById(R.id.title_notification);
            dateNotification = itemView.findViewById(R.id.date_notification);
            descNotification = itemView.findViewById(R.id.desc_notification);
            cardView = itemView.findViewById(R.id.moradia);
        }
    }
}
