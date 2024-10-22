package br.ufal.ic.p2.jackut.modelo.exception.verificacao;

public class NaoPossivelLiberarPedidoException extends Exception{
    public NaoPossivelLiberarPedidoException(){
        super("Nao e possivel liberar um produto que nao esta sendo preparado");
    }
}
