package com.example.hestia_app.domain.models;

import java.util.List;

public class Moradia {
    private String nome;
    private String dataDesde;
    private int quantidadePessoas;
    private List<String> imageList; // Lista de IDs de imagens

    public Moradia(String nome, String dataDesde, int quantidadePessoas, List<String> imageList) {
        this.nome = nome;
        this.dataDesde = dataDesde;
        this.quantidadePessoas = quantidadePessoas;
        this.imageList = imageList;
    }

    public String getNome() {
        return nome;
    }

    public String getDataDesde() {
        return dataDesde;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public List<String> getImageList() {
        return imageList;
    }
}
