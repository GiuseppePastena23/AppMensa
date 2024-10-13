package com.example.app_mensa.dao;

public class Transaction {
    private int id;
    private float importo;
    private String datetime;
    private String modalita;


    public Transaction(int id, float importo, String datetime, String modalita) {
        this.id = id;
        this.importo = importo;
        this.datetime = datetime;
        this.modalita = modalita;
    }


    public int getId() {
        return id;
    }

    public float getImporto() {
        return importo;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getModalita() {
        return modalita;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", importo=" + importo +
                ", datetime='" + datetime + '\'' +
                ", modalita='" + modalita + '\'' +
                '}';
    }
}