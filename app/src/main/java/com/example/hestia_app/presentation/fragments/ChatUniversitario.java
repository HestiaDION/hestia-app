package com.example.hestia_app.presentation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hestia_app.R;

public class ChatUniversitario extends Fragment {

    public ChatUniversitario() {
        // Required empty public constructor
    }

    public static ChatUniversitario newInstance(String param1, String param2) {
        ChatUniversitario fragment = new ChatUniversitario();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_universitario, container, false);
        showDevelopmentDialog();
        return view;
    }

    private void showDevelopmentDialog() {
        // Infla o layout do diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pagamento_em_aprovacao, null);

        // Personaliza o texto da TextView para a mensagem desejada
        TextView messageTextView = dialogView.findViewById(R.id.emailAnunciante);
        messageTextView.setText("Esta área ainda está em desenvolvimento.");

        // Cria o diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // Configura o botão "Entendido" para fechar o fragmento
        Button okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            dialog.dismiss(); // Fecha o diálogo
            requireActivity().getSupportFragmentManager().popBackStack(); // Retorna ao fragmento anterior
        });

        dialog.show();
    }
}
