package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class InformacoesAdicionaisMoradia {
    @SerializedName("id_moradia")
    private UUID id_moradia;

    @SerializedName("topicos")
    private List<String> topicos;

    public InformacoesAdicionaisMoradia(UUID id_moradia, List<String> topicos) {
        this.id_moradia = id_moradia;
        this.topicos = topicos;
    }

    public UUID getId_moradia() {
        return id_moradia;
    }

    public void setId_moradia(UUID id_moradia) {
        this.id_moradia = id_moradia;
    }

    public List<String> getTopicos() {
        return topicos;
    }

    public void setTopicos(List<String> topicos) {
        this.topicos = topicos;
    }

    @Override
    public String toString() {
        return "InformacoesAdicionaisMoradia{" +
                "id_moradia=" + id_moradia +
                ", topicos=" + topicos +
                '}';
    }

}
