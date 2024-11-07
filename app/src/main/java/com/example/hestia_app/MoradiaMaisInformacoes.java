package com.example.hestia_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.domain.models.Moradia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class MoradiaMaisInformacoes extends AppCompatActivity {

    TextView nomeMoradia, quantidadePessoas, universidadePerto, quantidadeQuartos, capacidadePessoas,
            descricao, contatoTitulo, regras;
    Button joinMoradia, entrarContato;
    ImageView goBack;
    String email;
    String nomeUniversitario;
    MoradiaService moradiaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moradia_mais_informacoes);

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();
        nomeUniversitario = user.getDisplayName();

        nomeMoradia = findViewById(R.id.nomeDaMoradia);
        quantidadePessoas = findViewById(R.id.quantidadePessoas);
        universidadePerto = findViewById(R.id.universidadePerto);
        quantidadeQuartos = findViewById(R.id.quantidadeQuartos);
        capacidadePessoas = findViewById(R.id.capacidadePessoas);
        descricao = findViewById(R.id.descricaoTexto);
        regras = findViewById(R.id.regrasTexto);

        joinMoradia = findViewById(R.id.btnAssociar);
        entrarContato = findViewById(R.id.btnEntrarContato);
        contatoTitulo = findViewById(R.id.contatoTitulo);

        goBack = findViewById(R.id.goBackArrow);
        goBack.setOnClickListener(v -> finish());

        // Verifica o tipo de usuário
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userOrigin = sharedPreferences.getString("user_origin", "");

        Log.d("OrigemUser", userOrigin);

        // Se for do tipo "anunciante", oculta os botões
        if ("anunciante".equals(userOrigin)) {
            joinMoradia.setVisibility(Button.GONE);
            entrarContato.setVisibility(Button.GONE);
            contatoTitulo.setVisibility(View.GONE);
        }

        moradiaService = new MoradiaService(getApplicationContext());
        String moradiaIdString = getIntent().getStringExtra("moradiaId");

        Log.d("moradiaId", moradiaIdString);

        // get de informações
        moradiaService.getMoradiaById(UUID.fromString(moradiaIdString), new MoradiaByIdCallback() {
            @Override
            public void onSuccess(Moradia moradias) {
                // populando campos da tela
                nomeMoradia.setText(moradias.getNomeCasa());
                quantidadePessoas.setText(String.valueOf(moradias.getQuantidadeMaximaPessoas()));
                universidadePerto.setText(moradias.getUniversidadeProxima());
                quantidadeQuartos.setText(String.valueOf(moradias.getQuantidadeQuartos()));
                capacidadePessoas.setText(String.valueOf(moradias.getQuantidadeMaximaPessoas()));
                descricao.setText(String.valueOf(moradias.getDescricao()));
                regras.setText(String.valueOf(moradias.getRegras()));

                email = moradias.getEmailAnunciante(); // Aqui o email é corretamente atribuído

                // Verifica o tipo de usuário
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                String userOrigin = sharedPreferences.getString("user_origin", "");

                Log.d("OrigemUser", userOrigin);

                // Se for do tipo "anunciante", oculta os botões
                if ("anunciante".equals(userOrigin)) {
                    joinMoradia.setVisibility(Button.GONE);
                    contatoTitulo.setVisibility(View.GONE);
                } else {
                    entrarContato.setOnClickListener(view -> showContactDialog(email)); // Coloca o click listener aqui
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("Mais Informações", "Falha no Get Moradia by ID");
            }
        });

        entrarContato.setOnClickListener(view -> showContactDialog(String.valueOf(email)));

    }

    private void showContactDialog(String emailAnunciante) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_entrar_em_contato);

        TextView emailTextView = dialog.findViewById(R.id.emailAnunciante);
        Button entrarContatoButton = dialog.findViewById(R.id.entrarEmContato);
        Button cancelarButton = dialog.findViewById(R.id.cancelarContato);

        // Define o email no TextView
        emailTextView.setText("Entrar em contato com o anunciante: \n" + emailAnunciante);

        // Configura o botão "Entrar em contato" para abrir o app de email
        entrarContatoButton.setOnClickListener(v -> {
            String subject = "Interesse na propriedade anunciada";
            String body = "Olá, estou interessado na propriedade que você anunciou no app Héstia.";

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setData(Uri.parse("mailto"));
            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{emailAnunciante});
            email.putExtra(Intent.EXTRA_SUBJECT,
                    "Sugestão: ");
            email.putExtra(Intent.EXTRA_TEXT, "Olá! Como vai?\nMe chamo " + nomeUniversitario + "! Encontrei o seu anúncio no aplicativo Héstia e fiquei muito interessado. Poderíamos conversar?");
            startActivity(Intent.createChooser(email, "ENVIAR E-MAIL"));

            if (email.resolveActivity(getPackageManager()) != null) {
                startActivity(email);
            } else {
                Log.e("DialogContato", "Não foi possível encontrar um app de email.");
            }
            dialog.dismiss();
        });

        // Configura o botão "Cancelar" para fechar o diálogo
        cancelarButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
