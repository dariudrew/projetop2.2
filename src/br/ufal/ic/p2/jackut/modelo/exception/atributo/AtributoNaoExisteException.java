package br.ufal.ic.p2.jackut.modelo.exception.atributo;

public class AtributoNaoExisteException extends Exception{
    public AtributoNaoExisteException(){
        super("Atributo nao existe");
    }
}
