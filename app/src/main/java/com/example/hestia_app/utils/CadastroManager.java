package com.example.hestia_app.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CadastroManager implements Parcelable {

    private static final int TOTAL_ETAPAS_ANUNCIANTE = 2;
    private static final int TOTAL_ETAPAS_UNIVERSITARIO = 4;
    private int etapaAtual;

    /**
     * Inicializa com a etapa sendo a primeira
     */
    public CadastroManager() {
        this.etapaAtual = 1; // Inicia na primeira etapa
    }

    /**
     * Método de navegação entre as etapas do anunciante
     */
    public boolean hasNextEtapaAnunciante() {
        return etapaAtual < TOTAL_ETAPAS_ANUNCIANTE;
    }

    /**
     * Método de navegação entre as etapas do universitário
     */
    public boolean hasNextEtapaUniversitario() {
        return etapaAtual < TOTAL_ETAPAS_UNIVERSITARIO;
    }

    /**
     * Verifica se a etapa atual é a última
     */
    public void nextEtapaAnunciante() {
        if (hasNextEtapaAnunciante()) {
            etapaAtual++;
        }
    }

    /**
     * Verifica se a etapa atual é a última
     */
    public void nextEtapaUniversitario() {
        if (hasNextEtapaUniversitario()) {
            etapaAtual++;
        }
    }

    /**
     * Retorna a etapa atual
     */
    public int getEtapaAtual() {
        return etapaAtual;
    }

    /**
     * Configura os campos de cada etapa do anunciante
     */
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

    /**
     * Configura os campos de cada etapa do anunciante
     */
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

    /**
     * Este é um construtor protegido que é usado para criar uma instância da classe CadastroManager a partir de um Parcel. Um Parcel é um contêiner para dados que pode ser usado para enviar informações entre componentes. Neste caso, ele está lendo um valor inteiro do Parcel (a etapa atual do cadastro) e atribuindo-o ao campo etapaAtual
     * @param in Parcel com os dados a serem lidos e atribuidos a este objeto
     */
    protected CadastroManager(Parcel in) {
        etapaAtual = in.readInt();
    }

    /**
     * Este é um objeto estático que implementa a interface Parcelable.Creator<CadastroManager>. Ele é responsável por criar novas instâncias de CadastroManager a partir de um Parcel ou por criar um array de objetos CadastroManager
     */
    public static final Creator<CadastroManager> CREATOR = new Creator<CadastroManager>() {
        /**
         * Cria um novo objeto CadastroManager a partir de um Parcel.
         * @param in The Parcel to read the object's data from.
         * @return Um novo CadastroManager
         */
        @Override
        public CadastroManager createFromParcel(Parcel in) {
            return new CadastroManager(in);
        }

        /**
         * Cria um novo array de objetos CadastroManager.
         * @param size O tamanho do array.
         */
        @Override
        public CadastroManager[] newArray(int size) {
            return new CadastroManager[size];
        }
    };

    /**
     * Este método faz parte da interface Parcelable e geralmente é utilizado para descrever algum conteúdo especial no Parcel, como um identificador de arquivo, mas na maioria dos casos, como aqui, ele simplesmente retorna 0. Se o objeto contém um descritor de arquivo, este método deve retornar CONTENTS_FILE_DESCRIPTOR
     * @return Um valor inteiro (geralmente 0).
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *  Este método é responsável por salvar os dados do objeto CadastroManager em um Parcel, para que ele possa ser transmitido para outro componente. No exemplo dado, ele está salvando o valor de etapaAtual no Parcel.
     * @param dest O Parcel onde os dados serão escritos.
     * @param flags Sinalizadores adicionais sobre como o objeto deve ser escrito (geralmente não usado, então 0 é passado).
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(etapaAtual);
    }
}
