package com.criso.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma despesa financeira, podendo envolver transferência entre caixas.
 * Herda de Transacao.
 */
public class Despesa extends Transacao {
    /** Status da despesa (Pago ou Pendente) */
    StatusDespesa status;
    /** Caixa que paga a despesa */
    Caixa caixaQuePaga;
    /** Caixa que recebe a despesa (opcional) */
    Caixa caixaQueRecebe;

    /**
     * Cria uma despesa simples, removendo o valor do caixa pagador.
     * @param nome Nome da despesa
     * @param valor Valor da despesa
     * @param data Data da despesa
     * @param status Status da despesa
     * @param caixaQuePaga Caixa de onde sai o valor
     */
    public Despesa(String nome, BigDecimal valor, LocalDate data, StatusDespesa status, Caixa caixaQuePaga) {
        super(nome, valor, data);
        this.status = status;
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQuePaga.removerValor(valor);
    }
    /**
     * Cria uma despesa simples com descrição, removendo o valor do caixa pagador.
     * @param nome Nome da despesa
     * @param descricao Descrição da despesa
     * @param valor Valor da despesa
     * @param data Data da despesa
     * @param status Status da despesa
     * @param caixaQuePaga Caixa de onde sai o valor
     */
    public Despesa(String nome, String descricao, BigDecimal valor, LocalDate data, StatusDespesa status, Caixa caixaQuePaga) {
        super(nome, descricao, valor, data);
        this.status = status;
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQuePaga.removerValor(valor);
    }
    /**
     * Cria uma despesa de transferência entre caixas.
     * @param nome Nome da despesa
     * @param valor Valor da despesa
     * @param data Data da despesa
     * @param status Status da despesa
     * @param caixaQuePaga Caixa de onde sai o valor
     * @param caixaQueRecebe Caixa que recebe o valor
     */
    public Despesa(String nome, BigDecimal valor, LocalDate data, StatusDespesa status, Caixa caixaQuePaga, Caixa caixaQueRecebe) {
        super(nome, valor, data);
        this.status = status;
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQuePaga.removerValor(valor);
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQueRecebe.adicionarValor(valor);
    }
    /**
     * Cria uma despesa de transferência entre caixas com descrição.
     * @param nome Nome da despesa
     * @param descricao Descrição da despesa
     * @param valor Valor da despesa
     * @param data Data da despesa
     * @param status Status da despesa
     * @param caixaQuePaga Caixa de onde sai o valor
     * @param caixaQueRecebe Caixa que recebe o valor
     */
    public Despesa(String nome, String descricao, BigDecimal valor, LocalDate data, StatusDespesa status, Caixa caixaQuePaga, Caixa caixaQueRecebe) {
        super(nome, descricao, valor, data);
        this.status = status;
        this.caixaQuePaga = caixaQuePaga;
        this.caixaQuePaga.removerValor(valor);
        this.caixaQueRecebe = caixaQueRecebe;
        this.caixaQueRecebe.adicionarValor(valor);
    }

    /**
     * Retorna o status da despesa.
     * @return status
     */
    public StatusDespesa getStatus() {
        return status;
    }
    /**
     * Retorna o caixa que recebe a despesa.
     * @return caixa que recebe
     */
    public Caixa getCaixaQueRecebe() {return caixaQueRecebe;}
    /**
     * Retorna o caixa que paga a despesa.
     * @return caixa que paga
     */
    public Caixa getCaixaQuePaga() {return caixaQuePaga;}
    /**
     * Define o status da despesa.
     * @param status StatusDespesa
     */
    public void setStatus(StatusDespesa status) {
        this.status = status;
    }
}