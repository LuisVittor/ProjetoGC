package com.criso.model;
import java.math.BigDecimal;

/**
 * Representa uma meta financeira, que é um tipo especial de caixa com saldo alvo.
 */
public class Meta  extends Caixa {
    /** Saldo alvo da meta */
    BigDecimal saldoAlvo;
    /** Status da meta */
    StatusMeta status;


    /**
     * Cria uma meta financeira com nome, saldo alvo e saldo inicial.
     * @param nome Nome da meta
     * @param saldoAlvo Saldo objetivo
     * @param saldoInicial Saldo inicial
     */
    public Meta(String nome, BigDecimal saldoAlvo, BigDecimal saldoInicial) {
        super(nome, saldoInicial);
        this.saldoAlvo = saldoAlvo;
        if (saldoInicial.compareTo(saldoAlvo) > 0){
            this.status = StatusMeta.Concluida;
        }
        else{
            this.status = StatusMeta.EmAndamento;
        }
        this.saldoAtual = BigDecimal.valueOf(0);
    }
    /**
     * Adiciona valor ao saldo atual da meta e atualiza o status.
     * @param valor Valor a adicionar
     */
    @Override
    public void adicionarValor(BigDecimal valor){
        saldoAtual = saldoAtual.add(valor);
        if(saldoAtual.compareTo(saldoAlvo) >= 0){
            this.status = StatusMeta.Concluida;
        }
    }
    /**
     * Remove valor do saldo atual da meta e atualiza o status.
     * @param valor Valor a remover
     */
    public void removerValor(BigDecimal valor){
        saldoAtual = saldoAtual.subtract(valor);
        if(this.status == StatusMeta.Concluida && saldoAtual.compareTo(saldoAlvo) < 0){
            this.status = StatusMeta.EmAndamento;
        }
    }

    /**
     * Retorna o saldo alvo da meta.
     * @return saldo alvo
     */
    public BigDecimal getSaldoAlvo() {
        return saldoAlvo;
    }

    /**
     * Retorna o status da meta.
     * @return status
     */
    public StatusMeta getStatus() {
        return status;
    }

    /**
     * Define o saldo alvo da meta.
     * @param saldoAlvo Saldo objetivo
     */
    public void setSaldoAlvo(BigDecimal saldoAlvo) {
        this.saldoAlvo = saldoAlvo;
    }

    /**
     * Define o status da meta.
     * @param status StatusMeta
     */
    public void setStatus(StatusMeta status) {
        this.status = status;
    }

    /**
     * Define o saldo atual da meta, com validação.
     * @param novoSaldo Novo saldo
     */
    public void setSaldoAtual(BigDecimal novoSaldo) {
        if (novoSaldo == null) {
            throw new IllegalArgumentException("O saldo não pode ser nulo.");
        }
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            this.saldoAtual = BigDecimal.ZERO; 
            System.err.println("Aviso: Tentativa de definir saldo negativo. Saldo definido como zero.");
        } else {
            this.saldoAtual = novoSaldo;
        }
        if (this.saldoAtual.compareTo(this.saldoAlvo) >= 0) {
            this.status = StatusMeta.Concluida;
        } else {
            this.status = StatusMeta.EmAndamento;
        }
    }
}