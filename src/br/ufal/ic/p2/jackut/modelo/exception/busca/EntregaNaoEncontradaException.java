package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class EntregaNaoEncontradaException extends Exception{
    public EntregaNaoEncontradaException(){
        super("Entrega nao encontrada");
    }
}
