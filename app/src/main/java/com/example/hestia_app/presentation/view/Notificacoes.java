package com.example.hestia_app.presentation.view;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Notification;
import com.example.hestia_app.presentation.view.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class Notificacoes extends AppCompatActivity {

    ImageView voltar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        voltar = findViewById(R.id.goBackArrow);
        voltar.setOnClickListener(v -> finish());

        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Confira as novidades!", "01/11/2024, 08:00", "Veja as atualizações no feed do nosso app."));
        notifications.add(new Notification("Seu pagamento foi concluído!", "Hoje, 20:00", "Seu pagamento foi concluído!"));
        notifications.add(new Notification("Confira as novidades!", "Hoje, 08:00", "Veja as atualizações no feed do nosso app."));
        notifications.add(new Notification("Novo imóvel disponível!", "02/11/2024, 09:00", "Veja o novo imóvel que acabamos de adicionar à nossa plataforma. Não perca!"));
        notifications.add(new Notification("Pagamento de aluguel realizado!", "Hoje, 20:00", "Confirmamos o pagamento do seu aluguel. Obrigado por estar em dia!"));
        notifications.add(new Notification("Fatura de aluguel disponível", "Ontem, 15:00", "Sua fatura de aluguel já está disponível. Confira os detalhes no app."));
        notifications.add(new Notification("Inspeção agendada!", "Hoje, 11:00", "Lembre-se da inspeção do imóvel agendada para a próxima semana."));
        notifications.add(new Notification("Promoção para novos inquilinos", "03/11/2024, 10:30", "Indique um amigo e ganhe descontos no próximo mês de aluguel."));
        notifications.add(new Notification("Imóvel próximo do vencimento", "Hoje, 17:00", "Seu contrato está próximo do vencimento. Confira as opções de renovação."));
        notifications.add(new Notification("Nova mensagem do proprietário", "Ontem, 19:20", "O proprietário enviou uma mensagem para você. Acesse seu inbox para ler."));
        notifications.add(new Notification("Imóvel agendado para visita", "04/11/2024, 14:00", "Visita ao imóvel agendada. Prepare-se para conferir seu possível novo lar!"));
        notifications.add(new Notification("Dicas de conservação do imóvel", "Hoje, 08:30", "Confira nossas dicas para manter o imóvel sempre em boas condições."));
        notifications.add(new Notification("Renove seu contrato", "05/11/2024, 09:45", "É hora de renovar seu contrato de aluguel. Garanta seu lar por mais um período."));

        recyclerView = findViewById(R.id.recycler_ad);

        NotificationAdapter notificationsAdapter = new NotificationAdapter(notifications, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notificationsAdapter);
        notificationsAdapter.notifyDataSetChanged();
    }
}