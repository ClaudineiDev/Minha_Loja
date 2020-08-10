package com.claudinei.minhaloja.model;

//import io.realm.RealmObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cliente extends RealmObject {
    @PrimaryKey
    int id_cliente;
    String nome_cliente;
    String telefone_cliente;
    String obs_cliente;

    public Cliente(){}

    public Cliente(int id_cliente, String nome_cliente, String telefone_cliente, String obs_cliente) {
        this.id_cliente = id_cliente;
        this.nome_cliente = nome_cliente;
        this.telefone_cliente = telefone_cliente;
        this.obs_cliente = obs_cliente;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public String getTelefone_cliente() {
        return telefone_cliente;
    }

    public void setTelefone_cliente(String telefone_cliente) {
        this.telefone_cliente = telefone_cliente;
    }

    public String getObs_cliente() {
        return obs_cliente;
    }

    public void setObs_cliente(String obs_cliente) {
        this.obs_cliente = obs_cliente;
    }


}
