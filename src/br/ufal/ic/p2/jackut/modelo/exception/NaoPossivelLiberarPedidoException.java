package br.ufal.ic.p2.jackut.modelo.exception;

public class NaoPossivelLiberarPedidoException extends Exception{
    public NaoPossivelLiberarPedidoException(){
        super("Nao e possivel liberar um produto que nao esta sendo preparado");
    }
}
