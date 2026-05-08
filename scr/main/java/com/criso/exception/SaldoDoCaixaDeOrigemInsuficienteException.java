package com.criso.exception;

public class SaldoDoCaixaDeOrigemInsuficienteException extends Exception{
    public static final long serialVersionUID = 1L;

    public SaldoDoCaixaDeOrigemInsuficienteException() {
        super("O Caixa de Origem não possui saldo suficiente para concluir essa transação. Deseja continuar?");
    }

    public SaldoDoCaixaDeOrigemInsuficienteException(String message) {
        super(message);
    }

    public SaldoDoCaixaDeOrigemInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    
}
    public SaldoDoCaixaDeOrigemInsuficienteException(Throwable cause) {
        super(cause);
    }
}
