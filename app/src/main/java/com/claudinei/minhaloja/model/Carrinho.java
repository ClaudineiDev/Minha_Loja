package com.claudinei.minhaloja.model;


import io.realm.RealmObject;

public class Carrinho extends RealmObject {

    long id_v;
    String id_item_v;
    String tipo_v;
    String id_cliente_v;
    String descricao_v;
    String marca_v;
    double preco_custo_v;
    double preco_venda_v;
    double perc_lucro_v;
    String tamanho_v;
    String tipo_pagamento_v;
    String parcelamento_v;
    int qtd_venda_v;

    public Carrinho(){}

    public Carrinho(long id_v, String id_item_v, String tipo_v, String id_cliente_v, String descricao_v,
                    String marca_v, double preco_custo_v, double preco_venda_v,
                    double perc_lucro_v, String tamanho_v, String tipo_pagamento_v,
                    String parcelamento_v, int qtd_venda_v) {
        this.id_v = id_v;
        this.id_item_v = id_item_v;
        this.tipo_v = tipo_v;
        this.id_cliente_v = id_cliente_v;
        this.descricao_v = descricao_v;
        this.marca_v = marca_v;
        this.preco_custo_v = preco_custo_v;
        this.preco_venda_v = preco_venda_v;
        this.perc_lucro_v = perc_lucro_v;
        this.tamanho_v = tamanho_v;
        this.tipo_pagamento_v = tipo_pagamento_v;
        this.parcelamento_v = parcelamento_v;
        this.qtd_venda_v = qtd_venda_v;
    }

    public long getId_v() {
        return id_v;
    }

    public void setId_v(long id_v) {
        this.id_v = id_v;
    }

    public String getId_item_v() {
        return id_item_v;
    }

    public void setId_item_v(String id_item_v) {
        this.id_item_v = id_item_v;
    }

    public String getTipo_v() {
        return tipo_v;
    }

    public void setTipo_v(String tipo_v) {
        this.tipo_v = tipo_v;
    }

    public String getId_cliente_v() {
        return id_cliente_v;
    }

    public void setId_cliente_v(String id_cliente_v) {
        this.id_cliente_v = id_cliente_v;
    }

    public String getDescricao_v() {
        return descricao_v;
    }

    public void setDescricao_v(String descricao_v) {
        this.descricao_v = descricao_v;
    }

    public String getMarca_v() {
        return marca_v;
    }

    public void setMarca_v(String marca_v) {
        this.marca_v = marca_v;
    }

    public double getPreco_custo_v() {
        return preco_custo_v;
    }

    public void setPreco_custo_v(double preco_custo_v) {
        this.preco_custo_v = preco_custo_v;
    }

    public double getPreco_venda_v() {
        return preco_venda_v;
    }

    public void setPreco_venda_v(double preco_venda_v) {
        this.preco_venda_v = preco_venda_v;
    }

    public double getPerc_lucro_v() {
        return perc_lucro_v;
    }

    public void setPerc_lucro_v(double perc_lucro_v) {
        this.perc_lucro_v = perc_lucro_v;
    }

    public String getTamanho_v() {
        return tamanho_v;
    }

    public void setTamanho_v(String tamanho_v) {
        this.tamanho_v = tamanho_v;
    }

    public String getTipo_pagamento_v() {
        return tipo_pagamento_v;
    }

    public void setTipo_pagamento_v(String tipo_pagamento_v) {
        this.tipo_pagamento_v = tipo_pagamento_v;
    }

    public String getParcelamento_v() {
        return parcelamento_v;
    }

    public void setParcelamento_v(String parcelamento_v) {
        this.parcelamento_v = parcelamento_v;
    }

    public int getQtd_venda_v() {
        return qtd_venda_v;
    }

    public void setQtd_venda_v(int qtd_venda_v) {
        this.qtd_venda_v = qtd_venda_v;
    }
}
