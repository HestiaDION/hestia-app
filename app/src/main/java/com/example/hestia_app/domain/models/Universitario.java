package com.example.hestia_app.domain.models;

import java.util.Date;
import java.util.UUID;

public class Universitario {
    private UUID id;
    private String nome;
    private Date dt_nascimento;
    private String dne;
    private String bio;
    private String cidade;
    private String universidade;
    private String telefone;
    // TODO: plano_id
    private String genero;

    public Universitario() {}

    public Universitario(UUID id, String nome, Date dt_nascimento, String dne, String bio, String genero, String cidade, String universidade, String telefone) {
        this.id = id;
        this.nome = nome;
        this.dt_nascimento = dt_nascimento;
        this.dne = dne;
        this.bio = bio;
        this.cidade = cidade;
        this.universidade = universidade;
        this.genero = genero;
        this.telefone = telefone;
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

    public Date getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(Date dt_nascimento) {
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
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
                ", dt_nascimento=" + dt_nascimento +
                ", dne='" + dne + '\'' +
                ", bio='" + bio + '\'' +
                ", cidade='" + cidade + '\'' +
                ", universidade='" + universidade + '\'' +
                '}';
    }
}
