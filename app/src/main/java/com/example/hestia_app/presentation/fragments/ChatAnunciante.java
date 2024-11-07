package com.example.hestia_app.presentation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hestia_app.R;

public class ChatAnunciante extends Fragment {

    public ChatAnunciante() {
    }

    public static ChatAnunciante newInstance(String param1, String param2) {
        return new ChatAnunciante();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_anunciante, container, false);
        showDevelopmentDialog();
        return view;
    }

    private void showDevelopmentDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pagamento_em_aprovacao, null);

        TextView messageTextView = dialogView.findViewById(R.id.emailAnunciante);
        messageTextView.setText("Esta área ainda está em desenvolvimento.");

        // Cria o diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        dialog.show();
    }
}
