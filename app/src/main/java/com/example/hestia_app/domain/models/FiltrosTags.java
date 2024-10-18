package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class FiltrosTags {
    @SerializedName("idUsuarioMoradia")
    private UUID idUsuarioMoradia;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("animais_estimacao")
    private List<String> animais_estimacao;

    @SerializedName("preferencia_genero")
    private String preferencia_genero;

    @SerializedName("numero_maximo_pessoas")
    private String numero_maximo_pessoas;

    @SerializedName("frequencia_fumo")
    private String frequencia_fumo;

    @SerializedName("frequencia_bebida")
    private String frequencia_bebida;

    @SerializedName("preferencias_moveis_outro")
    private List<String> preferencias_moveis_outro;

    public FiltrosTags() {}

    public FiltrosTags(UUID idUsuarioMoradia,String tipo, List<String> animais_estimacao, String preferencia_genero, String numero_maximo_pessoas, String frequencia_fumo, String frequencia_bebida, List<String> preferencias_moveis_outro) {
        this.idUsuarioMoradia = idUsuarioMoradia;
        this.tipo = tipo;
        this.animais_estimacao = animais_estimacao;
        this.preferencia_genero = preferencia_genero;
        this.numero_maximo_pessoas = numero_maximo_pessoas;
        this.frequencia_fumo = frequencia_fumo;
        this.frequencia_bebida = frequencia_bebida;
        this.preferencias_moveis_outro = preferencias_moveis_outro;
    }

    public FiltrosTags(UUID idUsuarioMoradia, String tipo, List<String> animais_estimacao, String preferencia_genero, String numero_maximo_pessoas, String frequencia_fumo, String frequencia_bebida) {
        this.idUsuarioMoradia = idUsuarioMoradia;
        this.tipo = tipo;
        this.animais_estimacao = animais_estimacao;
        this.preferencia_genero = preferencia_genero;
        this.numero_maximo_pessoas = numero_maximo_pessoas;
        this.frequencia_fumo = frequencia_fumo;
        this.frequencia_bebida = frequencia_bebida;
    }

    public UUID getIdUsuarioMoradia() {
        return idUsuarioMoradia;
    }

    public void setIdUsuarioMoradia(UUID idUsuarioMoradia) {
        this.idUsuarioMoradia = idUsuarioMoradia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getAnimais_estimacao() {
        return animais_estimacao;
    }

    public void setAnimais_estimacao(List<String> animais_estimacao) {
        this.animais_estimacao = animais_estimacao;
    }

    public String getPreferencia_genero() {
        return preferencia_genero;
    }

    public void setPreferencia_genero(String preferencia_genero) {
        this.preferencia_genero = preferencia_genero;
    }

    public String getNumero_maximo_pessoas() {
        return numero_maximo_pessoas;
    }

    public void setNumero_maximo_pessoas(String numero_maximo_pessoas) {
        this.numero_maximo_pessoas = numero_maximo_pessoas;
    }

    public String getFrequencia_fumo() {
        return frequencia_fumo;
    }

    public void setFrequencia_fumo(String frequencia_fumo) {
        this.frequencia_fumo = frequencia_fumo;
    }

    public String getFrequencia_bebida() {
        return frequencia_bebida;
    }

    public void setFrequencia_bebida(String frequencia_bebida) {
        this.frequencia_bebida = frequencia_bebida;
    }

    public List<String> getPreferencias_moveis_outro() {
        return preferencias_moveis_outro;
    }

    public void setPreferencias_moveis_outro(List<String> preferencias_moveis_outro) {
        this.preferencias_moveis_outro = preferencias_moveis_outro;
    }

    @Override
    public String toString() {
        return "FiltrosTags{" +
                "idUsuarioMoradia='" + idUsuarioMoradia + '\'' +
                ", tipo=" + tipo +
                ", animais_estimacao=" + animais_estimacao +
                ", preferencia_genero=" + preferencia_genero +
                ", numero_maximo_pessoas=" + numero_maximo_pessoas +
                ", frequencia_fumo='" + frequencia_fumo + '\'' +
                ", frequencia_bebida='" + frequencia_bebida + '\'' +
                ", preferencias_moveis_outro=" + preferencias_moveis_outro +
                '}';
    }
}
