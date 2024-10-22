package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class NaoExisteNadaEntregaException extends Exception {
    public NaoExisteNadaEntregaException(){
        super("Nao existe nada para ser entregue com esse id");
    }
}
