package com.example.supermercado;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Produtos")
public class Produto {
    @PrimaryKey
  //  private int id;
    @NonNull
    @ColumnInfo(name = "nome")
    private String nome;
    @ColumnInfo(name = "quantidade")
    private int quantidade;
    @ColumnInfo(name = "valor")
    private double valor;

    public Produto(String nome, int quantidade, double valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }
}
