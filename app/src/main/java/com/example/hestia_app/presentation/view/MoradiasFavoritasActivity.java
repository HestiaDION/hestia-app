package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.GetUUIDByEmailCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaFavoritaCallback;
import com.example.hestia_app.data.services.MoradiaFavoritaService;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.data.services.UniversitarioService;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.domain.models.MoradiaFavorita;
import com.example.hestia_app.presentation.view.adapter.MoradiasFavoritasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoradiasFavoritasActivity extends AppCompatActivity {
    ImageButton voltar;
    RecyclerView recycleViewFavorita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moradias_favoritas);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycleViewFavorita = findViewById(R.id.recycleViewFavorita);

        recycleViewFavorita.setLayoutManager(new LinearLayoutManager(this));

        // Exemplo de dados. No caso real, você obteria esses dados do backend ou banco de dados.
        List<Moradia> moradiaList = new ArrayList<>();

        // pegar do banco
        MoradiaFavoritaService service = new MoradiaFavoritaService();
        UniversitarioService universitarioService = new UniversitarioService();
        MoradiaService moradiaService = new MoradiaService();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        universitarioService.getUniversitarioId(user.getEmail(), new GetUUIDByEmailCallback() {
            @Override
            public void onGetUUIDByEmailSuccess(String uuid) {
                service.getMoradiasFavoritas(UUID.fromString(uuid), new MoradiaFavoritaCallback() {
                    @Override
                    public void moradiaFavoritaOnSuccess(MoradiaFavorita moradiaFavorita) {
                        for (UUID id : moradiaFavorita.getMoradias_ids()) {
                            moradiaService.getMoradiaById(id, new MoradiaByIdCallback() {
                                @Override
                                public void onSuccess(Moradia moradias) {
                                    moradiaList.add(moradias);
                                    Log.d("moradiaList", "onSuccess: " + moradiaList);
                                    
                                    MoradiasFavoritasAdapter adapter = new MoradiasFavoritasAdapter(MoradiasFavoritasActivity.this, moradiaList);
                                    recycleViewFavorita.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.d("moradiaFavorita", "onFailure: " + message);
                                }
                            });
                        }
                    }

                    @Override
                    public void moradiaFavoritaOnFailure(String message) {
                        Log.d("moradiaFavorita", "onFailure: " + message);
                    }
                });
            }

            @Override
            public void onGetUUIDByEmailFailure(String erroMessage) {
                Log.d("moradiaFavorita", "onFailure: " + erroMessage);
            }
        });

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(v -> finish());
    }
}