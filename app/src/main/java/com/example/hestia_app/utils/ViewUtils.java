package com.example.hestia_app.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.adapter.OnboardingAdapter;

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


    /**
     * Configura um TextWatcher para mostrar ou esconder o ícone de "olhinho"
     * com base no conteúdo do EditText. Além disso, define o clique do ícone de
     * "olhinho" para aberto ou fechado.
     *
     * @param context - contexto da aplicação.
     * @param layoutOnboardingIndicators - LinearLayout que contém os ícones de onboarding.
     * @param onboardingAdapter - Adapter de onboarding.
     */

    public static void setupOnboardingIndicators(Context context, LinearLayout layoutOnboardingIndicators, @NonNull OnboardingAdapter onboardingAdapter) {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(context);
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(
                            context,
                            R.drawable.onboarding_indicator_inactive
                    )
            );

            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    /**
     * Função de manusear o indicador de onboarding atual, assim alterando
     * o seu ícone de acordo com a tela atual da sequência.
     *
     * @param index - tela em que a sequência começa (no caso, 0)
     * @param context - contexto da aplicação.
     * @param layoutOnboardingIndicators - LinearLayout que contém os ícones de onboarding.
     * @param onboardingAdapter - Adapter de onboarding.
     */

    public static void setCurrentOnboardingIndicator(Context context, int index, LinearLayout layoutOnboardingIndicators, OnboardingAdapter onboardingAdapter, Button buttonNext) {
        int childCount = layoutOnboardingIndicators.getChildCount();

        for (int i = 0; i < childCount; i++) {

            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.onboarding_indicate_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if (index == onboardingAdapter.getItemCount() - 1) {
            buttonNext.setText("Começar");
        } else {
            buttonNext.setText("Próximo");
        }
    }
}
