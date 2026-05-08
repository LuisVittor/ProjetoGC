package com.criso.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma receita financeira, que pode envolver transferência entre caixas.
 * Herda de Transacao.
 */
public class Receita extends Transacao{
    /** Caixa que paga a receita (em caso de transferência) */
    Caixa caixaQuePaga;
    /** Caixa que recebe a receita */
    Caixa caixaQueRecebe;

    /**
     * Cria uma receita simples, adicionando o valor ao caixa de destino.
     * @param descricao Descrição da receita
     * @param nome Nome da receita
     * @param valor Valor da receita
     * @param data Data da receita
     * @param caixaQueRecebe Caixa que recebe o valor
     */
    public Receita(String descricao, String nome, BigDecimal valor, LocalDate data, Caixa caixaQueRecebe) {
        super(nome, descricao, valor, data);
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQueRecebe.adicionarValor(valor);
    }
    /**
     * Cria uma receita simples sem descrição, adicionando o valor ao caixa de destino.
     * @param nome Nome da receita
     * @param valor Valor da receita
     * @param data Data da receita
     * @param caixaQueRecebe Caixa que recebe o valor
     */
    public Receita(String nome, BigDecimal valor, LocalDate data, Caixa caixaQueRecebe) {
        super(nome, valor, data);
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQueRecebe.adicionarValor(valor);
    }

    /**
     * Cria uma receita de transferência entre caixas.
     * @param descricao Descrição da receita
     * @param nome Nome da receita
     * @param valor Valor da receita
     * @param data Data da receita
     * @param caixaQueRecebe Caixa que recebe o valor
     * @param caixaQuePaga Caixa que paga o valor
     */
    public Receita(String descricao, String nome, BigDecimal valor, LocalDate data, Caixa caixaQueRecebe, Caixa caixaQuePaga) {
        super(nome, descricao, valor, data);
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQueRecebe.adicionarValor(valor);
        this.caixaQuePaga.removerValor(valor);
    }
    /**
     * Cria uma receita de transferência entre caixas sem descrição.
     * @param nome Nome da receita
     * @param valor Valor da receita
     * @param data Data da receita
     * @param caixaQueRecebe Caixa que recebe o valor
     * @param caixaQuePaga Caixa que paga o valor
     */
    public Receita(String nome, BigDecimal valor, LocalDate data, Caixa caixaQueRecebe, Caixa caixaQuePaga) {
        super(nome, valor, data);
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQueRecebe.adicionarValor(valor);
        this.caixaQuePaga.removerValor(valor);
    }
    /**
     * Retorna o caixa que recebe o valor da receita.
     * @return Caixa que recebe
     */
    public Caixa getCaixaQueRecebe() {return caixaQueRecebe;}

    /**
     * Retorna o caixa que paga o valor da receita em uma transferencia.
     * @return Caixa que paga
     */
    public Caixa getCaixaQuePaga() {return caixaQuePaga;}
}
