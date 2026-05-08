package com.criso.exception;


public class TransacaoSemDataDefinidaException extends Exception {
    public static final long serialVersionUID = 1L;

    public TransacaoSemDataDefinidaException() {
        super("A transação não possui uma data definida. Por favor, defina uma data antes de prosseguir.");
    }

    public TransacaoSemDataDefinidaException(String message) {
        super(message);
    }

    public TransacaoSemDataDefinidaException(String message, Throwable cause) {
        super(message, cause);
    
}
    public TransacaoSemDataDefinidaException(Throwable cause) {
        super(cause);
    }
    
}