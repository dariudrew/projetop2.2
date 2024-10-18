package br.ufal.ic.p2.jackut.modelo.exception;

public class PedidoLiberadoException extends Exception{
    public PedidoLiberadoException(){
        super("Pedido ja liberado");
    }
}
