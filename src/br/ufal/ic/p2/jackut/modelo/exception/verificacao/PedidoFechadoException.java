package br.ufal.ic.p2.jackut.modelo.exception.verificacao;

public class PedidoFechadoException extends Exception{
    public PedidoFechadoException(){
        super("Nao e possivel adicionar produtos a um pedido fechado");
    }

}
