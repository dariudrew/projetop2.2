package br.ufal.ic.p2.jackut.modelo.exception.atributo;

public class EmpresaNomeEnderecoEmUsoException extends Exception{
    public EmpresaNomeEnderecoEmUsoException(){
        super("Proibido cadastrar duas empresas com o mesmo nome e local");
    }
}
