package com.criso.exception;

public class PeriodoDaConsultaDeTransacoesInvalidoException extends Exception {
    public static final long serialVersionUID = 1L;

    public PeriodoDaConsultaDeTransacoesInvalidoException() {
        super("Período de consulta inválido. Por favor, verifique as datas informadas.");
    }

    public PeriodoDaConsultaDeTransacoesInvalidoException(String message) {
        super(message);
    }

    public PeriodoDaConsultaDeTransacoesInvalidoException(String message, Throwable cause) {
        super(message, cause);
    
}
    public PeriodoDaConsultaDeTransacoesInvalidoException(Throwable cause) {
        super(cause);
    }

}