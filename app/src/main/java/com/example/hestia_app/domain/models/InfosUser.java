package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

public class InfosUser {

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;

    @SerializedName("urlFoto")
    private String urlFoto;


    public InfosUser(String email, String senha, String urlFoto) {
        this.email = email;
        this.senha = senha;
        this.urlFoto = urlFoto;
    }

    public InfosUser(String email, String urlFoto) {
        this.email = email;
        this.urlFoto = urlFoto;
    }
    public InfosUser(String urlFoto){
        this.urlFoto = urlFoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "InfosUser{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }
}
