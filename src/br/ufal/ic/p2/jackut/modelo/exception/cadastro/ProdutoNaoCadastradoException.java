package br.ufal.ic.p2.jackut.modelo.exception.cadastro;

public class ProdutoNaoCadastradoException extends Exception{
    public ProdutoNaoCadastradoException(){
        super("Produto nao cadastrado");
    }
}
