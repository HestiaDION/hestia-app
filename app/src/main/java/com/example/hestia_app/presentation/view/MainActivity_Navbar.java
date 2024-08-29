package com.example.hestia_app.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.hestia_app.presentation.fragments.Chat_anunciante;
import com.example.hestia_app.presentation.fragments.Home_anunciante;
import com.example.hestia_app.R;
import com.example.hestia_app.databinding.ActivityMainNavbarBinding;
import com.example.hestia_app.presentation.fragments.Perfil_anunciante;

public class MainActivity_Navbar extends AppCompatActivity {

    ActivityMainNavbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainNavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Abrindo a home primeiro
        replaceFragment(new Home_anunciante());
        binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_selected_icon);


        //Configurando a bottom navBar
        binding.bottomNavbar.setOnItemSelectedListener(
                item -> {

                    // Resetar todos os ícones
                    binding.bottomNavbar.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_not_selected_icon);
                    binding.bottomNavbar.getMenu().findItem(R.id.nav_chat).setIcon(R.drawable.chat_not_selected_icon);
                    binding.bottomNavbar.getMenu().findItem(R.id.nav_perfil).setIcon(R.drawable.person_not_selected_icon);


                    //substituindo os fragments
                    int id = item.getItemId();
                    if (id == R.id.nav_chat) {
                        item.setIcon(R.drawable.chat_selected_icon);
                        replaceFragment(new Chat_anunciante());
                    } else if (id == R.id.nav_home) {
                        item.setIcon(R.drawable.home_selected_icon);
                        replaceFragment(new Home_anunciante());
                    } else if (id == R.id.nav_perfil) {
                        item.setIcon(R.drawable.person_selected_icon);
                        replaceFragment(new Perfil_anunciante());
                    }
                    return true;
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
}