package br.ufal.ic.p2.jackut.modelo.exception;

public class NaoHaEntregaException extends Exception{
    public NaoHaEntregaException(){
        super("Nao existe nada para ser entregue com esse id");
    }
}
