package com.example.hestia_app.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CadastroManager implements Parcelable {

    private static final int TOTAL_ETAPAS_ANUNCIANTE = 2;
    private static final int TOTAL_ETAPAS_UNIVERSITARIO = 3;
    private int etapaAtual;

    public CadastroManager() {
        this.etapaAtual = 1; // Inicia na primeira etapa
    }

    // Métodos de navegação entre etapas
    public boolean hasNextEtapaAnunciante() {
        return etapaAtual < TOTAL_ETAPAS_ANUNCIANTE;
    }

    public boolean hasNextEtapaUniversitario() {
        return etapaAtual < TOTAL_ETAPAS_UNIVERSITARIO;
    }

    public void nextEtapaAnunciante() {
        if (hasNextEtapaAnunciante()) {
            etapaAtual++;
        }
    }

    public void nextEtapaUniversitario() {
        if (hasNextEtapaUniversitario()) {
            etapaAtual++;
        }
    }

    public int getEtapaAtual() {
        return etapaAtual;
    }

    // Métodos para obter campos das etapas
    public String[] getCamposDaEtapaAnunciante() {
        switch (etapaAtual) {
            case 1:
                return new String[]{"Nome Completo", "Cidade", "Data de Nascimento", "E-mail"};
            case 2:
                return new String[]{"Telefone", "Senha", "Confirmação de Senha", null};
            default:
                return new String[]{};
        }
    }

    public String[] getCamposDaEtapaUniversitario() {
        switch (etapaAtual) {
            case 1:
                return new String[]{"Nome Completo", "Cidade", "Data de Nascimento", null};
            case 2:
                return new String[]{"E-mail", "Telefone", "Sua Universidade (opcional)", null};
            case 3:
                return new String[]{"Documento Nacional do Estudante", "Senha", "Confirmação de Senha", null};
            default:
                return new String[]{};
        }
    }

    public String getEtapaString() {
        return "etapa" + etapaAtual;
    }

    // Métodos necessários para Parcelable
    protected CadastroManager(Parcel in) {
        etapaAtual = in.readInt();
    }

    public static final Creator<CadastroManager> CREATOR = new Creator<CadastroManager>() {
        @Override
        public CadastroManager createFromParcel(Parcel in) {
            return new CadastroManager(in);
        }

        @Override
        public CadastroManager[] newArray(int size) {
            return new CadastroManager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(etapaAtual);
    }
}
