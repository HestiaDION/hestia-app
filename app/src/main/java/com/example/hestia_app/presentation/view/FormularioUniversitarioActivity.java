package com.example.hestia_app.presentation.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ProgressBar;


import androidx.appcompat.app.AppCompatActivity;

import com.example.hestia_app.R;
import com.example.hestia_app.data.services.FormularioUniversitarioService;
import com.example.hestia_app.domain.models.FormularioUniversitario;
import com.example.hestia_app.domain.models.ProbabilityResponse;

public class FormularioUniversitarioActivity extends AppCompatActivity {

    private RadioGroup radioDNE, radioInstituicao, radioFrequenciaUso, radioConfianca, radioMudancaResidencia, radioAlojamento, radioRendaMensal;
    private EditText etIdade;
    private Button btnEnviar;

    // Service
    FormularioUniversitarioService formularioUniversitarioService = new FormularioUniversitarioService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_universitario);

        radioDNE = findViewById(R.id.radioDNE);
        radioInstituicao = findViewById(R.id.radioInstituicao);
        radioFrequenciaUso = findViewById(R.id.radioFrequenciaUso);
        radioConfianca = findViewById(R.id.radioConfianca);
        radioMudancaResidencia = findViewById(R.id.radioMudancaResidencia);
        radioAlojamento = findViewById(R.id.radioAlojamento);
        radioRendaMensal = findViewById(R.id.radioRendaMensal);
        etIdade = findViewById(R.id.etIdade);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Coletar dados
                String idade = etIdade.getText().toString();

                String possuiDNE = getSelectedRadioText(radioDNE);
                String tipoInstituicao = getSelectedRadioText(radioInstituicao);
                String frequenciaUso = getSelectedRadioText(radioFrequenciaUso);
                String confianca = getSelectedRadioText(radioConfianca);
                String mudancaResidencia = getSelectedRadioText(radioMudancaResidencia);
                String alojamento = getSelectedRadioText(radioAlojamento);
                String rendaMensal = getSelectedRadioText(radioRendaMensal);

                if (idade.isEmpty() || possuiDNE == null || tipoInstituicao == null ||
                        frequenciaUso == null || confianca == null || mudancaResidencia == null || alojamento == null || rendaMensal == null) {
                    Toast.makeText(FormularioUniversitarioActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {

                    showLoadingDialog();

                    FormularioUniversitario formularioUniversitario = new FormularioUniversitario(tipoInstituicao, possuiDNE, frequenciaUso, confianca, mudancaResidencia, alojamento, idade, rendaMensal);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ProbabilityResponse result = formularioUniversitarioService.enviarFormularioUniversitario(formularioUniversitario);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result != null) {
                                        dismissLoadingDialog(result); // Chamar dismissLoadingDialog com o resultado
                                    } else {
                                        Toast.makeText(FormularioUniversitarioActivity.this, "Erro ao obter a resposta.", Toast.LENGTH_SHORT).show();
                                        dismissLoadingDialog(null); // Passar null se houver erro
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

    }

    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton radioButton = findViewById(selectedId);
            return radioButton.getText().toString();
        }
        return null;
    }

    private Dialog loadingDialog;

    private void showLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_probabilidade);
        loadingDialog.setCancelable(false);

        // Obter referências aos elementos do layout
        ProgressBar progressBar = loadingDialog.findViewById(R.id.progressBar);
        TextView tvProbabilidade = loadingDialog.findViewById(R.id.tvProbabilidade);
        Button btnOk = loadingDialog.findViewById(R.id.btnOk);

        // Configura a visibilidade dos elementos
        tvProbabilidade.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        loadingDialog.show();
    }

    private void dismissLoadingDialog(ProbabilityResponse response) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            TextView tvProbabilidade = loadingDialog.findViewById(R.id.tvProbabilidade);
            Button btnOk = loadingDialog.findViewById(R.id.btnOk);

            // Aqui você deve adaptar para o método correto de ProbabilityResponse
            tvProbabilidade.setText(String.valueOf(response.getProbability()));
            tvProbabilidade.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);

            // Configurar o botão "OK" para fechar o dialog
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.dismiss();
                }
            });
        }
    }
}
