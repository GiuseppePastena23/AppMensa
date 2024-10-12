package com.example.app_mensa.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String status;
    private String telefono;
    private float credito;
    private String tmpCode;

    public User() {}

    public User(int id, String nome, String cognome, String cf, String email, String password, String telefono, String status, float credito, String tmpCode) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.status = status;
        this.credito = credito;
        this.tmpCode = tmpCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getCredito() {
        return credito;
    }

    public void setCredito(float credito) {
        this.credito = credito;
    }

    public String getTmpCode() {
        return tmpCode;
    }

    public void setTmpCode(String tmpCode) {
        this.tmpCode = tmpCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", telefono='" + telefono + '\'' +
                ", credito=" + credito +
                ", tmpCode='" + tmpCode + '\'' +
                '}';
    }

    public static User createFromJSON(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String cf = json.getString("cf");
        String nome = json.getString("nome");
        String cognome = json.getString("cognome");
        String email = json.getString("email");
        String password = json.getString("password");
        String status = json.getString("status");
        String telefono = json.getString("telefono");
        float credito = (float) json.getDouble("credito");
        String tmpCode = json.optString("tmpCode", null);

        return new User(id, nome, cognome, cf, email, password, telefono, status, credito, tmpCode);
    }
}
