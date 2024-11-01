package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class ImagensMoradia {
    @SerializedName("id_moradia")
    private UUID id_moradia;

    @SerializedName("imagens")
    private List<String> imagens;


    public ImagensMoradia(UUID id_moradia, List<String> imagens) {
        this.id_moradia = id_moradia;
        this.imagens = imagens;
    }

    public UUID getId_moradia() {
        return id_moradia;
    }

    public void setId_moradia(UUID id_moradia) {
        this.id_moradia = id_moradia;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ImagensMoradia{");
        sb.append("id_moradia=").append(id_moradia);
        sb.append(", imagens=").append(imagens);
        sb.append('}');
        return sb.toString();
    }
}
