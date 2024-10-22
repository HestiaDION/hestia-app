package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.ApiServiceCallback;
import com.example.hestia_app.data.services.ApiService;
import com.example.hestia_app.utils.NetworkUtil;

import java.util.Map;

public class Splashscreen extends AppCompatActivity {

    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 10000;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Pegando o logo
        ImageView logo = findViewById(R.id.logo);

        // Carregando a animação
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logoAnimation);

        // verificando se tem acesso à internet
        if (!NetworkUtil.isConnected(this)) {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_LONG).show();
        } else {
            wakeUpApiWithRetries();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Splashscreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 3000);  // 3 segundos
        }
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