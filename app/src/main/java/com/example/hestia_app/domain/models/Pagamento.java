package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

public class Pagamento {

    @SerializedName("email")
    private String emailUsuarioAssinante;

    @SerializedName("nome")
    private String nomeUsuarioAssinante;


    public Pagamento(String emailUsuarioAssinante, String nomeUsuarioAssinante) {
        this.emailUsuarioAssinante = emailUsuarioAssinante;
        this.nomeUsuarioAssinante = nomeUsuarioAssinante;
    }

    public String getEmailUsuarioAssinante() {
        return emailUsuarioAssinante;
    }

    public void setEmailUsuarioAssinante(String emailUsuarioAssinante) {
        this.emailUsuarioAssinante = emailUsuarioAssinante;
    }

    public String getNomeUsuarioAssinante() {
        return nomeUsuarioAssinante;
    }

    public void setNomeUsuarioAssinante(String nomeUsuarioAssinante) {
        this.nomeUsuarioAssinante = nomeUsuarioAssinante;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "emailUsuarioAssinante='" + emailUsuarioAssinante + '\'' +
                ", nomeUsuarioAssinante='" + nomeUsuarioAssinante + '\'' +
                '}';
    }
}
