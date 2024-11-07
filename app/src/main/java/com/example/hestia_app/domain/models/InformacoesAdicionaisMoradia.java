package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class InformacoesAdicionaisMoradia {
    @SerializedName("idMoradia")
    private UUID idMoradia;

    @SerializedName("topicos")
    private List<String> topicos;

    public InformacoesAdicionaisMoradia(UUID idMoradia, List<String> topicos) {
        this.idMoradia = idMoradia;
        this.topicos = topicos;
    }

    public UUID getIdMoradia() {
        return idMoradia;
    }

    public void setIdMoradia(UUID idMoradia) {
        this.idMoradia = idMoradia;
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
                "id_moradia=" + idMoradia +
                ", topicos=" + topicos +
                '}';
    }

}
