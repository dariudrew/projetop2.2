package br.ufal.ic.p2.jackut.modelo.exception.cadastro;

public class EntregadorSemEmpresaException extends Exception{
    public EntregadorSemEmpresaException(){
        super("Entregador nao estar em nenhuma empresa.");
    }
}