package com.example.hestia_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.example.hestia_app.data.api.callbacks.ImagensMoradiaCallback;
import com.example.hestia_app.data.api.callbacks.ListUniversitariosCallback;
import com.example.hestia_app.data.api.callbacks.MoradiaByIdCallback;
import com.example.hestia_app.data.services.ImagensMoradiaService;
import com.example.hestia_app.data.services.MoradiaService;
import com.example.hestia_app.data.services.UniversitarioMoradiaService;
import com.example.hestia_app.domain.models.ImagensMoradia;
import com.example.hestia_app.domain.models.Member;
import com.example.hestia_app.domain.models.Moradia;
import com.example.hestia_app.domain.models.Universitario;
import com.example.hestia_app.presentation.view.adapter.HouseImgAdapter;
import com.example.hestia_app.presentation.view.adapter.MemberAdapter;
import com.example.hestia_app.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoradiaMaisInformacoes extends AppCompatActivity {

    TextView nomeMoradia, quantidadePessoas, universidadePerto, quantidadeQuartos, capacidadePessoas,
            descricao, contatoTitulo, regras;
    Button joinMoradia, entrarContato;
    ImageView goBack;
    String email;
    String nomeUniversitario;
    MoradiaService moradiaService;
    RecyclerView recyclerView;
    MemberAdapter memberAdapter;
    UniversitarioMoradiaService universitarioMoradiaService;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moradia_mais_informacoes);
        universitarioMoradiaService = new UniversitarioMoradiaService(this);

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

        viewPager2 = findViewById(R.id.viewPager);

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

        // contexto da lista de membros  da moradia
        recyclerView = findViewById(R.id.memberList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Member> memberList = new ArrayList<>();
        memberAdapter = new MemberAdapter(this, memberList);


        // Chama o método do serviço para obter os universitários
        universitarioMoradiaService.getUniversitariosByImovelId(moradiaIdString, new ListUniversitariosCallback() {
            @Override
            public void onListSuccess(List<Universitario> universitarios) {
                // Atualiza a lista de membros no adaptador
                for (int i = 0; i < universitarios.size(); i++) {

                    String dataNascimento = universitarios.get(i).getDt_nascimento();
                    int idade = ViewUtils.calcularIdade(dataNascimento);

                    Member membro = new Member(universitarios.get(i).getNome(), universitarios.get(i).getGenero(), String.valueOf(idade));
                    memberList.add(membro);
                }
            }


            @Override
            public void onListError(Exception e) {
                // Trate o erro aqui (ex: mostre uma mensagem para o usuário)
                Log.e("Membros", e.getMessage());
                Toast.makeText(MoradiaMaisInformacoes.this, "Erro ao carregar membros", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.setAdapter(memberAdapter);

        Log.d("MoradiaId", UUID.fromString(moradiaIdString).toString());

        UUID moradiaId = UUID.fromString(moradiaIdString);
        // get de informações
        moradiaService.getMoradiaById(moradiaId, new MoradiaByIdCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Moradia moradias) {
                // populando campos da tela
                nomeMoradia.setText(moradias.getNomeCasa());
                quantidadePessoas.setText(String.valueOf(moradias.getQuantidadeMaximaPessoas()) + " pessoas");
                universidadePerto.setText("Perto de " + moradias.getUniversidadeProxima());
                quantidadeQuartos.setText(String.valueOf(moradias.getQuantidadeQuartos()) + " quartos");
                capacidadePessoas.setText("Capacidade de " + moradias.getQuantidadeMaximaPessoas() + " pessoas");
                descricao.setText(String.valueOf(moradias.getDescricao()));
                regras.setText(String.valueOf(moradias.getRegras()));

                // pegar as fotos das moradias
                List<String> imageList = new ArrayList<>();
                // pegar as imagens do mongo e repetir a requisição até que de certo
                ImagensMoradiaService imagensMoradiaService = new ImagensMoradiaService();
                imagensMoradiaService.getImagensMoradias(moradias.getId(), new ImagensMoradiaCallback() {
                    @Override
                    public void onSuccess(ImagensMoradia response) {
                        Log.d("Imagens", "onSuccess: " + response.getImagens());
                        imageList.addAll(response.getImagens());
                        // Configure o ViewPager2 com o adapter de imagens
                        HouseImgAdapter houseImgAdapter = new HouseImgAdapter(MoradiaMaisInformacoes.this, imageList);
                        viewPager2.setAdapter(houseImgAdapter);
                        houseImgAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("imagens", "onFailure: " + t.getMessage());
                    }
                });

                email = moradias.getEmailAnunciante(); // Aqui o email é corretamente atribuído

                // Verifica o tipo de usuário
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                String userOrigin = sharedPreferences.getString("user_origin", "");

                Log.d("OrigemUser", userOrigin);


                if ("anunciante".equals(userOrigin)) {
                    joinMoradia.setVisibility(Button.GONE);
                    contatoTitulo.setVisibility(View.GONE);
                } else {
                    entrarContato.setOnClickListener(view -> showContactDialog(email));
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
