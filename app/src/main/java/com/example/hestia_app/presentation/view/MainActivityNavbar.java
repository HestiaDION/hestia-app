package com.example.hestia_app.presentation.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.hestia_app.R;
import com.example.hestia_app.data.api.RetrofitPostgresClient;
import com.example.hestia_app.data.api.UsuarioCallback;
import com.example.hestia_app.data.api.UsuarioRepository;
import com.example.hestia_app.data.models.Usuario;
import com.example.hestia_app.databinding.ActivityMainNavbarBinding;
import com.example.hestia_app.presentation.fragments.ChatAnunciante;
import com.example.hestia_app.presentation.fragments.HomeAnunciante;


import com.example.hestia_app.presentation.fragments.PerfilAnunciante;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import retrofit2.Call;

public class MainActivityNavbar extends AppCompatActivity {

    ActivityMainNavbarBinding binding;
    String origemUsuario = "";
    private final String ANUNCIANTE = "anunciante";
    private final String UNIVERSITARIO = "universitario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainNavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configurando a bottom navBar
        binding.bottomNavbar.setOnItemSelectedListener(
                item -> {
                    // verificar origem do usuário para assim saber a qual escopo redirecionar
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    String emailUsuario = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                    fetchUserOrigin(emailUsuario, new UsuarioCallback() {
                        @Override
                        public void onUsuarioReceived(String origem) {
                            origemUsuario = origem;
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("Origem", "Erro ao buscar origem: " + errorMessage);
                        }
                    });

                    if (origemUsuario.equalsIgnoreCase(ANUNCIANTE)){

                        Toast.makeText(MainActivityNavbar.this, "Origem: " + origemUsuario, Toast.LENGTH_SHORT).show();
                        //Abrindo a home primeiro
                        replaceFragment(new HomeAnunciante());
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_selected_icon);

                        // Resetar todos os ícones
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_not_selected_icon);
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_not_selected_icon);
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_not_selected_icon);


                        //substituindo os fragments
                        int id = item.getItemId();
                        if (id == R.id.nav_chat) {
                            item.setIcon(R.drawable.chat_selected_icon);
                            replaceFragment(new ChatAnunciante());
                        } else if (id == R.id.nav_home) {
                            item.setIcon(R.drawable.home_selected_icon);
                            replaceFragment(new HomeAnunciante());
                        } else if (id == R.id.nav_perfil) {
                            item.setIcon(R.drawable.person_selected_icon);
                            replaceFragment(new PerfilAnunciante());
                        }
                        return true;

                    } else if (origemUsuario.equalsIgnoreCase(UNIVERSITARIO)) {

                        // TODO: implementar fragment do universitário

                        Toast.makeText(MainActivityNavbar.this, "Origem: " + origemUsuario, Toast.LENGTH_SHORT).show();
                        //Abrindo a home primeiro
                        replaceFragment(new HomeAnunciante());
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_selected_icon);

                        // Resetar todos os ícones
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_not_selected_icon);
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_not_selected_icon);
                        binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_not_selected_icon);


                        //substituindo os fragments
                        int id = item.getItemId();
                        if (id == R.id.nav_chat) {
                            item.setIcon(R.drawable.chat_selected_icon);
                            replaceFragment(new ChatAnunciante());
                        } else if (id == R.id.nav_home) {
                            item.setIcon(R.drawable.home_selected_icon);
                            replaceFragment(new HomeAnunciante());
                        } else if (id == R.id.nav_perfil) {
                            item.setIcon(R.drawable.person_selected_icon);
                            replaceFragment(new PerfilAnunciante());
                        }
                        return true;

                    }
                    return false;

                }
        );
    }

    //método para mudar o fragmento na navbar
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    // chamada para verificação do tipo do usuário
    public void fetchUserOrigin(String email, UsuarioCallback callback) {
        UsuarioRepository usuarioRepository = RetrofitPostgresClient.getClient().create(UsuarioRepository.class);

        Call<Usuario> call = usuarioRepository.getUserOrigin(email);

        call.enqueue(new retrofit2.Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, retrofit2.Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String origem = response.body().getOrigem();
                    callback.onUsuarioReceived(origem);  // Retorna a string "origem" via callback
                } else {
                    callback.onFailure("Erro na resposta da API. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                callback.onFailure("Erro na chamada da API: " + t.getMessage());
            }
        });
    }



}