package com.example.hestia_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("NotificationWorker", "Worker executado");
        sendNotification();
        return Result.success();
    }

    private void sendNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Verifica se o canal de notificação já existe (apenas para API 26+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "daily_notification",
                    "Notificações Diárias",
                    NotificationManager.IMPORTANCE_HIGH  // IMPORTANCE_HIGH para visibilidade
            );
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "daily_notification")
                .setSmallIcon(R.drawable.logo_casa_colorido)
                .setContentTitle("Confira as novidades!")
                .setContentText("Veja as atualizações no feed do nosso app.")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // IMPORTANCE_HIGH
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}
