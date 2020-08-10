package com.claudinei.minhaloja.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Produto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import io.realm.Realm;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    List<Produto> produto;
    Context context;
    View view;
    ViewHolder viewHolder;

    public RecyclerViewAdapter(Context context, List<Produto> produto) {
        this.context = context;
        this.produto = produto;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView iditem_adapter;
        public TextView qtd_adapter;
        public TextView desc_adapter;
        public TextView tam_adapter;
        public TextView preco_adapter;

        public ViewHolder(@NonNull View rowView) {
            super(rowView);
            imageView = (ImageView) rowView.findViewById(R.id.img_card_imageview);
            iditem_adapter = (TextView) rowView.findViewById(R.id.txt_card_cod);
            qtd_adapter =    (TextView) rowView.findViewById(R.id.txt_card_qtd);
            desc_adapter = (TextView) rowView.findViewById(R.id.txt_card_descricao);
            //TextView marca_adapter =    (TextView) rowView.findViewById(R.id.tv_brand);
            //TextView modelo_adapter =   (TextView) rowView.findViewById(R.id.tv_modelo);
            tam_adapter = (TextView) rowView.findViewById(R.id.txt_card_tamanho);
            preco_adapter = (TextView) rowView.findViewById(R.id.txt_card_preco_venda);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_card_list_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Realm realm = Realm.getDefaultInstance();

        Produto prod = realm.where(Produto.class).equalTo("id_item",
                Integer.parseInt(Integer.toString(produto.get(position).getId_item()))).findFirst();
        File foto = new File(prod.getFoto());
        Log.d("TAG", prod.getFoto());
        /*String retirar = "content://com.claudinei.minhaloja.fileprovider/my_images/";
        String colocar = "/storage/emulated/0/Android/data/com.claudinei.minhaloja/files/";*/

        if (prod.getFoto() == "foto"
                || prod.getFoto() == ""
                || prod.getFoto() == null
                || prod.getFoto().isEmpty()) {
            Picasso.get()
                    .load(R.drawable.estoque_fundo)
                    .error(R.drawable.estoque_fundo)
                    .into(holder.imageView);
        } else {
            Picasso.get()
                    .load(foto)
                    .error(R.drawable.estoque_fundo)
                    .into(holder.imageView);
        }

        holder.iditem_adapter.setText("Código: " +Integer.toString(produto.get(position).getId_item()));
        holder.qtd_adapter.setText("Quant.: " +Integer.toString(produto.get(position).getQtd()));
        holder.desc_adapter.setText(produto.get(position).getDescricao());
        //marca_adapter.setText(elementos.get(position).getMarca());
        holder.tam_adapter.setText("Tamanho: " + produto.get(position).getTamanho());
        holder.preco_adapter.setText("Preço: " + produto.get(position).getPreco_venda());


    }

    @Override
    public int getItemCount() {
        return produto.size();
    }


}
