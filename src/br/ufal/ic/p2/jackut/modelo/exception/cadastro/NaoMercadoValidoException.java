package br.ufal.ic.p2.jackut.modelo.exception.cadastro;

public class NaoMercadoValidoException extends Exception{
    public NaoMercadoValidoException(){
        super("Nao e um mercado valido");
    }
}
