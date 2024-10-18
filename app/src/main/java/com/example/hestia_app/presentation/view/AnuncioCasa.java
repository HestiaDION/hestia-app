package com.example.hestia_app.presentation.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AnuncioCasa {
    private UUID uId;
    private String cNmMoradia;
    private String cCEP;
    private String cTipoMoradia;
    private int iNumMoradia;
    private String cRua;
    private String cBairro;
    private String cCidade;
    private String cUF;
    private int iQntQuartos;
    private int iQntPessoasMax;
    private BigDecimal nValor;
    private String cStatus;
    private LocalDate dDtInicio;
    private LocalDate dDtExpiracao;
    private String cComplemento;
    private String cRegras;
    private UUID uIdAnunciante;
    private UUID uIdBoost;

    private String houseImg;

    // construtor somente com as informações do card
    public AnuncioCasa(String cNmMoradia, int iQntQuartos, int iQntPessoasMax, BigDecimal nValor, String houseImg){
        this.cNmMoradia = cNmMoradia;
        this.iQntQuartos = iQntQuartos;
        this.iQntPessoasMax = iQntPessoasMax;
        this.nValor = nValor;
        this.houseImg = houseImg;
    }


    // Construtor completo
    public AnuncioCasa(UUID uId, String cNmMoradia, String cCEP, String cTipoMoradia, int iNumMoradia,
                       String cRua, String cBairro, String cCidade, String cUF, int iQntQuartos,
                       int iQntPessoasMax, BigDecimal nValor, String cStatus, LocalDate dDtInicio,
                       LocalDate dDtExpiracao, String cComplemento, String cRegras,
                       UUID uIdAnunciante, UUID uIdBoost, String houseImg) {
        this.uId = uId;
        this.cNmMoradia = cNmMoradia;
        this.cCEP = cCEP;
        this.cTipoMoradia = cTipoMoradia;
        this.iNumMoradia = iNumMoradia;
        this.cRua = cRua;
        this.cBairro = cBairro;
        this.cCidade = cCidade;
        this.cUF = cUF;
        this.iQntQuartos = iQntQuartos;
        this.iQntPessoasMax = iQntPessoasMax;
        this.nValor = nValor;
        this.cStatus = cStatus;
        this.dDtInicio = dDtInicio;
        this.dDtExpiracao = dDtExpiracao;
        this.cComplemento = cComplemento;
        this.cRegras = cRegras;
        this.uIdAnunciante = uIdAnunciante;
        this.uIdBoost = uIdBoost;
        this.houseImg = houseImg;
    }

    // Getters e Setters
    public UUID getuId() {
        return uId;
    }

    public void setuId(UUID uId) {
        this.uId = uId;
    }

    public String getcNmMoradia() {
        return cNmMoradia;
    }

    public void setcNmMoradia(String cNmMoradia) {
        this.cNmMoradia = cNmMoradia;
    }

    public String getcCEP() {
        return cCEP;
    }

    public void setcCEP(String cCEP) {
        this.cCEP = cCEP;
    }

    public String getcTipoMoradia() {
        return cTipoMoradia;
    }

    public void setcTipoMoradia(String cTipoMoradia) {
        this.cTipoMoradia = cTipoMoradia;
    }

    public int getiNumMoradia() {
        return iNumMoradia;
    }

    public void setiNumMoradia(int iNumMoradia) {
        this.iNumMoradia = iNumMoradia;
    }

    public String getcRua() {
        return cRua;
    }

    public void setcRua(String cRua) {
        this.cRua = cRua;
    }

    public String getcBairro() {
        return cBairro;
    }

    public void setcBairro(String cBairro) {
        this.cBairro = cBairro;
    }

    public String getcCidade() {
        return cCidade;
    }

    public void setcCidade(String cCidade) {
        this.cCidade = cCidade;
    }

    public String getcUF() {
        return cUF;
    }

    public void setcUF(String cUF) {
        this.cUF = cUF;
    }

    public int getiQntQuartos() {
        return iQntQuartos;
    }

    public void setiQntQuartos(int iQntQuartos) {
        this.iQntQuartos = iQntQuartos;
    }

    public int getiQntPessoasMax() {
        return iQntPessoasMax;
    }

    public void setiQntPessoasMax(int iQntPessoasMax) {
        this.iQntPessoasMax = iQntPessoasMax;
    }

    public BigDecimal getnValor() {
        return nValor;
    }

    public void setnValor(BigDecimal nValor) {
        this.nValor = nValor;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public LocalDate getdDtInicio() {
        return dDtInicio;
    }

    public void setdDtInicio(LocalDate dDtInicio) {
        this.dDtInicio = dDtInicio;
    }

    public LocalDate getdDtExpiracao() {
        return dDtExpiracao;
    }

    public void setdDtExpiracao(LocalDate dDtExpiracao) {
        this.dDtExpiracao = dDtExpiracao;
    }

    public String getcComplemento() {
        return cComplemento;
    }

    public void setcComplemento(String cComplemento) {
        this.cComplemento = cComplemento;
    }

    public String getcRegras() {
        return cRegras;
    }

    public void setcRegras(String cRegras) {
        this.cRegras = cRegras;
    }

    public UUID getuIdAnunciante() {
        return uIdAnunciante;
    }

    public void setuIdAnunciante(UUID uIdAnunciante) {
        this.uIdAnunciante = uIdAnunciante;
    }

    public UUID getuIdBoost() {
        return uIdBoost;
    }

    public void setuIdBoost(UUID uIdBoost) {
        this.uIdBoost = uIdBoost;
    }

    public String getHouseImg() {
        return houseImg;
    }

    public void setHouseImg(String houseImg) {
        this.houseImg = houseImg;
    }

    //Método toString
    @Override
    public String toString() {
        return "AnuncioCasa{" +
                "uId=" + uId +
                ", cNmMoradia='" + cNmMoradia + '\'' +
                ", cCEP='" + cCEP + '\'' +
                ", cTipoMoradia='" + cTipoMoradia + '\'' +
                ", iNumMoradia=" + iNumMoradia +
                ", cRua='" + cRua + '\'' +
                ", cBairro='" + cBairro + '\'' +
                ", cCidade='" + cCidade + '\'' +
                ", cUF='" + cUF + '\'' +
                ", iQntQuartos=" + iQntQuartos +
                ", iQntPessoasMax=" + iQntPessoasMax +
                ", nValor=" + nValor +
                ", cStatus='" + cStatus + '\'' +
                ", dDtInicio=" + dDtInicio +
                ", dDtExpiracao=" + dDtExpiracao +
                ", cComplemento='" + cComplemento + '\'' +
                ", cRegras='" + cRegras + '\'' +
                ", uIdAnunciante=" + uIdAnunciante +
                ", uIdBoost=" + uIdBoost +
                '}';
    }
}

