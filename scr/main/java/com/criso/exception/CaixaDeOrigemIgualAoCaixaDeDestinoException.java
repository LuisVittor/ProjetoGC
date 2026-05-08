package com.criso.exception;

public class CaixaDeOrigemIgualAoCaixaDeDestinoException extends Exception {
    public static final long serialVersionUID = 1L;

    public CaixaDeOrigemIgualAoCaixaDeDestinoException() {
        super("O caixa de Origem deve ser diferente do caixa de Destino.");
    }

    public CaixaDeOrigemIgualAoCaixaDeDestinoException(String message) {
        super(message);
    }

    public CaixaDeOrigemIgualAoCaixaDeDestinoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaixaDeOrigemIgualAoCaixaDeDestinoException(Throwable cause) {
        super(cause);
    }
}
