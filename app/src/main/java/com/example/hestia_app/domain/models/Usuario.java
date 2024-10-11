package com.example.hestia_app.domain.models;

public class Usuario {

    private String email;
    private String origem;

    public Usuario(String email, String origem) {
        this.email = email;
        this.origem = origem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
