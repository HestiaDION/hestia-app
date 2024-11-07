package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hestia_app.MoradiaMaisInformacoes;
import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.services.ImagensMoradiaService;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.domain.models.ImagensMoradia;
import com.example.hestia_app.domain.models.Moradia;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoradiaHomeAdapter extends RecyclerView.Adapter<MoradiaHomeAdapter.MoradiaViewHolder> {

    private Context context;
    private List<Moradia> moradiaList;
    MoradiaService moradiaService;

    public MoradiaHomeAdapter(Context context, List<Moradia> moradiaList) {
        this.context = context;
        this.moradiaList = moradiaList;
    }

    @NonNull
    @Override
    public MoradiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_moradia_item, parent, false);
        moradiaService = new MoradiaService(context);
        return new MoradiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoradiaViewHolder holder, int position) {
        Moradia moradia = moradiaList.get(position);
        Handler handler;
        Runnable runnable;

        holder.nomeMoradia.setText(moradia.getNomeCasa());
        holder.dataDesde.setText(moradia.getDataRegistro());
        int quantidadePessoas = moradia.getQuantidadeMaximaPessoas();

        String texto;
        if (quantidadePessoas > 1) {
            texto = quantidadePessoas + " pessoas";
        } else {
            texto = quantidadePessoas + " pessoa";
        }
        holder.quantidadePessoas.setText(texto);

        final List<String>[] imageList = new List[]{new ArrayList<>()};

        Log.d("moradiaAdapter", "onBindViewHolder: " + moradia);
        Log.d("moradiaAdapter", "onBindViewHolder: " + moradia.getId());

        // pegar as imagens do mongo e repetir a requisição até que de certo
        ImagensMoradiaService imagensMoradiaService = new ImagensMoradiaService();
        imagensMoradiaService.getImagensMoradias(moradia.getId(), new ImagensMoradiaCallback() {
            @Override
            public void onSuccess(ImagensMoradia response) {
                Log.d("Imagens", "onSuccess: " + response.getImagens());
                imageList[0] = response.getImagens();
                // Configure o ViewPager2 com o adapter de imagens
                HouseImgAdapter houseImgAdapter = new HouseImgAdapter(context, imageList[0]);
                holder.imagensMoradia.setAdapter(houseImgAdapter);
                houseImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("imagens", "onFailure: " + t.getMessage());
            }
        });

        holder.setaDetalhes.setOnClickListener(v -> {
            Intent intent = new Intent(context, MoradiaMaisInformacoes.class);
            intent.putExtra("moradiaId", moradia.getId().toString());
            Log.d("MoradiaId", moradia.getId().toString());
            context.startActivity(intent);
        });

        // Inicializa o Handler e o Runnable para mudar as imagens automaticamente
        handler = new Handler();
        runnable = new Runnable() {
            int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < imageList[0].size()) {
                    holder.imagensMoradia.setCurrentItem(currentIndex++, true);
                } else {
                    currentIndex = 0; // Reinicia para o primeiro item
                }
                handler.postDelayed(this, 5000); // Altera a imagem a cada 3 segundos
            }
        };
        handler.post(runnable); // Inicia o processo
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
