package com.example.hestia_app.presentation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hestia_app.R;

import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hestia_app.presentation.view.swipe.PreviewScreensExplanation;
import com.example.hestia_app.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView cadastroRedirect;
    EditText email, senha;

    ImageButton eyeOpenedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        cadastroRedirect = findViewById(R.id.cadastroRedirect);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.password);
        eyeOpenedPassword = findViewById(R.id.ver_senha);

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();

        if (user != null) {
            autenticar.signOut();
            Intent intent = new Intent(LoginActivity.this, MainActivityNavbar.class);
            startActivity(intent);
            finish();
        }

        cadastroRedirect.setOnClickListener(v -> {
            // abrir main mandando os parâmetros
            Intent intent = new Intent(LoginActivity.this, PreviewScreensExplanation.class);
            startActivity(intent);
        });
      
        loginButton.setOnClickListener(v -> {
            if (email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                // autenticar
                autenticar.signInWithEmailAndPassword(email.getText().toString(),senha.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                    // abrir tela inicial

                                    Intent intent = new Intent(LoginActivity.this, MainActivityNavbar.class);
                                    startActivity(intent);
                                } else {
                                    // mostrar erro
                                    String msg = "Erro ao efetuar o login: ";
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException user) {
                                        msg += "\nE-mail inválido!";
                                    } catch (FirebaseAuthInvalidCredentialsException senha) {
                                        msg += "\nSenha inválida!";
                                    } catch (Exception e) {
                                        msg += "\nErro genérico: " + e.getMessage();
                                    }
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // verificação para abrir o ícone de "olho"
        ViewUtils.setEyeIconVisibilityAndChangeIconOnClick(senha, eyeOpenedPassword);


    }
}