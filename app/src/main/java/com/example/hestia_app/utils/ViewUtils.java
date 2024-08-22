package com.example.hestia_app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.hestia_app.R;

public class ViewUtils {

    /**
     * Configura um TextWatcher para mostrar ou esconder o ícone de "olhinho"
     * com base no conteúdo do EditText. Além disso, define o clique do ícone de
     * "olhinho" para aberto ou fechado.
     *
     * @param senha - O EditText que contém a senha.
     * @param eyeIcon      -    O ImageView que representa o ícone de "olhinho".
     */

    public static void setEyeIconVisibilityAndChangeIconOnClick(final EditText senha, final ImageButton eyeIcon) {
        senha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    eyeIcon.setVisibility(View.VISIBLE);
                } else {
                    eyeIcon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // mudando o ícone de "olhinho"
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            private boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.closed_eye_password_icon);
                } else {
                    senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.open_eye_password_icon);
                }
                isPasswordVisible = !isPasswordVisible;
                senha.setSelection(senha.getText().length());
            }
        });

    }

}
