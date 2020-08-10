package com.claudinei.minhaloja.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.claudinei.minhaloja.MainActivity;
import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Carrinho;
import com.claudinei.minhaloja.model.Produto;
import com.claudinei.minhaloja.model.Venda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class VendaListActivity extends AppCompatActivity {
    SimpleAdapter adapter;
    Realm realm;
    ListView listView_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_list);

        listView_v = (ListView) findViewById(R.id.lv_list_vendas);
        realm = Realm.getDefaultInstance();
        listVenda();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.venda_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void listVenda() {
        ArrayList<HashMap<String, String>> list_adapter = new ArrayList<>();
        RealmResults<Venda> results = realm.where(Venda.class).findAll();

        String valid = "";
        for (int i = 0; i < results.size(); i++) {

            HashMap<String, String> item = new HashMap<>();
            if(!valid.equals(String.valueOf(results.get(i).getId_v()))) {
                item.put("id_v", String.valueOf(results.get(i).getId_v()));
                item.put("itens", String.valueOf(results.where()
                        .equalTo("id_v", results.get(i).getId_v()).count()));
                item.put("valorVendido", results.where()
                        .equalTo("id_v", results.get(i).getId_v())
                        .sum("preco_venda_v").toString());

                list_adapter.add(item);
            }
            valid = String.valueOf(results.get(i).getId_v());
        }

        adapter = new SimpleAdapter(this, list_adapter, R.layout.row_list_vendas,
                new String[]{"id_v", "itens", "valorVendido"},
                new int[]{R.id.txt_list_vendas_id, R.id.txt_list_vendas_itens_count, R.id.txt_list_vendas_valor_vendido});
        listView_v.setAdapter(adapter);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.mover_esquerda, R.anim.desaparecendo);
    }
}