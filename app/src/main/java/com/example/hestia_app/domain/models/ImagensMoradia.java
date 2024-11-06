package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class ImagensMoradia {
    @SerializedName("idMoradia")
    private UUID idMoradia;

    @SerializedName("imagens")
    private List<String> imagens;


    public ImagensMoradia(UUID idMoradia, List<String> imagens) {
        this.idMoradia = idMoradia;
        this.imagens = imagens;
    }

    public UUID getIdMoradia() {
        return idMoradia;
    }

    public void setIdMoradia(UUID idMoradia) {
        this.idMoradia = idMoradia;
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
        sb.append("id_moradia=").append(idMoradia);
        sb.append(", imagens=").append(imagens);
        sb.append('}');
        return sb.toString();
    }
}
