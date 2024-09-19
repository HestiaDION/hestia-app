package com.example.hestia_app.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.example.hestia_app.R;
import com.example.hestia_app.presentation.view.adapter.OnboardingAdapter;

import java.util.Calendar;
import java.util.Locale;

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
        // Inicialmente, configure o EditText para ocultar a senha
        senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eyeIcon.setImageResource(R.drawable.closed_eye_password_icon); // Defina o ícone do olho fechado inicialmente

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

    public static void setCalendarIconOnClick(EditText campo3, ImageButton calendar_icon, Context context) {
        // deixar o campo sem ser clicável
        campo3.setFocusable(false);
        campo3.setFocusableInTouchMode(false);
        campo3.setClickable(false);

        // deixar o ícone visível
        calendar_icon.setVisibility(View.VISIBLE);

        calendar_icon.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            if (!campo3.getText().toString().isEmpty()) {
                // setar para a data selecionada
                String[] dateParts = campo3.getText().toString().split("/");
                year = Integer.parseInt(dateParts[2]);
                month = Integer.parseInt(dateParts[1]) - 1;
                dayOfMonth = Integer.parseInt(dateParts[0]);
            }
            DatePickerDialog dialog = new DatePickerDialog(context, R.style.CustomDatePicker, null, year, month, dayOfMonth);

            // Definir a data máxima para que a pessoa tenha pelo menos 18 anos
            Calendar eighteenYearsAgo = Calendar.getInstance();
            eighteenYearsAgo.add(Calendar.YEAR, -18); // Subtrai 18 anos da data atual

            // Configurar a data máxima permitida (no máximo 18 anos atrás)
            dialog.getDatePicker().setMaxDate(eighteenYearsAgo.getTimeInMillis());

            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Handle the selected date
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    campo3.setText(selectedDate);
                }
            });

            dialog.show();
        });

    }

    public static void showGenderPopup(EditText campo4, ImageButton genero, Context context) {
        // deixar o campo sem ser clicável
        campo4.setFocusable(false);
        campo4.setFocusableInTouchMode(false);
        campo4.setClickable(false);

        // deixar o ícone visível
        genero.setVisibility(View.VISIBLE);

        genero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criando o PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, v);

                // Inflando o menu de opções
                popupMenu.getMenu().add("Masculino");
                popupMenu.getMenu().add("Feminino");
                popupMenu.getMenu().add("Outro");

                // Definindo ações ao selecionar uma opção
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String selectedGender = item.getTitle().toString();
                        campo4.setText(selectedGender);
                        return true;
                    }
                });

                // Exibindo o menu
                popupMenu.show();
            }
        });
    }
}
