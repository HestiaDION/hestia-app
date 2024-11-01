package com.example.hestia_app.presentation.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.clients.RetrofitPostgresClient;
import com.example.hestia_app.data.api.callbacks.UsuarioCallback;
import com.example.hestia_app.data.api.repo.UsuarioRepository;
import com.example.hestia_app.domain.models.Usuario;
import com.example.hestia_app.databinding.ActivityMainNavbarBinding;
import com.example.hestia_app.presentation.fragments.ChatAnunciante;
import com.example.hestia_app.presentation.fragments.ChatUniversitario;
import com.example.hestia_app.presentation.fragments.HomeAnunciante;


import com.example.hestia_app.presentation.fragments.HomeUniversitario;
import com.example.hestia_app.presentation.fragments.PerfilAnunciante;
import com.example.hestia_app.presentation.fragments.PerfilUniversitario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import retrofit2.Call;

public class MainActivityNavbar extends AppCompatActivity {

    ActivityMainNavbarBinding binding;
    String origemUsuario;
    private final String ANUNCIANTE = "anunciante";
    private final String UNIVERSITARIO = "universitario";
    boolean isUserOriginFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainNavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String emailUsuario = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        fetchUserOrigin(emailUsuario, new UsuarioCallback() {
            @Override
            public void onUsuarioReceived(String origem) {
                origemUsuario = origem;
                isUserOriginFetched = true;
                Log.d("Origem", "Origem do usuário: " + origemUsuario);
                // Configurar o fragmento inicial com base na origem
                if (origemUsuario.equals(ANUNCIANTE)) {
                    replaceFragment(new HomeAnunciante());
                    updateIcons(binding.bottomNavbar.getMenu().findItem(R.id.nav_home).getItemId());
                } else if (origemUsuario.equals(UNIVERSITARIO)) {
                    replaceFragment(new HomeUniversitario());
                    updateIcons(binding.bottomNavbar.getMenu().findItem(R.id.nav_home).getItemId());

                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Origem", "Erro ao buscar origem: " + errorMessage);
            }
        });


        binding.bottomNavbar.setOnItemSelectedListener(item -> {
            if (isUserOriginFetched) {
                handleNavigationItemSelected(item.getItemId());
            } else {
                Toast.makeText(MainActivityNavbar.this, "Carregando dados do usuário, por favor aguarde...", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Método para lidar com a seleção de itens da navbar
    private void handleNavigationItemSelected(int itemId) {
        if (origemUsuario.equals(ANUNCIANTE)) {
            if (itemId == R.id.nav_chat) {
                replaceFragment(new ChatAnunciante());
            } else if (itemId == R.id.nav_home) {
                replaceFragment(new HomeAnunciante());
            } else if (itemId == R.id.nav_perfil) {
                replaceFragment(new PerfilAnunciante(origemUsuario));
            }

        } else if (origemUsuario.equals(UNIVERSITARIO)) {
            replaceFragment(new HomeUniversitario());
            if (itemId == R.id.nav_chat) {
                replaceFragment(new ChatUniversitario());
                binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_selected_icon);
            } else if (itemId == R.id.nav_home) {
                replaceFragment(new HomeUniversitario());
                binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_selected_icon);
            } else if (itemId == R.id.nav_perfil) {
                replaceFragment(new PerfilUniversitario(origemUsuario));
                binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_selected_icon);
            }
        }
        updateIcons(itemId);
    }

    // metodo para atualizar os ícones da navbar
    private void updateIcons(int selectedItemId) {
        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_not_selected_icon);
        binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_not_selected_icon);
        binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_not_selected_icon);

        if (selectedItemId == R.id.nav_chat) {
            binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_selected_icon);
        } else if (selectedItemId == R.id.nav_home) {
            binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_selected_icon);
        } else if (selectedItemId == R.id.nav_perfil) {
            binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_selected_icon);
        }
    }

    // Método para trocar o fragmento
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Chamada para verificação do tipo do usuário
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
