package com.claudinei.minhaloja;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.claudinei.minhaloja.view.CadastroClienteActivity;
import com.claudinei.minhaloja.view.CadastroProdutoActivity;
import com.claudinei.minhaloja.view.ClienteListActivity;
import com.claudinei.minhaloja.view.StockListActivity;
import com.claudinei.minhaloja.view.VendaActivity;
import com.claudinei.minhaloja.view.VendaListActivity;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView caddastroProduto, caddastroCliente,
            stockList, listCliente, venda, listVendas;
    ImageView bgapp, imgIconHome, imgLogo_home;
    TextView txtMainHome;
    GridLayout loMainBtn;
    float altura = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        getWindow().setStatusBarColor(Color.rgb(255, 69, 0));
        //actionbar.hide();
        Realm.init(this);

        caddastroProduto = findViewById(R.id.btn_main_cadastro_produto);
        caddastroProduto.setOnClickListener(this);

        stockList = findViewById(R.id.btn_main_list_estoque);
        stockList.setOnClickListener(this);

        caddastroCliente = findViewById(R.id.btn_main_cadastro_cliente);
        caddastroCliente.setOnClickListener(this);

        listCliente = findViewById(R.id.btn_main_list_cliente);
        listCliente.setOnClickListener(this);

        venda = findViewById(R.id.btn_main_vender);
        venda.setOnClickListener(this);

        listVendas = findViewById(R.id.btn_list_vendas);
        listVendas.setOnClickListener(this);

        //confTela();
        animacao();
    }



    private void animacao() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        bgapp = findViewById(R.id.bgapp);
        imgIconHome = findViewById(R.id.img_main_home);
        imgLogo_home = findViewById(R.id.img_main_logo_home);
        txtMainHome = findViewById(R.id.txt_main_home);
        loMainBtn = findViewById(R.id.grid_main_btn);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        //float dpWidth  = outMetrics.widthPixels / density;

        altura = Float.valueOf( dm.heightPixels );
        bgapp.animate().translationY((altura * 0.65f)*-1).setDuration(1000).setStartDelay(1900);
        loMainBtn.animate().translationY((altura * 0.63f)*-1).setDuration(1400).setStartDelay(1200);
        imgLogo_home.animate().translationY(-750).setDuration(900).setStartDelay(1300);

        //bgapp.getLayoutParams().height = Float.valueOf(dpHeight * 0.65);
        //loMainBtn.setVisibility(View.VISIBLE);
        /*loMainBtn.setAlpha(0f);
        loMainBtn.animate().alpha(1.0f).setDuration(200).setStartDelay(1900);
        txtMainHome.setAlpha(0f);
        txtMainHome.animate().alpha(1.0f).setDuration(1000).setStartDelay(1300);
        imgIconHome.setAlpha(0f);
        imgIconHome.animate().alpha(1.0f).setDuration(1000).setStartDelay(1300);*/

    }

    @Override
    public void onClick(View v) {
        if (v == caddastroProduto) {
            Intent intent = new Intent(this, CadastroProdutoActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        } else if (v == stockList) {
            Intent intent = new Intent(this, StockListActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        } else if (v == caddastroCliente) {
            Intent intent = new Intent(this, CadastroClienteActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        } else if (v == listCliente) {
            Intent intent = new Intent(this, ClienteListActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        } else if (v == venda) {
            Intent intent = new Intent(this, VendaActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        } else if (v == listVendas) {
            Intent intent = new Intent(this, VendaListActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                    R.anim.aparecendo, R.anim.mover_direita);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        }
    }
}