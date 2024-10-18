package br.ufal.ic.p2.jackut.modelo.exception;

public class NaoExisteEntregaException extends Exception{
    public NaoExisteEntregaException(){
        super("Nao existe entrega com esse id");
    }

}
