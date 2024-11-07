package com.example.hestia_app.presentation.view;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class Notificacoes extends AppCompatActivity {

    ImageView voltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        voltar = findViewById(R.id.goBackArrow);
        voltar.setOnClickListener(v -> finish());

        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Venha visitar o nosso feed!", "Hoje, 20:00", "Seu pagamento foi concluído!"));
        notifications.add(new Notification("Venha conferir !", "Hoje, 20:00", "Seu pagamento foi concluído!"));
        notifications.add(new Notification("Seu pagamento foi concluído!", "Hoje, 20:00", "Seu pagamento foi concluído!"));
        notifications.add(new Notification("Seu pagamento foi concluído!", "Hoje, 20:00", "Seu pagamento foi concluído!"));
    }
}