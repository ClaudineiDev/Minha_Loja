package com.claudinei.minhaloja.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.claudinei.minhaloja.MainActivity;
import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Carrinho;
import com.claudinei.minhaloja.model.Produto;
import com.claudinei.minhaloja.model.Venda;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static java.lang.Double.parseDouble;


public class VendaActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView listView_v;
    EditText editTextSearchVenda;
    Button addCarrinho;
    Spinner pagamento, parcelamento;
    TextView qtdVenda, valorVendido, cliente;
    ProgressDialog loading;
    String idcliente = "";
    String nomecliente = "";
    SimpleAdapter adapter;
    Realm realm;
    int next_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        listView_v = (ListView) findViewById(R.id.lv_items);
        editTextSearchVenda = (EditText) findViewById(R.id.et_search);
        pagamento = (Spinner) findViewById(R.id.tipo_pagamento_v);
        parcelamento = (Spinner) findViewById(R.id.tipo_parcelamento_v);
        listView_v = (ListView) findViewById(R.id.lv_items);
        listView_v.setOnItemLongClickListener(this);

        realm = Realm.getDefaultInstance();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 69, 0)));
        actionbar.setTitle("Realizar venda");
        getWindow().setStatusBarColor(Color.parseColor("#FE0000"));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.venda_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.return_home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.vit_cliente) {
            Intent i = new Intent(this, ClienteListActivity.class);
            i.putExtra("venda", "queroCliente");
            startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculoCarrinho();

    }

    public void getItem(View view) {

        String idvenda = editTextSearchVenda.getText().toString();

        Produto buscarproduto = realm.where(Produto.class).equalTo("id_item", Integer.parseInt(idvenda)).findFirst();
        try {
            Integer idItem = buscarproduto.getId_item();
            String tipoItem = buscarproduto.getTipo();
            String itemName = buscarproduto.getDescricao();
            String marca = buscarproduto.getMarca();
            String preco = buscarproduto.getPreco_custo().toString();
            String vVenda = buscarproduto.getPreco_venda().toString();
            Double pLucro = parseDouble(buscarproduto.getPerc_lucro().toString());
            String tamanho = buscarproduto.getTamanho();
            String tipo_pag = pagamento.getSelectedItem().toString();
            String tipo_par = parcelamento.getSelectedItem().toString();
            String obs = buscarproduto.getObs();
            int qtd = 0;

            if (preco.equals("")) {
                preco = "0";
            }
            if (pLucro.toString().equals("")) {
                pLucro = 0.0;
            }
            if (buscarproduto.getQtd() > 0) {
                if (vVenda.equals("")) {
                    Toast.makeText(VendaActivity.this, "Produto sem preço de venda cadastrado.",
                            Toast.LENGTH_LONG).show();
                } else {
                    qtd = 1;

                    Carrinho carrinho = new Carrinho(1, Integer.toString(idItem), tipoItem, idcliente, itemName,
                            marca, parseDouble(preco), parseDouble(vVenda),
                            pLucro, tamanho, tipo_pag, tipo_par, qtd);

                    realm.beginTransaction();
                    realm.insert(carrinho);
                    realm.commitTransaction();
                    //realm.close();
                    calculoCarrinho();
                }
            } else {
                Toast.makeText(VendaActivity.this, "O estoque (" + itemName + ") está zerado (0)",
                        Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(VendaActivity.this, "Código procurado não existe no estoque.",
                    Toast.LENGTH_LONG).show();
        }
        editTextSearchVenda.setText("");
    }
    private int getNextIdVenda(){
        RealmResults<Venda> results = realm.where(Venda.class).findAll();
        if (results.isEmpty()){
            return 1;
        }
        return results.max("id_v").intValue() + 1;
    }

    private void calculoCarrinho() {
        addCarrinho = (Button) findViewById(R.id.btn_venda);
        qtdVenda = (TextView) findViewById(R.id.qtd_itens_v);
        valorVendido = (TextView) findViewById(R.id.total_valor_v);
        listVenda();
        RealmResults<Carrinho> results = realm.where(Carrinho.class).findAll();
        int sum_qtd = results.sum("qtd_venda_v").intValue();
        double sum_valor = results.sum("preco_venda_v").doubleValue();

        qtdVenda.setText("Total Itens: " + sum_qtd);
        valorVendido.setText("Total valor: R$ " + sum_valor);

    }

    private void listVenda() {
        Realm realm = Realm.getDefaultInstance();

        ArrayList<HashMap<String, String>> list_adapter = new ArrayList<>(); //lista para preencher o simpleAdapter
        List<Carrinho> list = realm.where(Carrinho.class).findAll();

        for (int i = 0; i < list.size(); i++) {

            HashMap<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(list.get(i).getId_item_v()));
            item.put("desc", list.get(i).getDescricao_v());
            item.put("marca", list.get(i).getMarca_v());
            item.put("preco", String.valueOf(list.get(i).getPreco_venda_v()));
            item.put("qtd", String.valueOf(list.get(i).getQtd_venda_v()));
            item.put("tamanho", list.get(i).getTamanho_v());

            list_adapter.add(item);
        }

        adapter = new SimpleAdapter(this, list_adapter, R.layout.row_list_item_carrinho,
                new String[]{"desc", "marca", "preco", "qtd", "tamanho", "id"},
                new int[]{R.id.txt_item_row_desc, R.id.txt_item_row_marca, R.id.txt_item_row_preco,
                        R.id.txt_item_row_qtd, R.id.txt_item_row_tam, R.id.txt_item_row_id});

        listView_v.setAdapter(adapter);


    }

    public void addVenda(View view) {

        List<Carrinho> listVerificar = realm.where(Carrinho.class).findAll();
        if (idcliente.equals("")) {
            Toast.makeText(VendaActivity.this, "Selecione o cliente", Toast.LENGTH_LONG).show();
            return;

        } else if (pagamento.getSelectedItem().toString().equals("Tipo de pagamento")) {
            Toast.makeText(VendaActivity.this, "Selecione o tipo de pagamento.", Toast.LENGTH_LONG).show();

            return;
        } else if (parcelamento.getSelectedItem().toString().equals("") &&
                pagamento.getSelectedItem().toString().equals("Crédito")) {
            Toast.makeText(VendaActivity.this, "Selecione o parcelamento.", Toast.LENGTH_LONG).show();
            return;
        } else if (listVerificar.size() == 0) {
            Toast.makeText(VendaActivity.this, "Selecione o item da venda.", Toast.LENGTH_LONG).show();
            return;
        }


        List<Carrinho> list = realm.where(Carrinho.class).findAll();
        next_id = getNextIdVenda();
        for (int i = 0; i < list.size(); i++) {
            Venda v = realm.where(Venda.class).sort("id", Sort.DESCENDING).findFirst();
            long id = v == null ? 1 : v.getId() + 1;

            final String idItem = list.get(i).getId_item_v();
            final String itemName = list.get(i).getDescricao_v();
            final String tipo = list.get(i).getTipo_v();
            final String marca = list.get(i).getMarca_v();
            final double preco = list.get(i).getPreco_custo_v();
            final double vVenda = list.get(i).getPreco_venda_v();
            final double pLucro = list.get(i).getPerc_lucro_v();
            final String tamanho = list.get(i).getTamanho_v();
            //final String id_cliente = idcliente;
            final String tipo_pagamento = pagamento.getSelectedItem().toString();
            final String tipo_parcelamento = parcelamento.getSelectedItem().toString();
            final int qtd = 1;
            // Criar um modo de inputar na tabela interna de venda se não hover internet
            Venda venda = new Venda(id, next_id, idItem, tipo, Long.parseLong(idcliente), itemName,
                    marca, preco, vVenda, pLucro, tamanho, tipo_pagamento, tipo_parcelamento, qtd);

            realm.beginTransaction();
            realm.insert(venda);
            realm.commitTransaction();

        }
        deleteCarrinho();

    }

    private void deleteCarrinho(){
        final RealmResults<Carrinho> results = realm.where(Carrinho.class).findAll();
        final AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Venda");
        msg.setMessage("Deseja finalizar a venda?");
        //define um botão como positivo
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                /*final ProgressDialog loading = ProgressDialog.show(VendaActivity.this,
                        "Finalizando", "Aguarde...");*/

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        results.deleteAllFromRealm();
                        Toast.makeText(VendaActivity.this, "Venda Finalizada!", Toast.LENGTH_LONG).show();
                        listVenda();
                    }
                });
               //
            }

        });

        msg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(VendaActivity.this, "Continue a venda!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alerta = msg.create();
        //Exibe
        alerta.show();

        //alerta.dismiss();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final String idItem = (String) ((TextView) view.findViewById(R.id.txt_item_row_id)).getText();
        final String valorItem = (String) ((TextView) view.findViewById(R.id.txt_item_row_preco)).getText();
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Alterar valor");
        itens.add("Remover");
        itens.add("Voltar");

        ArrayAdapter adapter = new ArrayAdapter(VendaActivity.this, R.layout.alertdialog_item_venda, itens);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                TextInputLayout textInputLayout = new TextInputLayout(VendaActivity.this);
                textInputLayout.setPadding(
                        getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin), // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                        0,
                        getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin),
                        0
                );
                final EditText input = new EditText(VendaActivity.this);
                textInputLayout.setHint("Valor atual R$ " + valorItem);
                textInputLayout.addView(input);
                if (which == 0) {
                    dialog.cancel();
                    AlertDialog.Builder alert = new AlertDialog.Builder(VendaActivity.this);
                    alert.setTitle("Valor do Produto?");
                    alert.setView(textInputLayout);
                    alert.setMessage("Deseja alterar o valor de venda para esse produto?");
                    alert.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Realm realmDelete = Realm.getDefaultInstance();
                            Carrinho vendaUpdate = realmDelete.where(Carrinho.class).equalTo("id_item_v", idItem).findFirst();
                            realmDelete.beginTransaction();
                            vendaUpdate.setPreco_venda_v(parseDouble(input.getText().toString()));
                            realmDelete.commitTransaction();
                            calculoCarrinho();
                        }
                    });
                    alert.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
                if (which == 1) {
                    dialog.cancel();
                    Realm realmDelete = Realm.getDefaultInstance();
                    Carrinho vendaDelete = realmDelete.where(Carrinho.class).equalTo("id_item_v", idItem).findFirst();
                    realmDelete.beginTransaction();
                    vendaDelete.deleteFromRealm();
                    realmDelete.commitTransaction();

                    calculoCarrinho();
                }
                if (which == 2) {
                    dialog.cancel();
                }
            }
        }).create();

        builder.show();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (requestCode == 1) {
            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = i != null ? i.getExtras() : null;
            if (params != null) {
                if (resultCode == 1) {
                    cliente = (TextView) findViewById(R.id.cliente_v);
                    idcliente = params.getString("id");
                    nomecliente = params.getString("nome");
                    cliente.setText("Cliente: " + nomecliente);
                }
            }
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.mover_esquerda, R.anim.desaparecendo);
    }
}
