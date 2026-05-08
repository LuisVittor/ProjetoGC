package com.criso.model;

import java.io.Serializable;

/**
 * Representa uma categoria para transações financeiras.
 */
public class Categoria  implements Serializable {
    /** Nome da categoria */
    public String nome;
    /** Cor associada à categoria */
    public String cor;

    /**
     * Cria uma categoria com nome e cor.
     * @param nome Nome da categoria
     * @param cor Cor associada
     */
    public Categoria(String nome, String cor) {
        this.nome = nome;
        this.cor = cor;
    }

    /**
     * Retorna o nome da categoria.
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a cor da categoria.
     * @return cor
     */
    public String getCor() {
        return cor;
    }

    /**
     * Define o nome da categoria.
     * @param nome Nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define a cor da categoria.
     * @param cor Cor
     */
    public void setCor(String cor) {
        this.cor = cor;
    }
}