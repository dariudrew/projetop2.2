package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class NaoExistePedidoEntregaException extends Exception{
    public NaoExistePedidoEntregaException(){
        super("Nao existe pedido para entrega");
    }
}
