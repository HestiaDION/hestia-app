package com.example.hestia_app.domain.models;

import com.example.hestia_app.utils.ViewUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Universitario {
    private UUID id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("dt_nascimento")
    private String dt_nascimento;

    @SerializedName("dne")
    private String dne;

    @SerializedName("bio")
    private String bio;

    @SerializedName("municipio")
    private String municipio;

    @SerializedName("universidade")
    private String universidade;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("genero")
    private String genero;

    @SerializedName("email")
    private String email;

    // TODO: plano_id


    public Universitario() {}
//
//    public Universitario(UUID id, String nome, String dt_nascimento, String dne, String bio, String genero, String cidade, String universidade, String telefone) {
//        this.id = id;
//        this.nome = nome;
//        this.dt_nascimento = dt_nascimento;
//        this.dne = dne;
//        this.bio = bio;
//        this.cidade = cidade;
//        this.universidade = universidade;
//        this.genero = genero;
//        this.telefone = telefone;
//    }

    // construtor para registro


    public Universitario(String nome, String email, String dne, String municipio,
                         String universidade, String genero, String telefone, String dtNascimento) {

      this.nome = nome;
      this.email = email;
        this.dne = dne;
        this.municipio = municipio;
        this.telefone = ViewUtils.formatarTelefone(telefone);
        this.universidade = universidade;
        this.genero = genero;
        this.dt_nascimento = ViewUtils.formatarData(dtNascimento);


    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(String dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public String getDne() {
        return dne;
    }

    public void setDne(String dne) {
        this.dne = dne;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUniversidade() {
        return universidade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }


    @Override
    public String toString() {
        return "Universitario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dt_nascimento='" + dt_nascimento + '\'' +
                ", dne='" + dne + '\'' +
                ", bio='" + bio + '\'' +
                ", municipio='" + municipio + '\'' +
                ", universidade='" + universidade + '\'' +
                ", telefone='" + telefone + '\'' +
                ", genero='" + genero + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
