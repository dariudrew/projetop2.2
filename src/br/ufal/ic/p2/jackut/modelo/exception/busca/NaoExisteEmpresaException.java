package br.ufal.ic.p2.jackut.modelo.exception.busca;

public class NaoExisteEmpresaException extends Exception{
    public NaoExisteEmpresaException(){
        super("Nao existe empresa com esse nome");
    }
}
