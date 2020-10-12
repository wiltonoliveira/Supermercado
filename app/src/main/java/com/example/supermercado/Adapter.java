package com.example.supermercado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Produto> {

    Context context;
    int resource;

    public Adapter(Context context, int resource, ArrayList<Produto> produtos){
        super(context, resource, produtos);

        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String nome = getItem(position).getNome();
        int quantidade = getItem(position).getQuantidade();
        double valor = getItem(position).getValor();


        Produto produto = new Produto(nome, quantidade, valor);
        LayoutInflater inflater = LayoutInflater.from(context);

        convertView = inflater.inflate(resource, parent, false);
        TextView tvNome = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvQuantidade = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvValor = (TextView) convertView.findViewById(R.id.textView3);

        tvNome.setText(nome);
        tvQuantidade.setText("Quantidade: " + Integer.toString(quantidade));
        tvValor.setText("Preço: " + Double.toString(valor));

        return convertView;
    }
}
