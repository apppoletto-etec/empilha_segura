package com.lima.checklist.model;

import java.util.Objects;

public class CheckListDiario {



    private String dataEntrega;

    private String nomeFuncExame;

    private Funcionarios funcionario;

    private String horaPartida;
    private String horaChegada;

    private String id;
    private String pneu;
    private String freios;
    private String farois;
    private String buzina;
    private String combustivel;
   //rivate String statusExame;


    public CheckListDiario() {
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

    public String getPneu() {
        return pneu;
    }

    public void setPneu(String pneu) {
        this.pneu = pneu;
    }

    public String getFreios() {
        return freios;
    }

    public void setFreios(String freios) {
        this.freios = freios;
    }

    public String getFarois() {
        return farois;
    }

    public void setFarois(String farois) {
        this.farois = farois;
    }

    public String getBuzina() {
        return buzina;
    }

    public void setBuzina(String buzina) {
        this.buzina = buzina;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckListDiario that = (CheckListDiario) o;
        return Objects.equals(dataEntrega, that.dataEntrega) && Objects.equals(nomeFuncExame, that.nomeFuncExame) && Objects.equals(funcionario, that.funcionario) && Objects.equals(horaPartida, that.horaPartida) && Objects.equals(horaChegada, that.horaChegada) && Objects.equals(id, that.id) && Objects.equals(pneu, that.pneu) && Objects.equals(freios, that.freios) && Objects.equals(farois, that.farois) && Objects.equals(buzina, that.buzina) && Objects.equals(combustivel, that.combustivel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataEntrega, nomeFuncExame, funcionario, horaPartida, horaChegada, id, pneu, freios, farois, buzina, combustivel);
    }

    @Override
    public String toString() {
        return
                "Funcionário  : " + nomeFuncExame;


    }
}
