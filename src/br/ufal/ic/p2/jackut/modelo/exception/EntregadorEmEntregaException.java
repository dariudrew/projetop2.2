package br.ufal.ic.p2.jackut.modelo.exception;

public class EntregadorEmEntregaException extends Exception{
    public EntregadorEmEntregaException(){
        super("Entregador ainda em entrega");
    }
}
