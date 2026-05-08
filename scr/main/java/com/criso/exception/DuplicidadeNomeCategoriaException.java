package com.criso.exception;

public class DuplicidadeNomeCategoriaException extends Exception {
    public static final long serialVersionUID = 1L;

    public DuplicidadeNomeCategoriaException() {
        super("Já existe uma Categoria com este nome. Por favor, escolha um novo nome.");
    }

    public DuplicidadeNomeCategoriaException(String message) {
        super(message);
    }

    public DuplicidadeNomeCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicidadeNomeCategoriaException(Throwable cause) {
        super(cause);
    }
}
