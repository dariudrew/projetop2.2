package br.ufal.ic.p2.jackut.modelo.sistemaControle;


import br.ufal.ic.p2.jackut.modelo.exception.atributo.*;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeVeiculoInvalidoException;
import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;


import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoCadastradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoEncontradaException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.PlacaInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.ErroApagarArquivoException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.FormatoPlacaException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.LoginSenhaException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.UsuarioNaoEntregadorException;
import br.ufal.ic.p2.jackut.modelo.usuario.Cliente;
import br.ufal.ic.p2.jackut.modelo.usuario.DonoEmpresa;
import br.ufal.ic.p2.jackut.modelo.usuario.Entregador;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;


import java.lang.reflect.Method;


public class SistemaUsuario {
    private final SistemaDados dados;


  public SistemaUsuario(SistemaDados dados){
      this.dados = dados;
      dados.xml.criarXML();
  }



    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, EnderecoInvalidoException, SenhaInvalidaException, EmailJaExisteException, AtributoNaoExisteException, ErroApagarArquivoException {
        dados.carregarTodosDados();
        dados.validaDados(nome, email, senha, endereco);
        if(getIdUsuario("Email", email) > 0){
            throw new EmailJaExisteException();
        }
        Cliente cliente = new Cliente(dados.contadorID, nome, email, senha, endereco);

        dados.xml.inserirUsuario(cliente);
        dados.usuariosPorID.put(dados.contadorID, cliente);
        dados.contadorID+=1;
    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws NomeInvalidoException, EmailInvalidoException, EnderecoInvalidoException, SenhaInvalidaException, EmailJaExisteException, CPFInvalidoException, AtributoNaoExisteException, ErroApagarArquivoException {
        dados.carregarTodosDados();
        dados.validaCPF(cpf);
        dados.validaDados(nome, email, senha, endereco);
        if(getIdUsuario("Email", email) > 0){
            throw new EmailJaExisteException();
        }
        DonoEmpresa donoEmpresa = new DonoEmpresa(dados.contadorID, nome, email, senha, endereco, cpf);
       dados.xml.inserirUsuario(donoEmpresa);

        dados.usuariosPorID.put(dados.contadorID, donoEmpresa);
        dados.contadorID+=1;
    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws EmailJaExisteException, NomeInvalidoException, EmailInvalidoException, EnderecoInvalidoException, SenhaInvalidaException, NomeVeiculoInvalidoException, PlacaInvalidoException, FormatoPlacaException, AtributoNaoExisteException, ErroApagarArquivoException {
        dados.carregarTodosDados();
        dados.validaDados(nome, email, senha, endereco);
        if(dados.validaNome(veiculo)){
            throw new NomeVeiculoInvalidoException();
        }
        else if(dados.validaNome(placa) || getIdUsuario("Placa", placa) > 0){
            throw new PlacaInvalidoException();
        }

        if(getIdUsuario("Email", email) > 0){
            throw new EmailJaExisteException();
        }

        Entregador entregador = new Entregador(dados.contadorID, nome, email, senha, endereco, veiculo, placa);
        dados.xml.inserirUsuario(entregador);
        dados.usuariosPorID.put(dados.contadorID, entregador);
        dados.contadorID++;

    }





    public int getIdUsuario(String tipoMetodo, String atributo) throws AtributoNaoExisteException {
        try {
            String metodoNome = "get" + tipoMetodo;

            if(!dados.usuariosPorID.isEmpty())
            {
                for (Usuario usuario : dados.usuariosPorID.values()) {
                    Method metodo = usuario.getClass().getMethod(metodoNome);

                    if (metodo.invoke(usuario).equals(atributo)) {
                        return usuario.getId();
                    }
                }
            }

        } catch (Exception e) {
            throw new AtributoNaoExisteException();
        }
        return 0;
    }



    public String getAtributoUsuario(int id, String atributo) throws UsuarioNaoCadastradoException {

        if(dados.usuariosPorID.containsKey(id)){

            Usuario usuario = dados.usuariosPorID.get(id);
            switch (atributo){
                case "nome":
                    return usuario.getNome();
                case "senha":
                    return usuario.getSenha();
                case "email":
                    return usuario.getEmail();
                case "endereco":
                    return usuario.getEndereco();
                case "cpf":
                    return  usuario.getCpf();
                case "veiculo":
                    return usuario.getVeiculo();
                case "placa":
                    return usuario.getPlaca();
            }
        }
        else{
            throw new UsuarioNaoCadastradoException();
        }
        return "";
    }

    public int login (String email, String senha) throws LoginSenhaException, AtributoNaoExisteException {

        if(dados.validaEmail(email) || dados.validaSenha(senha)){
            throw new LoginSenhaException();
        }
        else{
             int id = getIdUsuario("Email", email);
            if( id > 0)
            {
                Usuario usuario = dados.usuariosPorID.get(id);
                if(usuario.getSenha().matches(senha)){
                    return id;
                }
                else{
                    throw new LoginSenhaException();
                }
            }
        }

        return 0;
    }

    public void encerrarSistema(){
    }

    public void cadastrarEntregador(int idEmpresa, int entregador) throws EmpresaNaoEncontradaException, UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, ErroApagarArquivoException {

      if(dados.empresasPorID.isEmpty()|| !dados.empresasPorID.containsKey(idEmpresa)){
          throw new EmpresaNaoEncontradaException();
      }
      if(dados.usuariosPorID.isEmpty() || !dados.usuariosPorID.containsKey(entregador)){
          throw new UsuarioNaoCadastradoException();
      }

      Usuario usuario = dados.usuariosPorID.get(entregador);
      if(!usuario.getTipoObjeto().matches("entregador")){
          throw new UsuarioNaoEntregadorException();
      }


        Empresa empresa = dados.empresasPorID.get(idEmpresa);

        String str = empresa.getEntregadoresVinculados(); //empresa.entregadoresa
        str = str.replaceAll("]}", "");
        if(str.matches(".*[a-zA-Z0-9]$")){ // se h� produtos
            str = str.concat(", "+usuario.getEmail()+"]}");
        }
        else{
            str = str.concat(usuario.getEmail()+"]}");
        }
        empresa.setEntregadoresVinculados(str);
        dados.xml.editarEmpresa(empresa);
    }

    public String getEntregadores(int idEmpresa) throws EmpresaNaoEncontradaException {
        dados.empresasPorID.clear();
        dados.carregarEmpresasDoXML();
        if(dados.empresasPorID.isEmpty() || !dados.empresasPorID.containsKey(idEmpresa)){
          throw new EmpresaNaoEncontradaException();
        }
        Empresa empresa = dados.empresasPorID.get(idEmpresa);
        return empresa.getEntregadoresVinculados();
    }

    public String getEmpresas(int entregador) throws UsuarioNaoCadastradoException, EmpresaNaoCadastradaException, UsuarioNaoEntregadorException {
      if(dados.usuariosPorID.isEmpty() || !dados.usuariosPorID.containsKey(entregador)){
          throw new UsuarioNaoCadastradoException();
      }
      else if(dados.empresasPorID.isEmpty()){
          throw new EmpresaNaoCadastradaException();
      }
      else if(!dados.usuariosPorID.get(entregador).getTipoObjeto().matches("entregador")){
          throw new UsuarioNaoEntregadorException();
      }
      String listaEmpresas = "";
      Usuario usuario = dados.usuariosPorID.get(entregador);

        int qntEmpresas = dados.empresasPorID.size();

        for(int i = 1; i <= qntEmpresas; i++) {

            String listaEntregadores;
            Empresa empresa = dados.empresasPorID.get(i);
            listaEntregadores = empresa.getEntregadoresVinculados();
            listaEntregadores = listaEntregadores.replaceAll("[\\[\\]{}]", "");

            String[] listaEntregadoresaArray = listaEntregadores.split(", ");

            if (i == 1) {
                listaEmpresas = listaEmpresas.concat("{[");
            }
            for (String s : listaEntregadoresaArray) {

                if (usuario.getEmail().matches(s)) {
                    if (listaEmpresas.matches("^\\{\\[\\[.*")) {       //veifica se a string terminar com letras, para saber quando add virgula e espa�amento entre as produtos.
                        listaEmpresas = listaEmpresas.concat(", ");
                    }

                    listaEmpresas = listaEmpresas.concat("[" + empresa.getNomeEmpresa() + ", " + empresa.getEnderecoEmpresa() + "]");
                    break;
                }
            }
            if (i == qntEmpresas) {
                listaEmpresas = listaEmpresas.concat("]}");
            }

        }
        return listaEmpresas;
    }
}
