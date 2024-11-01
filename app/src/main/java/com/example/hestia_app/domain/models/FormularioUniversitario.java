package com.example.hestia_app.domain.models;

import com.google.gson.annotations.SerializedName;

public class FormularioUniversitario {


    @SerializedName("tipo_de_instituicao")
    private String tipoInstituicao;

    @SerializedName("possui_dne")
    private String possuiDNE;

    @SerializedName("frequencia_de_uso_de_apps_de_moradia")
    private String frequenciaUso;

    @SerializedName("confianca_em_avaliacoes_de_outros_usuarios")
    private String confianca;

    @SerializedName("mudanca_de_residencia")
    private String mudancaResidencia;

    @SerializedName("faculdade_possui_alojamento")
    private String alojamentoFaculdade;

    @SerializedName("idade")
    private String idade;

    @SerializedName("renda_mensal")
    private String rendaMensal;

    public FormularioUniversitario(String tipoInstituicao, String possuiDNE, String frequenciaUso, String confianca,
                                   String mudancaResidencia, String alojamentoFaculdade, String idade, String rendaMensal) {
        this.tipoInstituicao = tipoInstituicao;
        this.possuiDNE = possuiDNE;
        this.frequenciaUso = frequenciaUso;
        this.confianca = confianca;
        this.mudancaResidencia = mudancaResidencia;
        this.alojamentoFaculdade = alojamentoFaculdade;
        this.idade = idade;
        this.rendaMensal = rendaMensal;
    }

    public String getTipoInstituicao() {
        return tipoInstituicao;
    }

    public void setTipoInstituicao(String tipoInstituicao) {
        this.tipoInstituicao = tipoInstituicao;
    }

    public String getPossuiDNE() {
        return possuiDNE;
    }

    public void setPossuiDNE(String possuiDNE) {
        this.possuiDNE = possuiDNE;
    }

    public String getFrequenciaUso() {
        return frequenciaUso;
    }

    public void setFrequenciaUso(String frequenciaUso) {
        this.frequenciaUso = frequenciaUso;
    }

    public String getConfianca() {
        return confianca;
    }

    public void setConfianca(String confianca) {
        this.confianca = confianca;
    }

    public String getMudancaResidencia() {
        return mudancaResidencia;
    }

    public void setMudancaResidencia(String mudancaResidencia) {
        this.mudancaResidencia = mudancaResidencia;
    }

    public String getAlojamentoFaculdade() {
        return alojamentoFaculdade;
    }

    public void setAlojamentoFaculdade(String alojamentoFaculdade) {
        this.alojamentoFaculdade = alojamentoFaculdade;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(String rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    @Override
    public String toString() {
        return "FormularioUniversitario{" +
                "tipoInstituicao='" + tipoInstituicao + '\'' +
                ", possuiDNE='" + possuiDNE + '\'' +
                ", frequenciaUso='" + frequenciaUso + '\'' +
                ", confianca='" + confianca + '\'' +
                ", mudancaResidencia='" + mudancaResidencia + '\'' +
                ", alojamentoFaculdade='" + alojamentoFaculdade + '\'' +
                ", idade='" + idade + '\'' +
                ", rendaMensal='" + rendaMensal + '\'' +
                '}';
    }
}
