package com.lima.checklist.model;

import java.util.Objects;

public class CheckListMensal {



    private String dataEntrega;

    private String proxima;

    private String nomeFuncExame;

    private Funcionarios funcionario;

    private String horaPartida;
    private String horaChegada;

    private String id;
    private String oleo;
    private String agua;
    private String bateria;
    private String hidraulica;
    private String elevacao;
   //rivate String statusExame;


    public CheckListMensal() {
    }

    public String getProxima() {
        return proxima;
    }

    public void setProxima(String proxima) {
        this.proxima = proxima;
    }

    public String getHoraPartida() {
        return horaPartida;
    }

    public void setHoraPartida(String horaPartida) {
        this.horaPartida = horaPartida;
    }

    public String getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(String horaChegada) {
        this.horaChegada = horaChegada;
    }

    public Funcionarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionarios funcionario) {
        this.funcionario = funcionario;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }



    public String getNomeFuncExame() {
        return nomeFuncExame;
    }

    public void setNomeFuncExame(String nomeFuncExame) {
        this.nomeFuncExame = nomeFuncExame;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOleo() {
        return oleo;
    }

    public void setOleo(String oleo) {
        this.oleo = oleo;
    }

    public String getAgua() {
        return agua;
    }

    public void setAgua(String agua) {
        this.agua = agua;
    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public String getHidraulica() {
        return hidraulica;
    }

    public void setHidraulica(String hidraulica) {
        this.hidraulica = hidraulica;
    }

    public String getElevacao() {
        return elevacao;
    }

    public void setElevacao(String elevacao) {
        this.elevacao = elevacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckListMensal that = (CheckListMensal) o;
        return Objects.equals(dataEntrega, that.dataEntrega) && Objects.equals(proxima, that.proxima) && Objects.equals(nomeFuncExame, that.nomeFuncExame) && Objects.equals(funcionario, that.funcionario) && Objects.equals(horaPartida, that.horaPartida) && Objects.equals(horaChegada, that.horaChegada) && Objects.equals(id, that.id) && Objects.equals(oleo, that.oleo) && Objects.equals(agua, that.agua) && Objects.equals(bateria, that.bateria) && Objects.equals(hidraulica, that.hidraulica) && Objects.equals(elevacao, that.elevacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataEntrega, proxima, nomeFuncExame, funcionario, horaPartida, horaChegada, id, oleo, agua, bateria, hidraulica, elevacao);
    }

    @Override
    public String toString() {
        return
                "Funcionário  : " + nomeFuncExame;


    }
}
