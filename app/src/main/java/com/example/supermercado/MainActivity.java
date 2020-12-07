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

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Produto> items;
    private Adapter itemsAdapter;
    private ListView lista;
    private Button addButton;
    private Double usd;


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

        items = (ArrayList<Produto>) AppDatabase.getInstance(getApplicationContext()).ProdutoDAO().getAll();
        itemsAdapter = new Adapter(this, R.layout.item_layout, items);
        lista.setAdapter(itemsAdapter);
        setUpListViewListener();

        String url = "https://economia.awesomeapi.com.br/all/USD-BRL";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread((new Runnable() {
                        @Override
                        public void run() {
                                System.out.println(myResponse.substring(69,74));
                                usd = Double.parseDouble(myResponse.substring(69,72));
                        }
                    }));
                }
            }
        });

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

        valor.setText("Valor Total: " +
                Double.toString(getTotal()) + " (USD: " + Double.toString(getTotal() / usd) + ")");

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
                AppDatabase.getInstance(getApplicationContext()).ProdutoDAO().update(itemAntigo);
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

                AppDatabase.getInstance(getApplicationContext()).ProdutoDAO().delete(items.get(i));
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
            AppDatabase.getInstance(getApplicationContext()).ProdutoDAO().insert(produto);

            nome.setText("");
            quantidade.setText("");
            preco.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Insira os dados corretamente", Toast.LENGTH_LONG).show();
        }

    }
}