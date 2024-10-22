package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class NaoExisteEntregaIdException extends Exception{
    public NaoExisteEntregaIdException(){
        super("Nao existe entrega com esse id");
    }

}
