package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hestia_app.NotificationWorker;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.ApiServiceCallback;
import com.example.hestia_app.data.services.ApiService;
import com.example.hestia_app.utils.NetworkUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {

    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 10000;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ImageView logo = findViewById(R.id.logo);
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logoAnimation);

        // verificando se tem acesso à internet
        if (!NetworkUtil.isConnected(this)) {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_LONG).show();
            Bundle bundle = new Bundle();
            bundle.putString("textExplanation", "Héstia requer acesso à internet para se conectar ao servidor. Por favor, verifique se está conectado a rede e tente novamente.");
            bundle.putInt("lottieAnimation", R.raw.no_internet);
            bundle.putString("tipo", "no_internet");
            Intent intent = new Intent(Splashscreen.this, TelaAviso.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {

            wakeUpApiWithRetries();
            new Handler().postDelayed(() -> {

                // garantindo que o usuário tem permissão para receber notificações
                requestNotificationPermission();

                OneTimeWorkRequest testNotificationWork =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class).build();

                WorkManager.getInstance(this).enqueue(testNotificationWork);

                PeriodicWorkRequest notificationWork =
                        new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.HOURS)
                                .build();

                WorkManager.getInstance(this).enqueue(notificationWork);


                Intent intent = new Intent(Splashscreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 3000);
        }




    }

    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }
    private void testNotificationDirectly() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Configura o canal de notificação (apenas para API 26+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "test_notification",
                    "Teste de Notificação",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test_notification")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Teste de Notificação")
                .setContentText("Se você vê isso, as notificações funcionam!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(2, builder.build());
    }

    private void wakeUpApiWithRetries() {
        wakeUpApi();
    }

    private void retryApiWakeUp() {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            Log.d("SplashScreen", "Tentativa " + retryCount + " de acordar a API...");

            // Espera um tempo definido antes de tentar novamente
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    wakeUpApi();
                }
            }, RETRY_DELAY_MS);
        } else {
            // Após o número máximo de tentativas, exibe uma mensagem e continua
            Log.e("SplashScreen", "Número máximo de tentativas atingido.");

            // fechar aplicativo
            finish();
        }
    }

    private void wakeUpApi() {
        // acordar apis
        ApiService apiService = new ApiService();
        apiService.wakeUpApi(new ApiServiceCallback() {
            @Override
            public void onApiServiceSuccess(Map<String, String> message) {
                Log.d("WakeUpApi", "onApiServiceSuccess: " + message);
                // Navegar para MainActivity após 3 segundos

            }

            @Override
            public void onApiServiceFailure(String errorMessage) {
                Log.d("WakeUpApi", "onApiServiceFailure: " + errorMessage);
                retryApiWakeUp();
            }
        });
    }
}