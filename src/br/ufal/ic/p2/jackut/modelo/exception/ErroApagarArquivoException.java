package br.ufal.ic.p2.jackut.modelo.exception;

public class ErroApagarArquivoException extends Exception{
    public ErroApagarArquivoException(){
        super("Erro ao tentar apagar arquivo");
    }
}
