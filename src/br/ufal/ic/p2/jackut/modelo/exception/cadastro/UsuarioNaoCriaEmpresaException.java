package br.ufal.ic.p2.jackut.modelo.exception.cadastro;

public class UsuarioNaoCriaEmpresaException extends Exception {
    public UsuarioNaoCriaEmpresaException(){
        super("Usuario nao pode criar uma empresa");
    }
}
