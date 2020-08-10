package com.claudinei.minhaloja.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Produto;
import com.claudinei.minhaloja.util.Conexao;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import io.realm.Realm;

public class ItenDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;

    TextView txtName, txtTipo, txtMarca, txtTamanho, txtCusto,
            txtId, txtVenda, txtLucro, txtObs, txtQtd;

    Button buttonUpdateItem, buttonDeleteItem, buttonAdd, buttonSubtract, buttonAddFoto;

    String itemId, itemTipo, itemDesc,
            itemMarca, itemPreco, itemTamanho,
            itemModelo, itemObs, item_pLucro,
            item_vVenda, item_qtd, titulo;
    private ImageView imagem;
    int valid;

    String mCurrentPhotoPath;
    Realm realm;
    Intent intent = new Intent();
    String alteracoes = "Sem Alterações";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Detalhe");

        Intent i = getIntent();
        itemId = i.getStringExtra("id");
        initialize();
    }

    private void initialize() {
        realm = Realm.getDefaultInstance();

        Produto produto = realm.where(Produto.class).equalTo("id_item", Integer.parseInt(itemId)).findFirst();


        itemTipo = produto.getTipo();
        itemDesc = produto.getDescricao();
        itemMarca = produto.getMarca();
        itemPreco = produto.getPreco_custo().toString();
        itemTamanho = produto.getTamanho();
        item_pLucro = produto.getPerc_lucro().toString();
        item_vVenda = produto.getPreco_venda().toString();
        //itemModelo = produto.getModelo();
        itemObs = produto.getObs();
        item_qtd = String.valueOf(produto.getQtd());
        File foto = new File(produto.getFoto());
        valid = 0;

        txtId = (TextView) findViewById(R.id.tv_dtl_id);
        txtTipo = (TextView) findViewById(R.id.tv_tipo);
        txtQtd = (TextView) findViewById(R.id.tv_dtl_qtd);
        txtName = (TextView) findViewById(R.id.txt_item_row_desc);
        txtMarca = (TextView) findViewById(R.id.txt_item_row_marca);
        txtTamanho = (TextView) findViewById(R.id.tv_tamanho);
        //txtModelo = (TextView) findViewById(R.id.tv_modelo);
        txtCusto = (TextView) findViewById(R.id.txt_item_row_preco);
        txtVenda = (TextView) findViewById(R.id.tv_vVenda);
        txtLucro = (TextView) findViewById(R.id.tv_pLucro);
        txtObs = (TextView) findViewById(R.id.tv_obs);
        imagem = (ImageView) findViewById(R.id.img_detail_image);

        buttonDeleteItem = (Button) findViewById(R.id.btn_deletar);
        buttonUpdateItem = (Button) findViewById(R.id.btn_alterar_up);
        buttonAdd = (Button) findViewById(R.id.btn_mais);
        buttonSubtract = (Button) findViewById(R.id.btn_menos);
        //buttonAddFoto = (Button) findViewById(R.id.btn_tirar_foto);

        txtId.setText(itemId);
        //txtTipo.setText(itemTipo);
        txtQtd.setText(item_qtd);
        txtName.setText(itemDesc);
        txtMarca.setText(itemMarca);
        //txtModelo.setText(itemModelo);
        txtTamanho.setText(itemTamanho);
        txtCusto.setText("Custo: R$ " + itemPreco);
        txtVenda.setText("Venda: R$ " + item_vVenda);
        txtLucro.setText("Lucro: " + item_pLucro);
        txtObs.setText(itemObs);
        if (produto.getFoto() == "foto"
                || produto.getFoto().equals("")
                || produto.getFoto() == null
                || produto.getFoto().isEmpty()) {
            imagem.setImageResource(R.mipmap.ic_launcher);
            imagem.setBackgroundColor(Color.parseColor("#CFD8DC"));

        } else {
            Picasso.get()
                    .load(foto)
                    .error(R.drawable.add_photo)
                    .into(imagem);
            imagem.setBackgroundColor(Color.parseColor("#CFD8DC"));
        }
        buttonDeleteItem.setOnClickListener(this);
        buttonUpdateItem.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        imagem.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_iten_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            intent.putExtra("detailActivity", alteracoes);
            setResult(2, intent);
            finish();
        }
        /*if (id == R.id.dtl_add_photo) {
            getPermissions();
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void deleteItem() {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Deletar");
        msg.setMessage("Realmente quer apagar este item?");
        //define um botão como positivo
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                final ProgressDialog loading = ProgressDialog.show(ItenDetailsActivity.this,
                        "Deletando", "Aguarde...");
                final String id = itemId;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {

                        Produto produto = realm.where(Produto.class).
                                equalTo("id_item", Integer.parseInt(id)).findFirst();

                        new File(
                                "/storage/emulated/0/Android/data/com.claudinei.minhaloja/files/Pictures/"
                                        + produto.getFoto().substring(66)).delete();
                        produto.deleteFromRealm();
                        loading.dismiss();
                        alteracoes = "Item deletado";
                        intent.putExtra("detailActivity", alteracoes);
                        setResult(2, intent);
                        finish();
                    }
                });

            }
        });
        msg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alerta = msg.create();
        //Exibe
        alerta.show();
    }

    private void updateItem() {
        Intent intentUp = new Intent(this, ItenUpdateActivity.class);
        String idItem = itemId;
        intentUp.putExtra("id", idItem);

        startActivityForResult(intentUp, 2);
    }

    private void addEstoque() {
        final String id = itemId;
        int qtd;

        //textViewQtd.setText(item_qtd);
        realm.beginTransaction();
        Produto produto = realm.where(Produto.class).
                equalTo("id_item", Integer.parseInt(id)).findFirst();
        qtd = produto.getQtd();
        produto.setQtd(qtd + 1);
        realm.commitTransaction();
        item_qtd = String.valueOf(produto.getQtd());
        txtQtd.setText(item_qtd);

        if (alteracoes.equals("Sem Alterações")) {
            alteracoes = "Estoque alterarado";
        } else {
            if (alteracoes.contains("Estoque alterarado")) {
                alteracoes = alteracoes + "";
            } else {
                alteracoes = alteracoes + ", Estoque alterarado";
            }

        }
    }

    private void subtraiEstoque() {
        final String id = itemId;
        int qtd;

        //textViewQtd.setText(item_qtd);
        realm.beginTransaction();
        Produto produto = realm.where(Produto.class).
                equalTo("id_item", Integer.parseInt(id)).findFirst();
        qtd = produto.getQtd();
        if (produto.getQtd() <= 0) {
            realm.cancelTransaction();
        } else {
            produto.setQtd(qtd - 1);
            realm.commitTransaction();
        }
        item_qtd = String.valueOf(produto.getQtd());
        txtQtd.setText(item_qtd);
        if (alteracoes.equals("Sem Alterações")) {
            alteracoes = "Estoque alterarado";
        } else {
            if (alteracoes.contains("Estoque alterarado")) {
                alteracoes = alteracoes + "";
            } else {
                alteracoes = alteracoes + ", Estoque alterarado";
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else
            dispatchTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + itemId + "_";
        ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "erro " + ex, Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.claudinei.minhaloja.fileprovider",
                        photoFile);
                mCurrentPhotoPath = photoURI.toString();
                realm.beginTransaction();
                Produto produto = realm.where(Produto.class).
                        equalTo("id_item", Integer.parseInt(itemId)).findFirst();
                if (produto.getFoto().equals("foto")
                        || produto.getFoto() == ""
                        || produto.getFoto() == null
                        || produto.getFoto().isEmpty()) {
                    produto.setFoto(currentPhotoPath);
                    if (alteracoes.equals("Sem Alterações")) {
                        alteracoes = "Foto adicionada";
                    } else {
                        alteracoes = alteracoes + "Foto adicionada";
                    }
                } else {
                    new File(
                            "/storage/emulated/0/Android/data/com.claudinei.minhaloja/files/Pictures/"
                                    + produto.getFoto().substring(66)).delete();
                    produto.setFoto(currentPhotoPath);
                    if (alteracoes == "Sem Alterações") {
                        alteracoes = "Foto substituida";
                    } else {
                        alteracoes = alteracoes + ", Foto substituida";
                    }
                }
                realm.commitTransaction();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonDeleteItem) {
            if (!Conexao.isConnected(this)) {
                Toast.makeText(this, "Sem conexão com a INTERNET", Toast.LENGTH_LONG).show();
                return;
            } else {
                deleteItem();
            }
        }
        if (v == buttonUpdateItem) {
            updateItem();
        }
        if (v == buttonAdd) {
            addEstoque();
        }
        if (v == buttonSubtract) {
            subtraiEstoque();
        }
        if (v == imagem) {
            getPermissions();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (requestCode == 2) {
            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = i != null ? i.getExtras() : null;
            if (params != null) {
                if (resultCode == 3) {
                    itemId = params.getString("id");
                    if (alteracoes == "Sem Alterações") {
                        alteracoes = "Item alterado";
                    } else {
                        alteracoes = alteracoes + ", Item alterado";
                    }
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap bm1 = BitmapFactory.decodeStream(getContentResolver().
                        openInputStream(Uri.parse(mCurrentPhotoPath)));

                File f = new File(currentPhotoPath);

                Picasso.get()
                        .load(f)
                        .error(R.drawable.estoque_fundo)
                        .into(imagem);

                Log.d("pathFoto", currentPhotoPath);
            } catch (FileNotFoundException fnex) {
                Toast.makeText(getApplicationContext(), "Foto não encontrada!", Toast.LENGTH_LONG).show();
            }
        }
    }
}