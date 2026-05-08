package com.criso.exception;

public class DuplicidadeNomeCaixaException extends Exception {
    public static final long serialVersionUID = 1L;

    public DuplicidadeNomeCaixaException() {
        super("Já existe um Caixa com este nome. Por favor, escolha um novo nome.");
    }

    public DuplicidadeNomeCaixaException(String message) {
        super(message);
    }

    public DuplicidadeNomeCaixaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicidadeNomeCaixaException(Throwable cause) {
        super(cause);
    }
    
}

