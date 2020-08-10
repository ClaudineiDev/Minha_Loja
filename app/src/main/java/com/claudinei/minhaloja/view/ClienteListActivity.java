package com.claudinei.minhaloja.view;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Cliente;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ClienteListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    SimpleAdapter adapter;
    ProgressDialog loading;
    EditText editTextSearchCliente;
    Button btnlocalizar;
    Intent intent;
    String retornoCadastro, telaVenda;
    Realm realm;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));*/

        realm = Realm.getDefaultInstance();

        intent = getIntent();

        telaVenda = intent.getExtras() != null ? intent.getStringExtra("venda") : "";

        editTextSearchCliente = (EditText) findViewById(R.id.listcliente_ed_procurar);
        listView = (ListView) findViewById(R.id.listcliente_lv_cliente);
        listView.setOnItemClickListener(this);


        getClienteLocal();

        btnlocalizar = (Button) findViewById(R.id.listcliente_bt_buscar);

        getClienteLocal();
    }

    private void configuraToolbar() {
        toolbar = findViewById(R.id.list_clientes_topAppBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cliente_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.add_cliente) {
            Intent i = new Intent(this, CadastroClienteActivity.class);
            if (telaVenda.equals("")) {
                i.putExtra("ida", "list");
                startActivityForResult(i, 2);
            } else {
                i.putExtra("ida", "venda");
                startActivityForResult(i, 2);
            }
        }
        return super.onOptionsItemSelected(item);
    }

/*    public void getClientes(Context context) {

        loading = ProgressDialog.show(this, "Carregando", "Aguarde...", false, true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://script.google.com/macros/s/AKfycbw0e2xrnkVYk9dD8_sH9lc1kksKuhfJYGfIYgs6ZvkaWxp3cbw/exec?action=getClientes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResposnce) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);
                String id = jo.getString("id");
                String nome = jo.getString("nome");
                String telefone = jo.getString("telefone");
                String obs = jo.getString("obs");

                HashMap<String, String> item = new HashMap<>();

                item.put("id", id);
                item.put("nome", nome);
                item.put("telefone", telefone);
                item.put("obs", obs);

                list.add(item);

                Cliente cliente = new Cliente(id, nome, telefone, obs);

                realm.copyToRealm(cliente);
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        realm.commitTransaction();
        realm.close();

        getClienteLocal();
        loading.dismiss();
    }*/

    private void getClienteLocal() {
        ArrayList<HashMap<String, String>> list_adapter = new ArrayList<>(); //lista para preencher o simpleAdapter
        List<Cliente> list = realm.where(Cliente.class).sort("id_cliente", Sort.DESCENDING).findAll();

        for (int i = 0; i < list.size(); i++) {

            HashMap<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(list.get(i).getId_cliente()));
            item.put("nome", list.get(i).getNome_cliente());
            item.put("telefone", list.get(i).getTelefone_cliente());
            item.put("obs", list.get(i).getObs_cliente());

            list_adapter.add(item);
        }

        adapter = new SimpleAdapter(this, list_adapter, R.layout.row_list_cliente,
                new String[]{"nome", "telefone", "obs", "id"},
                new int[]{R.id.clienterow_tv_nome, R.id.clienterow_tv_telefone, R.id.clienterow_tv_obs, R.id.clienterow_tv_id});

        listView.setAdapter(adapter);


        editTextSearchCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ClienteListActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

/*    public void botaoWhatsapp(String contato) {
        ImageView btwhatsapp = (ImageView) findViewById(R.id.img_whatsapp);
        String apiWhatsapp = "http://api.whatsapp.com/send?1=pt_BR&phone=55" + contato;
        Uri uri = Uri.parse(apiWhatsapp);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);

    }*/

    private void deleteCliente() {
        // obtain the results of a query
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Cliente> results = realm.where(Cliente.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String idcliente = (String) ((TextView) view.findViewById(R.id.clienterow_tv_id)).getText();
        final String nomecliente = (String) ((TextView) view.findViewById(R.id.clienterow_tv_nome)).getText();
        if (telaVenda.equals("queroCliente")) {
            Intent intent = new Intent();
            intent.putExtra("id", idcliente);
            intent.putExtra("nome", nomecliente);
            setResult(1, intent);
            finish();
        } else {

            ArrayList<String> itens = new ArrayList<String>();
            itens.add("Remover");
            itens.add("Voltar");

            ArrayAdapter adapter = new ArrayAdapter(ClienteListActivity.this, R.layout.alertdialog_item_venda, itens);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    TextInputLayout textInputLayout = new TextInputLayout(ClienteListActivity.this);
                    textInputLayout.setPadding(
                            getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin), // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                            0,
                            getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin),
                            0
                    );
                    if (which == 0) {
                        dialog.cancel();
                        Realm realmDelete = Realm.getDefaultInstance();
                        Cliente clienteDelete = realmDelete.where(Cliente.class).equalTo("id_cliente", Integer.valueOf(idcliente)).findFirst();
                        realmDelete.beginTransaction();
                        clienteDelete.deleteFromRealm();
                        realmDelete.commitTransaction();
                        getClienteLocal();
                        Toast.makeText(getBaseContext(), "Cliente removido - " + nomecliente, Toast.LENGTH_LONG).show();
                    }

                    if (which == 1) {
                        dialog.cancel();
                    }
                }
            }).create();

            builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // se o "data" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = data != null ? data.getExtras() : null;
            if (params != null) {

                if (resultCode == 2) {
                    retornoCadastro = params.getString("volta");

                    if (retornoCadastro.equals("queroCliente")) {
                        getClienteLocal();
                        telaVenda = retornoCadastro;
                    } else if (retornoCadastro.equals("novoCliente")) {
                        getClienteLocal();
                    }
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.mover_esquerda, R.anim.desaparecendo);
    }
}