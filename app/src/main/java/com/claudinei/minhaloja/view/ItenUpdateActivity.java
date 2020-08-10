package com.claudinei.minhaloja.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Produto;


import io.realm.Realm;

public class ItenUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtDesc, edtMarca, edtVvenda, edtPreco, edtTamanho, edtObs, edtPLucro;
    TextView txtUpId;
    Spinner spTipo;
    Button buttonUpdateItem, buttonDeleteItem;
    String idUp, descUp, marcaUp, tipoUp, vVendaUp, precoUp, tamanhoUp, obsUp, pLucroUp;
    Realm realm;
    Produto produto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Alterações");

        Intent intent = getIntent();
        idUp = intent.getStringExtra("id");

        realm = Realm.getDefaultInstance();

        produto = realm.where(Produto.class).equalTo("id_item", Integer.parseInt(idUp)).findFirst();

        tipoUp = produto.getTipo();
        descUp = produto.getDescricao();
        marcaUp = produto.getMarca();
        precoUp = produto.getPreco_custo().toString();
        tamanhoUp = produto.getTamanho();
        //pLucroUp = intent.getStringExtra("pLucro");
        vVendaUp = produto.getPreco_venda().toString();
        //modeloUp = produto.getModelo();
        obsUp = produto.getObs();

        txtUpId = (TextView) findViewById(R.id.tv_up_id);
        spTipo = (Spinner) findViewById(R.id.sp_up_tipo);
        edtDesc = (EditText) findViewById(R.id.edt_up_desc);
        edtMarca = (EditText) findViewById(R.id.edt_up_marca);
        edtPreco = (EditText) findViewById(R.id.edt_up_custo);
        //edtPLucro = (EditText) findViewById(R.id.edt_up_pLucro);
        edtVvenda = (EditText) findViewById(R.id.edt_up_vVenda);
        edtTamanho = (EditText) findViewById(R.id.edt_up_tam);
        //edtModelo = (EditText) findViewById(R.id.edt_up_modelo);
        edtObs = (EditText) findViewById(R.id.edt_up_obs);

        buttonDeleteItem = (Button) findViewById(R.id.btn_deletar);
        buttonUpdateItem = (Button) findViewById(R.id.btn_alterar_up);

        txtUpId.setText("Código: " + idUp);
        edtDesc.setText(descUp);
        Resources res = getResources();
        String[] tipo = res.getStringArray(R.array.item_tipo);
        for(int i = 0; i < tipo.length; ++i){
            String t = tipo[i].substring(0, 3);
            if(t.equals(tipoUp)){
                spTipo.setSelection(i);
            }
        }
        edtMarca.setText(marcaUp);
        edtVvenda.setText(vVendaUp);
        edtPreco.setText(precoUp);
        //edtPLucro.setText(pLucroUp);
        edtTamanho.setText(tamanhoUp);
        //edtModelo.setText(modeloUp);
        edtObs.setText(obsUp);

        buttonUpdateItem.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent();
            String idItem = idUp;
            intent.putExtra("id", idItem);
            setResult(3, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateItem() {
        final String id = idUp;
        final String tipo = spTipo.getSelectedItem().toString();
        final String desc = edtDesc.getText().toString().trim();
        final String marca = edtMarca.getText().toString().trim();
        final String preco = edtPreco.getText().toString().trim();
        //final String pLucro = edtPLucro.getText().toString().trim();
        final String vVenda = edtVvenda.getText().toString().trim();
        final String tamanho = edtTamanho.getText().toString().trim();
        //final String modelo = edtModelo.getText().toString().trim();
        final String obs = edtObs.getText().toString();
        realm.beginTransaction();
        produto.setTipo(tipo);
        produto.setDescricao(desc);
        produto.setMarca(marca);
        produto.setPreco_custo(Double.parseDouble(preco));
        produto.setPreco_venda(Double.parseDouble(vVenda));
        produto.setTamanho(tamanho);
        //produto.setModelo(modelo);
        produto.setObs(obs);
        realm.commitTransaction();

        Toast.makeText(this, "Item Alterado", Toast.LENGTH_LONG).show();
        Intent intentUp = new Intent(this, ItenDetailsActivity.class);
        String idItem = idUp;
        intentUp.putExtra("id", idItem);
        startActivity(intentUp);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUpdateItem) {
            /*if (!Conexao.isConnected(this)) {
                Toast.makeText(this, "Sem conexão com a INTERNET", Toast.LENGTH_LONG).show();
                return;
            } else {

            }*/
            updateItem();
        }
    }
}