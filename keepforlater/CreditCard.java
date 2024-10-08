package com.example.app_mensa;

import java.sql.Date;

/**
 * informazioni sulla carta di credito
 * verranno mantenute solamente in locale
 */
public class CreditCard {
    private String owner;
    private String number;
    private Date expiration;
    private String cvv;

    public CreditCard(String owner, String number, Date expiration, String cvv) {
        this.owner = owner;
        this.number = number;
        this.expiration = expiration;
        this.cvv = cvv;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
