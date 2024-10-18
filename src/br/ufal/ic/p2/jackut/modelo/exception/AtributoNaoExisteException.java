package br.ufal.ic.p2.jackut.modelo.exception;

public class AtributoNaoExisteException extends Exception{
    public AtributoNaoExisteException(){
        super("Atributo nao existe");
    }
}
