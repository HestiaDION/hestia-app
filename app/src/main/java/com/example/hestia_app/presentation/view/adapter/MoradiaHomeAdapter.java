package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hestia_app.R;
import com.example.hestia_app.domain.models.Moradia;

import java.util.List;

public class MoradiaHomeAdapter extends RecyclerView.Adapter<MoradiaHomeAdapter.MoradiaViewHolder> {

    private Context context;
    private List<Moradia> moradiaList;

    public MoradiaHomeAdapter(Context context, List<Moradia> moradiaList) {
        this.context = context;
        this.moradiaList = moradiaList;
    }

    @NonNull
    @Override
    public MoradiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_moradia_item, parent, false);
        return new MoradiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoradiaViewHolder holder, int position) {
        Moradia moradia = moradiaList.get(position);

        holder.nomeMoradia.setText(moradia.getNome());
        holder.dataDesde.setText(moradia.getDataDesde());
        int quantidadePessoas = moradia.getQuantidadePessoas();

        String texto;
        if (quantidadePessoas > 1) {
            texto = quantidadePessoas + " pessoas";
        } else {
            texto = quantidadePessoas + " pessoa";
        }
        holder.quantidadePessoas.setText(texto);

        List<String> imageList = moradia.getImageList();

        // Configure o ViewPager2 com o adapter de imagens
        HouseImgAdapter houseImgAdapter = new HouseImgAdapter(context, imageList);
        holder.imagensMoradia.setAdapter(houseImgAdapter);
        holder.setaDetalhes.setOnClickListener(v -> {
            // Lógica para abrir os detalhes da moradia, se necessário
        });
    }

    @Override
    public int getItemCount() {
        return moradiaList.size();
    }

    public static class MoradiaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeMoradia;
        TextView dataDesde;
        TextView quantidadePessoas;
        ImageView setaDetalhes;
        ViewPager2 imagensMoradia;

        public MoradiaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializando as views
            nomeMoradia = itemView.findViewById(R.id.house_name);
            dataDesde = itemView.findViewById(R.id.ad_date);
            quantidadePessoas = itemView.findViewById(R.id.people_qnt);
            setaDetalhes = itemView.findViewById(R.id.go_arrow);
            imagensMoradia = itemView.findViewById(R.id.house_img);
        }

    }
}
