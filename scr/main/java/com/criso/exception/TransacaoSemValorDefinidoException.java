package com.criso.exception;

public class TransacaoSemValorDefinidoException extends Exception {
    public static final long serialVersionUID = 1L;

    public TransacaoSemValorDefinidoException() {
        super("A transação possui o valor de R$ 0,00. Deseja proseguir com esse valor?");
    }

    public TransacaoSemValorDefinidoException(String message) {
        super(message);
    }

    public TransacaoSemValorDefinidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransacaoSemValorDefinidoException(Throwable cause) {
        super(cause);
    }
    
}
