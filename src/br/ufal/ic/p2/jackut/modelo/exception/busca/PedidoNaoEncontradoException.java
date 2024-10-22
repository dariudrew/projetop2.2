package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class PedidoNaoEncontradoException extends Exception{
    public PedidoNaoEncontradoException(){
        super("Pedido nao encontrado");
    }
}
