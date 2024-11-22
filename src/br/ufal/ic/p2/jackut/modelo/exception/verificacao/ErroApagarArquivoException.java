package br.ufal.ic.p2.jackut.modelo.exception.verificacao;

public class ErroApagarArquivoException extends Exception{
    public ErroApagarArquivoException(){
        super("Erro ao tentar apagar arquivo");
    }

    public ErroApagarArquivoException(String string) {
        //
    }
}
