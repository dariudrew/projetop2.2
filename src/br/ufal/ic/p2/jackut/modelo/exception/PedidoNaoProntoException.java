package br.ufal.ic.p2.jackut.modelo.exception;

public class PedidoNaoProntoException extends Exception{
    public PedidoNaoProntoException(){
        super("Pedido nao esta pronto para entrega");
    }
}
