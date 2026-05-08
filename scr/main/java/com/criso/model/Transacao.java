package com.criso.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe abstrata que representa uma transação financeira genérica.
 * Pode ser estendida para receitas, despesas, etc.
 */
public abstract class Transacao implements Serializable {
    /** Identificador único da transação */
    public UUID id;
    /** Descrição da transação */
    public String descricao;
    /** Valor da transação */
    public BigDecimal valor;
    /** Data da transação */
    public LocalDate data;
    /** Nome da transação */
    public String nome;
    /** Caixa alvo da transação */
    public Caixa caixaAlvo;
    /** Categoria da transação */
    public Categoria categoria;

    /**
     * Construtor da transação com nome, descrição, valor e data.
     * @param nome Nome da transação
     * @param descricao Descrição da transação
     * @param valor Valor da transação
     * @param data Data da transação
     */
    public Transacao(String nome, String descricao, BigDecimal valor, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.id = UUID.randomUUID();
        this.nome = nome;
    }

    /**
     * Construtor da transação com nome, valor e data.
     * @param nome Nome da transação
     * @param valor Valor da transação
     * @param data Data da transação
     */
    public Transacao(String nome, BigDecimal valor, LocalDate data) {
        this.valor = valor;
        this.data = data;
        this.id = UUID.randomUUID();
        this.nome = nome;
    }

    /**
     * Retorna o identificador da transação.
     * @return UUID da transação
     */
    public UUID getId() {
        return id;
    }

    /**
     * Retorna a descrição da transação.
     * @return descrição
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o valor da transação.
     * @return valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Retorna a data da transação.
     * @return data
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Retorna o nome da transação.
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    // Métodos Set
    /**
     * Define o identificador da transação.
     * @param id UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Define a descrição da transação.
     * @param descricao Descrição
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Define o valor da transação.
     * @param valor Valor
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * Define a data da transação.
     * @param data Data
     */
    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * Define a categoria da transação.
     * @param categoria Categoria
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna a categoria da transação.
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }
}