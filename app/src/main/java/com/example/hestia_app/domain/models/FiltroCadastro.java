package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

public class FiltroCadastro {
    @SerializedName("cnome")
    private String nome;

    @SerializedName("ccategoria")
    private String categoria;

    public FiltroCadastro(){}

    public FiltroCadastro(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FiltroCadastro{");
        sb.append("nome='").append(nome).append('\'');
        sb.append(", categoria='").append(categoria).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
