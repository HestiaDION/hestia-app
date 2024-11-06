package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Moradia;

import java.util.List;

public class MoradiasFavoritasAdapter extends RecyclerView.Adapter<MoradiasFavoritasAdapter.MoradiaViewHolder> {

    private final List<Moradia> moradiaList;
    private final Context context;

    public MoradiasFavoritasAdapter(Context context, List<Moradia> moradiaList) {
        this.context = context;
        this.moradiaList = moradiaList;
    }

    @NonNull
    @Override
    public MoradiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_moradias_favoritas, parent, false);
        return new MoradiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoradiaViewHolder holder, int position) {
        Moradia moradia = moradiaList.get(position);
        holder.cNmMoradia.setText(moradia.getNomeCasa());
        holder.qntPessoas.setText(moradia.getQuantidadeMaximaPessoas());
        holder.iQntQuartos.setText(moradia.getQuantidadeQuartos() + " quartos");
        holder.iQntPessoasMax.setText("Capacidade de " + moradia.getQuantidadeMaximaPessoas() + " pessoas");
        holder.nValor.setText("R$" + moradia.getAluguel());

        // Exemplo de como configurar a imagem
//        holder.houseImg.setImageResource(moradia.getImagemResId());

        // Ações dos botões
        holder.infoButton.setOnClickListener(v -> {
            // Lógica para o botão "Mais informações"
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Lógica para o botão de exclusão
        });
    }

    @Override
    public int getItemCount() {
        return moradiaList.size();
    }

    static class MoradiaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView houseImg;
        TextView cNmMoradia, qntPessoas, iQntQuartos, iQntPessoasMax, nValor;
        AppCompatButton infoButton;
        ImageButton deleteButton;

        public MoradiaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            houseImg = itemView.findViewById(R.id.houseImg);
            cNmMoradia = itemView.findViewById(R.id.cNmMoradia);
            qntPessoas = itemView.findViewById(R.id.qntPessoas);
            iQntQuartos = itemView.findViewById(R.id.iQntQuartos);
            iQntPessoasMax = itemView.findViewById(R.id.iQntPessoasMax);
            nValor = itemView.findViewById(R.id.nValor);
            infoButton = itemView.findViewById(R.id.infoButton);
            deleteButton = itemView.findViewById(R.id.button);
        }
    }
}

