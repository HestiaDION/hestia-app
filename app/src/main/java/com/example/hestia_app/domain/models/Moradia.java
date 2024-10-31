package com.example.hestia_app.domain.models;

public class Moradia {

    private String emailAnunciante;
    private String quantidadeMaximaPessoas;
    private String dataRegistro;
    private String nomeCasa;
    private double aluguel;
    private String descricao;
    private String regras;
    private String quantidadeQuartos;
    private String universidadeProxima;
    private String cep;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;

    public Moradia(String emailAnunciante, String quantidadeMaximaPessoas, String dataRegistro, String nomeCasa,
                   double aluguel, String descricao, String regras, String quantidadeQuartos, String universidadeProxima,
                   String cep, String cidade, String bairro, String rua, String numero, String complemento) {

        this.emailAnunciante = emailAnunciante;
        this.quantidadeMaximaPessoas = quantidadeMaximaPessoas;
        this.dataRegistro = dataRegistro;
        this.nomeCasa = nomeCasa;
        this.aluguel = aluguel;
        this.descricao = descricao;
        this.regras = regras;
        this.quantidadeQuartos = quantidadeQuartos;
        this.universidadeProxima = universidadeProxima;
        this.cep = cep;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    public String getEmailAnunciante() {
        return emailAnunciante;
    }

    public void setEmailAnunciante(String emailAnunciante) {
        this.emailAnunciante = emailAnunciante;
    }

    public String getQuantidadeMaximaPessoas() {
        return quantidadeMaximaPessoas;
    }

    public void setQuantidadeMaximaPessoas(String quantidadeMaximaPessoas) {
        this.quantidadeMaximaPessoas = quantidadeMaximaPessoas;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getNomeCasa() {
        return nomeCasa;
    }

    public void setNomeCasa(String nomeCasa) {
        this.nomeCasa = nomeCasa;
    }

    public double getAluguel() {
        return aluguel;
    }

    public void setAluguel(double aluguel) {
        this.aluguel = aluguel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegras() {
        return regras;
    }

    public void setRegras(String regras) {
        this.regras = regras;
    }

    public String getQuantidadeQuartos() {
        return quantidadeQuartos;
    }

    public void setQuantidadeQuartos(String quantidadeQuartos) {
        this.quantidadeQuartos = quantidadeQuartos;
    }

    public String getUniversidadeProxima() {
        return universidadeProxima;
    }

    public void setUniversidadeProxima(String universidadeProxima) {
        this.universidadeProxima = universidadeProxima;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public String toString() {
        return "Moradia{" +
                "emailAnunciante='" + emailAnunciante + '\'' +
                ", quantidadeMaximaPessoas='" + quantidadeMaximaPessoas + '\'' +
                ", dataRegistro='" + dataRegistro + '\'' +
                ", nomeCasa='" + nomeCasa + '\'' +
                ", aluguel=" + aluguel +
                ", descricao='" + descricao + '\'' +
                ", regras='" + regras + '\'' +
                ", quantidadeQuartos='" + quantidadeQuartos + '\'' +
                ", universidadeProxima='" + universidadeProxima + '\'' +
                ", cep='" + cep + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", rua='" + rua + '\'' +
                ", numero=" + numero +
                ", complemento='" + complemento + '\'' +
                '}';
    }
}
