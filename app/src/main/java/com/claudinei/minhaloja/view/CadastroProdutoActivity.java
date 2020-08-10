package com.claudinei.minhaloja.view;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Produto;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.Sort;

public class CadastroProdutoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private EditText editTextItemName;
    private EditText editTextMarca;
    private EditText editTextPreco;
    private EditText editText_vVenda;
    private EditText editText_pLucro;
    private EditText editTextTamanho;
    private EditText editTextObs;
    //private EditText editTextModelo;
    private Button buttonAddItem;
    private Spinner spinnerTipo;
    private ImageView imagem;
    Double calculo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);


        configuraToolbar();
        initialize();

    }




    private void configuraToolbar() {
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ola fale um pouco");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }


    }

    public void initialize() {

        editTextItemName = (EditText) findViewById(R.id.edt_item_name);
        editTextMarca = (EditText) findViewById(R.id.edt_marca);
        spinnerTipo = (Spinner) findViewById(R.id.sp_tipo);
        editTextPreco = (EditText) findViewById(R.id.edt_preco);
        editText_vVenda = (EditText) findViewById(R.id.edt_vVenda);
        editText_pLucro = (EditText) findViewById(R.id.edt_pLucro);
        editTextTamanho = (EditText) findViewById(R.id.edt_tamanho);
        //editTextModelo = (EditText) findViewById(R.id.edt_item_modelo);
        editTextObs = (EditText) findViewById(R.id.edt_obs);
        //buttonAddItem = (Button) findViewById(R.id.btn_add_item);

        imagem = (ImageView) findViewById(R.id.img_detail_image);

        buttonAddItem = (Button) findViewById(R.id.btn_salvar_item);
        buttonAddItem.setOnClickListener(this);
        editText_vVenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTextPreco.getText().toString().equals("")) {
                    if (count > 0) {
                        calculo = ((Double.parseDouble(s.toString()) /
                                Double.parseDouble(editTextPreco.getText().toString()) - 1) * 100);

                        editText_pLucro.setText(String.format("%.2f", calculo));
                    }
                } else {
                    editText_pLucro.setText("");
                    //Toast.makeText(ActivityAddItem.this, "Preencha o Custo", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPreco.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editText_vVenda.getText().toString().equals("")) {
                    if (count > 0) {
                        calculo = ((Double.parseDouble(editText_vVenda.getText().toString()) / Double.parseDouble(s.toString()) - 1) * 100);

                        editText_pLucro.setText(String.format("%.2f", calculo));
                    }
                } else {
                    editText_pLucro.setText("");
                    //Toast.makeText(ActivityAddItem.this, "Preencha a Venda", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void limparCampos() {
        TextView textViewPreencher = (TextView) findViewById(R.id.txt_preencher);
        textViewPreencher.setVisibility(View.GONE);
        editTextItemName.setText("");
        editTextItemName.setHint("");
        editTextMarca.setText("");
        editTextMarca.setHint("");
        spinnerTipo.setSelection(0);
        spinnerTipo.setBackgroundColor(0x00000000);
        editTextPreco.setText("");
        editTextPreco.setHint("");
        editText_vVenda.setText("");
        editText_pLucro.setText("");
        editTextTamanho.setText("");
        editTextTamanho.setHint("");
        /*editTextModelo.setText("");
        editTextModelo.setHint("");*/
        editTextObs.setText("");
    }

    public int validarCampos() {
        TextView textViewPreencher = (TextView) findViewById(R.id.txt_preencher);
        int validar = 0;
        if (editTextItemName.getText().toString().trim().equals("")) {
            editTextItemName.setHint("*Preencher");
            editTextItemName.setHintTextColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        }
        if (editTextMarca.getText().toString().trim().equals("")) {
            editTextMarca.setHint("*Preencher");
            editTextMarca.setHintTextColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        }
        if (spinnerTipo.getSelectedItem().toString().equals("Tipo")) {
            //Toast.makeText(ActivityAddItem.this,"Selecione o Tipo",Toast.LENGTH_LONG).show();
            spinnerTipo.setBackgroundColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        } else {
            spinnerTipo.setBackgroundColor(0x00000000);
        }
        if (editTextTamanho.getText().toString().trim().equals("")) {
            editTextTamanho.setHint("  *  ");
            editTextTamanho.setHintTextColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        }
        if (editTextPreco.getText().toString().trim().equals("")) {
            editTextPreco.setHint("  *  ");
            editTextPreco.setHintTextColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        }
/*        if (editTextModelo.getText().toString().trim().equals("")) {
            editTextModelo.setHint("*Preencher");
            editTextModelo.setHintTextColor(0xFFFF1301);
            textViewPreencher.setVisibility(View.VISIBLE);
            textViewPreencher.setText("*Preencher campos marcados (*), estes campos são necessários.");
            validar = 1;
            return validar;
        }*/
        return validar;
    }

    private void addItemProduto() {

        final String name = editTextItemName.getText().toString().trim();
        final String marca = editTextMarca.getText().toString().trim();
        final String tipo = spinnerTipo.getSelectedItem().toString();
        final Double preco = Double.parseDouble(editTextPreco.getText().toString().trim());
        final Double venda = Double.parseDouble(editText_vVenda.getText().toString().trim());
        final Double lucro = calculo;
        final String tamanho = editTextTamanho.getText().toString().toUpperCase().trim();
        //final String modelo = editTextModelo.getText().toString().trim();
        final String obs = editTextObs.getText().toString().trim();

        // Colocar um try cath pra pegar um possivel erro
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Produto item = realm.where(Produto.class).sort("id_item", Sort.DESCENDING).findFirst();
        int id = item == null ? 1 : item.getId_item() + 1;
        Produto produto = new Produto(id, tipo, name, marca, preco, venda, lucro, tamanho, obs, "foto", 1);
        realm.copyToRealm(produto);
        realm.commitTransaction();
        realm.close();
        Toast.makeText(getBaseContext(), "Registros inseridos", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.produto_menu, menu);
        return true;
    }*/
    @Override
    public void onClick(View v) {
        if (v == buttonAddItem) {
            if (validarCampos() == 0) {
                addItemProduto();
                limparCampos();
            } else {
                validarCampos();
            }
        } /*else if (v == buttonAddFoto) {
            Log.d("Foto", "-----------------------");
            getPermissions();
        } else if (v == buttonFotoGirar) {
            Bitmap bitmap = null;
            try {
                bitmap = CarregadorDeFoto.carrega(currentPhotoPath, "90");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("CaminhoFoto", currentPhotoPath);
            Log.d("CaminhoFoto", mCurrentPhotoPath);
            galleryAddPic();
            //imagem.setImageBitmap(bitmap);
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtTexto.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.mover_esquerda, R.anim.desaparecendo);
    }
}