package com.example.hestia_app.domain.models;

import com.example.hestia_app.utils.ViewUtils;
import com.google.gson.annotations.SerializedName;

public class Anunciante {

    @SerializedName("nome")
    private String nome;

    @SerializedName("municipio")
    private String municipio;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("genero")
    private String genero;

    @SerializedName("dt_nascimento")
    private String dt_nascimento;

    @SerializedName("email")
    private String email;

    @SerializedName("bio")
    private String bio;

    public Anunciante(String nome, String municipio, String telefone, String genero, String dt_nascimento, String email) {
        this.nome = nome;
        this.municipio = municipio;
        this.telefone = ViewUtils.formatarTelefone(telefone);
        this.genero = genero;
        this.dt_nascimento = ViewUtils.formatarData(dt_nascimento);
        this.email = email;
    }

   public Anunciante(String nome, String bio){
        this.nome = nome;
        this.bio = bio;
   }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String cidade) {
        this.municipio = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(String dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "Anunciante{" +
                "nome='" + nome + '\'' +
                ", municipio='" + municipio + '\'' +
                ", telefone='" + telefone + '\'' +
                ", genero='" + genero + '\'' +
                ", dt_nascimento='" + dt_nascimento + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
