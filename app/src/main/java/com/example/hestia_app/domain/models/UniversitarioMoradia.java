package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

public class UniversitarioMoradia {

    private String id;

    @SerializedName("universitario_id")
    private String universitarioId;

    @SerializedName("imovel_id")
    private String imovelId;

    public UniversitarioMoradia(String universitarioId, String imovelId) {
        this.universitarioId = universitarioId;
        this.imovelId = imovelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniversitarioId() {
        return universitarioId;
    }

    public void setUniversitarioId(String universitarioId) {
        this.universitarioId = universitarioId;
    }

    public String getImovelId() {
        return imovelId;
    }

    public void setImovelId(String imovelId) {
        this.imovelId = imovelId;
    }

    @Override
    public String toString() {
        return "UniversitarioMoradia{" +
                "universitarioId='" + universitarioId + '\'' +
                ", imovelId='" + imovelId + '\'' +
                '}';
    }
}
