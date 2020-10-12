package com.example.supermercado;

public class Produto {
    private String nome;
    private int quantidade;
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
