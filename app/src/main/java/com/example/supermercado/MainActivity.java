package com.example.supermercado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Produto> items;
    private Adapter itemsAdapter;
    private ListView lista;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);
        addButton = findViewById(R.id.editarButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });



        items = new ArrayList<>();
        itemsAdapter = new Adapter(this, R.layout.item_layout, items);
        lista.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public void ordernarPorNome(ArrayList<Produto> items) {
        Collections.sort(items, new Comparator<Produto>() {
            @Override
            public int compare(Produto produto, Produto t1) {
                return produto.getNome().compareTo(t1.getNome());
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

    public void ordernarPorPreco(ArrayList<Produto> items) {
        Collections.sort(items, new Comparator<Produto>() {
            @Override
            public int compare(Produto produto, Produto t1) {
                if(produto.getValor() < t1.getValor()) {
                    return -1;
                }
                if(produto.getValor() > t1.getValor()) {
                    return 1;
                }
                return 0;
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

    private double getTotal() {
        double total = 0;

        for (int i = 0; i < items.size(); i++) {
            double totalItem = (items.get(i).getValor() * items.get(i).getQuantidade());
            total += totalItem;
        }
        return total;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.porNome:
                ordernarPorNome(this.items);
                Toast.makeText(this, "Por nome", Toast.LENGTH_LONG).show();
                return true;

            case R.id.porPreco:
                ordernarPorPreco(this.items);
                Toast.makeText(this, "Por preço", Toast.LENGTH_LONG).show();
                return true;

            case R.id.valorTotal:
                mostrarTotal();
                Toast.makeText(this, "Valor Total", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void mostrarTotal(){
        final Dialog dialogo = new Dialog(MainActivity.this);
        dialogo.setTitle("Valor Total");
        dialogo.setContentView(R.layout.total_layout);

        final TextView valor = (TextView) dialogo.findViewById(R.id.textTotal);

        valor.setText("Valor Total: " +Double.toString(getTotal()));

        dialogo.show();
    }

    private void showInputBox(final Produto itemAntigo, final int index){
        final Dialog dialogo = new Dialog(MainActivity.this);
        dialogo.setTitle("Editar");
        dialogo.setContentView(R.layout.editar_layout);

        final EditText nome = (EditText) dialogo.findViewById(R.id.editNome);
        final EditText quantidade = (EditText) dialogo.findViewById(R.id.editQuantidade);
        final EditText valor = (EditText) dialogo.findViewById(R.id.editPreco);


        nome.setText(itemAntigo.getNome());
        quantidade.setText(Integer.toString(itemAntigo.getQuantidade()));
        valor.setText(Double.toString(itemAntigo.getValor()));

        Button editar = (Button) dialogo.findViewById(R.id.editarButton);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAntigo.setNome(nome.getText().toString());
                itemAntigo.setQuantidade(Integer.parseInt(quantidade.getText().toString()));
                itemAntigo.setValor(Double.parseDouble(valor.getText().toString()));

                itemsAdapter.notifyDataSetChanged();
                dialogo.dismiss();

                Toast.makeText(MainActivity.this, "Item Editado com sucesso", Toast.LENGTH_LONG).show();
            }
        });

        dialogo.show();
    }

    private void setUpListViewListener() {
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item Removido", Toast.LENGTH_LONG).show();

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }

        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showInputBox(items.get(i), i);
                //Toast.makeText(getApplicationContext(), "Long Press to delete item...", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void addItem(View view) {
        EditText nome = findViewById(R.id.editNome);
        EditText quantidade = findViewById(R.id.editQuantidade);
        EditText preco = findViewById(R.id.editPreco);


        String nomeText = nome.getText().toString();
        String quantidadeText = quantidade.getText().toString();
        String precoText = preco.getText().toString();


        if(nomeText.equals("")){
            Toast.makeText(getApplicationContext(), "Insira o nome", Toast.LENGTH_LONG).show();
            return;
        }

        if(quantidadeText.equals("")){
            Toast.makeText(getApplicationContext(), "Insira quantidade", Toast.LENGTH_LONG).show();
            return;
        }

        if(precoText.equals("")){
            Toast.makeText(getApplicationContext(), "Insira o preço", Toast.LENGTH_LONG).show();
            return;
        }

        int quantidadeValue = Integer.parseInt(quantidadeText);
        double precoValue = Double.parseDouble(precoText);

        Produto produto = new Produto(nomeText, quantidadeValue, precoValue);

        if (!(nomeText.equals(""))) {
            itemsAdapter.add(produto);

            nome.setText("");
            quantidade.setText("");
            preco.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Insira os dados corretamente", Toast.LENGTH_LONG).show();
        }

    }
}