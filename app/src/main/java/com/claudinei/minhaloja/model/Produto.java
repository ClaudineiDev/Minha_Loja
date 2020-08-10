package com.claudinei.minhaloja.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Produto extends RealmObject {
    @PrimaryKey
    private int id_item;
    private String tipo;
    private String descricao;
    private String marca;
    private Double preco_custo;
    private Double preco_venda;
    private Double perc_lucro;
    private String tamanho;
    //private String modelo;
    private String obs;
    private String foto;
    private int qtd;

    public Produto(){}
    public Produto(int id_item, String tipo, String descricao, String marca,
                   Double preco_custo, Double preco_venda, Double perc_lucro, String tamanho,
                   String obs, String foto, int qtd) {

        this.id_item = id_item;
        this.tipo = tipo;
        this.descricao = descricao;
        this.marca = marca;
        this.preco_custo = preco_custo;
        this.preco_venda = preco_venda;
        this.perc_lucro = perc_lucro;
        this.tamanho = tamanho;
        this.obs = obs;
        this.foto = foto;
        this.qtd = qtd;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPreco_custo() {
        return preco_custo;
    }

    public void setPreco_custo(Double preco_custo) {
        this.preco_custo = preco_custo;
    }

    public Double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(Double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public Double getPerc_lucro() {
        return perc_lucro;
    }

    public void setPerc_lucro(Double perc_lucro) {
        this.perc_lucro = perc_lucro;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }


}
