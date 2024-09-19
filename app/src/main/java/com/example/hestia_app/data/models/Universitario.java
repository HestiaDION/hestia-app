package com.example.hestia_app.data.models;

import java.util.Date;
import java.util.UUID;

public class Universitario {
    private UUID id;
    private String nome;
    private Date dt_nascimento;
    private String dne;
    private String bio;
    private String login_id;
    private String cidade;
    private String universidade;

    public Universitario() {}

    public Universitario(UUID id, String nome, Date dt_nascimento, String dne, String bio, String login_id, String cidade, String universidade) {
        this.id = id;
        this.nome = nome;
        this.dt_nascimento = dt_nascimento;
        this.dne = dne;
        this.bio = bio;
        this.login_id = login_id;
        this.cidade = cidade;
        this.universidade = universidade;
    }

    public Universitario(String nome, Date dt_nascimento, String dne, String login_id, String cidade, String universidade) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.dt_nascimento = dt_nascimento;
        this.dne = dne;
        this.login_id = login_id;
        this.cidade = cidade;
        this.universidade = universidade;
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

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
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
                ", login_id='" + login_id + '\'' +
                ", cidade='" + cidade + '\'' +
                ", universidade='" + universidade + '\'' +
                '}';
    }
}
