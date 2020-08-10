package com.claudinei.minhaloja.view;

import android.app.ProgressDialog;
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

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Cliente;

import io.realm.Realm;
import io.realm.Sort;

public class CadastroClienteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_nome, edt_telefone, edt_obs;
    private Button btn_salvar;
    private Realm realm;
    String retorno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        realm = Realm.getDefaultInstance();

        initialize();

        //actionBar();

        Intent intent = getIntent();
        retorno = intent.getStringExtra("ida");

    }

/*    private void actionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#424242")));
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar .setTitle("Cadastro de Item");
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
    }*/

    private void initialize() {
        edt_nome = (EditText)findViewById(R.id.addcliente_et_nome);
        edt_telefone= (EditText)findViewById(R.id.addcliente_et_telefone);
        edt_obs = (EditText)findViewById(R.id.addcliente_et_obs);

        btn_salvar = (Button)findViewById(R.id.addcliente_bt_salvar);
        btn_salvar.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if(retorno == null){
                finish();
            }else if(retorno.equals("list")){
                Intent intent = new Intent();
                intent.putExtra("volta", "novoCliente");
                setResult(2, intent);
                finish();
            }else if(retorno.equals("venda")){
                Intent intent = new Intent();
                intent.putExtra("volta", "queroCliente");
                setResult(2, intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private void addCliente() {
        //final ProgressDialog loading = ProgressDialog.show(this,"Salvando","Aguarde...");
        final String nome = edt_nome.getText().toString().trim();
        final String telefone = edt_telefone.getText().toString().trim();
        final String obs = edt_obs.getText().toString().trim();

        realm.beginTransaction();
        Cliente cliente = realm.where(Cliente.class).sort("id_cliente", Sort.DESCENDING).findFirst();
        int id = cliente == null ? 1 : cliente.getId_cliente() + 1;
        Cliente c = new Cliente(id, nome, telefone, obs);
        realm.copyToRealm(c);
        realm.commitTransaction();

        edt_nome.setText("");
        edt_telefone.setText("");
        edt_obs.setText("");

        if(retorno.equals("venda")){
            Intent intent = new Intent();
            intent.putExtra("volta", "queroCliente");
            setResult(2, intent);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btn_salvar){
            /*if(!Conexao.isConnected(this)){
                Toast.makeText(this, "Sem conexão com a INTERNET", Toast.LENGTH_LONG).show();
                return;
            }else{*/
            addCliente();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.mover_esquerda, R.anim.desaparecendo);
    }
}

