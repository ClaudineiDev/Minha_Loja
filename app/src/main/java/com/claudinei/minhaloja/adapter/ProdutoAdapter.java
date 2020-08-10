package com.claudinei.minhaloja.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.claudinei.minhaloja.R;
import com.claudinei.minhaloja.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    private final Context context;
    private final List<Produto> elementos;
    private List<Produto> itens_exibicao;

    public ProdutoAdapter(Context context, List<Produto> elementos) {
        super(context, R.layout.row_list_item_carrinho, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_card_list_item, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_card_imageview);
        TextView iditem_adapter = (TextView) rowView.findViewById(R.id.txt_card_cod);
        TextView desc_adapter = (TextView) rowView.findViewById(R.id.txt_card_descricao);
        //TextView marca_adapter =    (TextView) rowView.findViewById(R.id.tv_brand);
        //TextView modelo_adapter =   (TextView) rowView.findViewById(R.id.tv_modelo);
        TextView tam_adapter = (TextView) rowView.findViewById(R.id.txt_card_tamanho);
        TextView preco_adapter = (TextView) rowView.findViewById(R.id.txt_card_preco_venda);
        //TextView qtd_adapter =      (TextView) rowView.findViewById(R.id.tv_listQtd);
        Realm realm = Realm.getDefaultInstance();

        Produto produto = realm.where(Produto.class).equalTo("id_item", Integer.parseInt(Integer.toString(elementos.get(position).getId_item()))).findFirst();
        String foto = produto.getFoto();
        Log.d("TAG", produto.getFoto());
        String retirar = "content://com.claudinei.minhaloja.fileprovider/my_images/";
        String colocar = "/storage/emulated/0/Android/data/com.claudinei.minhaloja/files/";
        foto.replace(retirar,colocar);
        Log.d("FotoRealm", foto);
        if (produto.getFoto() == "foto"
                || produto.getFoto() == ""
                || produto.getFoto() == null
                || produto.getFoto().isEmpty()) {
            imageView.setImageResource(R.mipmap.ic_launcher);

        } else {
            Picasso.get()
                    .load(foto)
                    .rotate(90)
                    .into(imageView);
        }
        iditem_adapter.setText(Integer.toString(elementos.get(position).getId_item()));
        desc_adapter.setText(elementos.get(position).getDescricao());
        //marca_adapter.setText(elementos.get(position).getMarca());
        //modelo_adapter.setText(elementos.get(position).getModelo());
        tam_adapter.setText("Tamanho: " + elementos.get(position).getTamanho());
        preco_adapter.setText("Preço: " + elementos.get(position).getPreco_venda());
        //qtd_adapter.setText(String.valueOf(elementos.get(position).getQtd()));

        return rowView;
    }

}
