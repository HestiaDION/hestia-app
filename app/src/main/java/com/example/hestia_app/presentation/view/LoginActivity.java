package com.example.hestia_app.presentation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hestia_app.R;

import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hestia_app.data.api.callbacks.TokenJwtCallback;
import com.example.hestia_app.data.services.TokenJwtService;
import com.example.hestia_app.domain.models.Token;
import com.example.hestia_app.presentation.view.swipe.PreviewScreensExplanation;
import com.example.hestia_app.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private static final String TOKEN_KEY = "token";
    Button loginButton;
    TextView cadastroRedirect;
    EditText email, senha;
    ImageButton eyeOpenedPassword;

    // Service
    TokenJwtService tokenJwtService = new TokenJwtService();

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
//            Intent intent = new Intent(LoginActivity.this, MainActivityNavbar.class);
//            startActivity(intent);
//            finish();
            autenticar.signOut();
        }

        cadastroRedirect.setOnClickListener(v -> {
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
                                    // gerar token
                                    tokenJwtService.getAccessToken(email.getText().toString(), new TokenJwtCallback() {
                                        @Override
                                        public void onSuccess(Token tokenResponse) {

                                            Log.d("TokenResponse", "onSuccess: " + tokenResponse.getToken());
                                            // colocar token no shared preferences
                                            getSharedPreferences("UserPreferences", MODE_PRIVATE)
                                                    .edit()
                                                    .putString(TOKEN_KEY, tokenResponse.getToken())
                                                    .apply();

                                            Log.d("TokenLogin", "onSuccess: " + getSharedPreferences("UserPreferences", MODE_PRIVATE).getString("token", ""));
                                            Toast.makeText(LoginActivity.this, "Token gerado com sucesso!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(LoginActivity.this, MainActivityNavbar.class);
                                            startActivity(intent);
                                        }
                                        @Override
                                        public void onFailure(String t) {
                                            Toast.makeText(LoginActivity.this, "Erro ao gerar token: " + t, Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                } else {
                                    // mostrar erro
                                    String msg = "Erro ao efetuar o login: ";
                                    try {
                                        throw Objects.requireNonNull(task.getException());
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

        ViewUtils.setEyeIconVisibilityAndChangeIconOnClick(senha, eyeOpenedPassword);


    }
}