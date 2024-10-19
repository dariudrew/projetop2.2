package br.ufal.ic.p2.jackut.modelo.usuario;

public abstract class Usuario {
    private final int id;
    private final String nome;
    private final String email;
    private final String senha;
    private final String endereco;

    public Usuario(int id, String nome, String email, String senha, String endereco){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getEndereco() {
        return endereco;
    }
    public String getCpf(){
        return "cpf";
    }

    public String getTipoObjeto() {
        return "Usuario";
    }

    public String getVeiculo() {
        return "";
    }

    public String getPlaca() {
        return "";
    }
}
