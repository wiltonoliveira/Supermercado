package com.example.supermercado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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
        String nomeText = nome.getText().toString();

        EditText quantidade = findViewById(R.id.editQuantidade);
        int quantidadeText = Integer.parseInt(quantidade.getText().toString());

        EditText preco = findViewById(R.id.editPreco);
        double precoText = Double.parseDouble(preco.getText().toString());

        Produto produto = new Produto(nomeText, quantidadeText, precoText);

        if(!(nomeText.equals(""))) {
            itemsAdapter.add(produto);

            nome.setText("");
            quantidade.setText("");
            preco.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }
    }
}