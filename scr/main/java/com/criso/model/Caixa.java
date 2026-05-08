package com.criso.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representa um caixa financeiro, como conta corrente, carteira física, etc.
 */
public class Caixa  implements Serializable {
    /** Identificador único do caixa */
    public UUID id;
    /** Nome do caixa */
    public String nome;
    /** Saldo inicial do caixa */
    public BigDecimal saldoInicial;
    /** Saldo atual do caixa */
    public BigDecimal saldoAtual;

    /**
     * Cria um novo caixa com nome e saldo inicial.
     * @param nome Nome do caixa
     * @param saldoInicial Saldo inicial
     */
    public Caixa(String nome, BigDecimal saldoInicial) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoInicial;
    }
    /**
     * Adiciona um valor ao saldo atual do caixa.
     * @param valor Valor a adicionar
     */
    public void adicionarValor(BigDecimal valor){
        saldoAtual = saldoAtual.add(valor);
    };
    /**
     * Remove um valor do saldo atual do caixa.
     * @param valor Valor a remover
     */
    public void removerValor(BigDecimal valor){
        saldoAtual = saldoAtual.subtract(valor);
    }
    /**
     * Retorna o identificador do caixa.
     * @return UUID do caixa
     */
    public UUID getId() {
        return id;
    }
    /**
     * Retorna o nome do caixa.
     * @return nome
     */
    public String getNome() {
        return nome;
    }
    /**
     * Retorna o saldo inicial do caixa.
     * @return saldo inicial
     */
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    /**
     * Retorna o saldo atual do caixa.
     * @return saldo atual
     */
    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
    /**
     * Define o nome do caixa.
     * @param nome Nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
}
