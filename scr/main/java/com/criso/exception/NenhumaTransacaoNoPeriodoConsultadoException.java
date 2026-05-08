package com.criso.exception;

public class NenhumaTransacaoNoPeriodoConsultadoException extends Exception {
    public static final long serialVersionUID = 1L;

    public NenhumaTransacaoNoPeriodoConsultadoException() {
        super("Nenhuma transação encontrada no período consultado.");
    }
    public NenhumaTransacaoNoPeriodoConsultadoException(String message) {
        super(message);
    }
    public NenhumaTransacaoNoPeriodoConsultadoException(String message, Throwable cause) {
        super(message, cause);
    }
    public NenhumaTransacaoNoPeriodoConsultadoException(Throwable cause) {
        super(cause);
    }
    
}
