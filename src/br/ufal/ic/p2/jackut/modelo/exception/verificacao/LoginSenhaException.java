package br.ufal.ic.p2.jackut.modelo.exception.verificacao;

public class LoginSenhaException extends Exception{

    public LoginSenhaException(){
        super("Login ou senha invalidos");
    }
}
