package br.ufal.ic.p2.jackut.modelo.exception.cadastro;

public class ProdutoJaExisteNaEmpresaException extends Exception{
    public ProdutoJaExisteNaEmpresaException(){
        super("Ja existe um produto com esse nome para essa empresa");
    }
}
