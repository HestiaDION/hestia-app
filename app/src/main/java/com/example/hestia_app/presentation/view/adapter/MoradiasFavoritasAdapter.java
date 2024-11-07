package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.util.Log;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.GetUUIDByEmailCallback;
import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaFavoritaCallback;
import com.example.hestia_app.data.services.ImagensMoradiaService;
import com.example.hestia_app.data.services.MoradiaFavoritaService;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.ImagensMoradia;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.domain.models.MoradiaFavorita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        View view = LayoutInflater.from(context).inflate(R.layout.activity_moradia_favorita_item, parent, false);
        return new MoradiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoradiaViewHolder holder, int position) {
        Moradia moradia = moradiaList.get(position);
        holder.cNmMoradia.setText(moradia.getNomeCasa());
        holder.qntPessoas.setText(moradia.getQuantidadeMaximaPessoas() + " pessoas");
        holder.iQntQuartos.setText(moradia.getQuantidadeQuartos() + " quartos");
        holder.iQntPessoasMax.setText("Capacidade de " + moradia.getQuantidadeMaximaPessoas() + " pessoas");
        holder.nValor.setText("R$" + moradia.getAluguel());

        final List<String>[] imageList = new List[]{new ArrayList<>()};
        // pegar as imagens do mongo e repetir a requisição até que de certo
        ImagensMoradiaService imagensMoradiaService = new ImagensMoradiaService();
        imagensMoradiaService.getImagensMoradias(moradia.getId(), new ImagensMoradiaCallback() {
            @Override
            public void onSuccess(ImagensMoradia response) {
                Log.d("Imagens", "onSuccess: " + response.getImagens());
                imageList[0] = response.getImagens();
                // Configure o ViewPager2 com o adapter de imagens
                HouseImgAdapter houseImgAdapter = new HouseImgAdapter(context, imageList[0]);
                holder.houseImg.setAdapter(houseImgAdapter);
                houseImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("imagens", "onFailure: " + t.getMessage());
            }
        });

        // Ações dos botões
        holder.infoButton.setOnClickListener(v -> {
            // Lógica para o botão "Mais informações"
        });

        holder.deleteButton.setOnClickListener(v -> {
            MoradiaFavoritaService service = new MoradiaFavoritaService();
            UniversitarioService universitarioService = new UniversitarioService(context);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            universitarioService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
                @Override
                public void onGetUUIDByEmailSuccess(String uuid) {
                    service.deleteMoradiasFavoritas(UUID.fromString(uuid), moradia.getId(), new MoradiaFavoritaCallback() {
                        @Override
                        public void moradiaFavoritaOnSuccess(MoradiaFavorita moradiaFavorita) {
                            Log.d("moradiaFavorita", "moradiaFavoritaOnSuccess: " + moradiaFavorita);

                            // Remover a moradia da lista
                            moradiaList.remove(moradia);  // Assumindo que "moradiasList" é a lista de moradias no Adapter

                            // Atualizar a lista no RecyclerView
                            notifyDataSetChanged();
                        }

                        @Override
                        public void moradiaFavoritaOnFailure(String message) {
                            Log.d("moradiaFavorita", "moradiaFavoritaOnFailure: " + message);
                        }
                    });
                }

                @Override
                public void onGetUUIDByEmailFailure(String erroMessage) {
                    Log.d("moradiaFavorita", "onGetUUIDByEmailFailure: " + erroMessage);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return moradiaList.size();
    }

    static class MoradiaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ViewPager2 houseImg;
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

