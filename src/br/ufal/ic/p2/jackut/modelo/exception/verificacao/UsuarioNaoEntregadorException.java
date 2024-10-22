package br.ufal.ic.p2.jackut.modelo.exception.verificacao;

public class UsuarioNaoEntregadorException extends Exception {
    public UsuarioNaoEntregadorException(){
        super("Usuario nao e um entregador");
    }

}
