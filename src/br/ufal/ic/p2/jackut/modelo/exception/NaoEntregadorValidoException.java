package br.ufal.ic.p2.jackut.modelo.exception;

public class NaoEntregadorValidoException extends Exception {
    public NaoEntregadorValidoException(){
        super("Nao e um entregador valido");
    }

}
