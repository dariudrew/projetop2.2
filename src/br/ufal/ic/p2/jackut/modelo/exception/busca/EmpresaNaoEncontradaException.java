package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class EmpresaNaoEncontradaException extends Exception{
    public EmpresaNaoEncontradaException(){
        super("Empresa nao encontrada");
    }

}
