package com.example.hestia_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PagamentoEmAprovacaoDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pagamento_em_aprovacao, container, false);

        Button okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
