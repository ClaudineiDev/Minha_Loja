package com.claudinei.minhaloja.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.adapter.RecyclerViewAdapter;
import com.claudinei.minhaloja.model.Produto;
import com.claudinei.minhaloja.util.RecyclerItemClickListener;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class StockListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressDialog loading;
    EditText editTextSearchItem;
    Button btnlocalizar;
    Context context;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produto);

        context = getApplicationContext();
        actionBar();

        realm.init(this);
        getProdutosLocal();
        getProdutosLocal();
        btnlocalizar = (Button) findViewById(R.id.btn_localizar);
        btnlocalizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
    }

    private void actionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FE9A2E")));
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Estoque");
        getWindow().setStatusBarColor(Color.rgb(255, 69, 0));
    }

    /*@Override
    protected void onResume() {
        super.onResume();

    }*/

    private void getProdutosLocal() {

        realm = Realm.getDefaultInstance();

        List<Produto> lista_produto = realm.where(Produto.class).sort("id_item", Sort.DESCENDING).findAll();

        recyclerView = findViewById(R.id.lv_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(context, lista_produto);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Intent intent = new Intent(context, ItenDetailsActivity.class);
                                String idItem = (String) ((TextView) v.findViewById(R.id.txt_card_cod)).getText();
                                intent.putExtra("id", idItem.substring(8));
                                startActivityForResult(intent, 1);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (requestCode == 1) {
            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = i != null ? i.getExtras() : null;
            if (params != null) {
                String msg = params.getString("detailActivity");
                //getProdutosLocal();
                if (resultCode == 2) {
                    if (msg.equals("Sem Alterações")) {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        getProdutosLocal();
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
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
