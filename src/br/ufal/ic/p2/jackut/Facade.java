package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.modelo.exception.atributo.*;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.EmpresaNomeEnderecoEmUsoException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.EmpresaNomeExisteException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeVeiculoInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.*;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.*;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.*;
import br.ufal.ic.p2.jackut.modelo.sistemaControle.*;


public class Facade {
    private SistemaDados dados;
    private SistemaUsuario sistemaUsuario;
    private SistemaEmpresa sistemaEmpresa;
    private SistemaProduto sistemaProduto;
    private SistemaPedido sistemaPedido;
    private SistemaEntrega sistemaEntrega;
    public Facade(){
        dados = new SistemaDados();
        sistemaUsuario = new SistemaUsuario(dados);
        sistemaEmpresa = new SistemaEmpresa(dados);
        sistemaProduto = new SistemaProduto(dados);
        sistemaPedido  = new SistemaPedido(dados);
        sistemaEntrega = new SistemaEntrega(dados);

    }


    public void zerarSistema(){
        dados.zerarSistema();
    }

    public void encerrarSistema(){
         sistemaUsuario.encerrarSistema();
    }

    public String getAtributoUsuario(int id, String nome) throws UsuarioNaoCadastradoException {
        return sistemaUsuario.getAtributoUsuario(id, nome);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, EnderecoInvalidoException, SenhaInvalidaException, EmailJaExisteException, AtributoNaoExisteException, ErroApagarArquivoException {
        sistemaUsuario.criarUsuario(nome, email, senha, endereco);

    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws NomeInvalidoException, EmailInvalidoException, EnderecoInvalidoException, SenhaInvalidaException, EmailJaExisteException, CPFInvalidoException, AtributoNaoExisteException, ErroApagarArquivoException {
        sistemaUsuario.criarUsuario(nome, email, senha, endereco, cpf);

    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa)
            throws EmailJaExisteException, NomeInvalidoException, EmailInvalidoException, FormatoPlacaException, EnderecoInvalidoException, SenhaInvalidaException, NomeVeiculoInvalidoException, PlacaInvalidoException, AtributoNaoExisteException {
        sistemaUsuario.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }
    public int login(String email, String senha) throws LoginSenhaException, AtributoNaoExisteException {
        return sistemaUsuario.login(email, senha);
    }
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws EmpresaEnderecoInvalidoException, UsuarioNaoCriaEmpresaException, EmpresaNomeEnderecoEmUsoException,
            EmpresaTipoCozinhaInvalidoException, UsuarioNaoCadastradoException, EmpresaNomeExisteException, TipoEmpresaInvalidoException, NomeInvalidoException, ErroApagarArquivoException {
        return sistemaEmpresa.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nomeMercado, String endereco, String abre, String fecha, String tipoMercado)
            throws UsuarioNaoCriaEmpresaException, EmpresaEnderecoInvalidoException, UsuarioNaoCadastradoException, EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException, FormatoHoraInvalidoException, HorarioInvalidoException, TipoEmpresaInvalidoException, NomeInvalidoException, TipoMercadoInvalidoException, ErroApagarArquivoException {
        return sistemaEmpresa.criarEmpresa(tipoEmpresa, dono, nomeMercado, endereco, abre, fecha,tipoMercado);
    }
    public int criarEmpresa(String tipoEmpresa, int dono, String nomeFarmacia, String endereco, boolean abre24Horas, int numeroFuncionarios) throws UsuarioNaoCriaEmpresaException, EmpresaEnderecoInvalidoException, UsuarioNaoCadastradoException, TipoEmpresaInvalidoException, NomeInvalidoException, EmpresaNomeEnderecoEmUsoException, NumeroFuncionariosException, EmpresaNomeExisteException, ErroApagarArquivoException {
        return sistemaEmpresa.criarEmpresa(tipoEmpresa, dono, nomeFarmacia, endereco, abre24Horas,numeroFuncionarios);
    }

    public String getEmpresasDoUsuario(int idDono) throws UsuarioNaoCriaEmpresaException {
       return sistemaEmpresa.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws UsuarioNaoCadastradoException, NomeInvalidoException, UsuarioNaoCriaEmpresaException, IndiceInvalidoException, NaoExisteEmpresaException, IndiceMaiorException {
        return sistemaEmpresa.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo) throws EmpresaNaoCadastradaException, AtributoInvalidoException {
        return sistemaEmpresa.getAtributoEmpresa(empresa, atributo);
    }

    public void alterarFuncionamento(int idEmpresa, String abre, String fecha) throws EmpresaNaoEncontradaException, HorarioInvalidoException, FormatoHoraInvalidoException, NaoMercadoValidoException {
       sistemaEmpresa.alterarFuncionamento(idEmpresa, abre, fecha);
    }

    public void cadastrarEntregador(int empresa, int entregador) throws UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, EmpresaNaoEncontradaException {
        sistemaUsuario.cadastrarEntregador(empresa, entregador);
    }

    public String getEntregadores(int empresa) throws EmpresaNaoEncontradaException {
        return sistemaUsuario.getEntregadores(empresa);
    }
    public String getEmpresas(int entregador) throws EmpresaNaoCadastradaException, UsuarioNaoCadastradoException, UsuarioNaoEntregadorException {
        return  sistemaUsuario.getEmpresas(entregador);
    }

    public int criarProduto(int idEmpresa, String nomeProduto, float valorProduto, String categoriaProduto) throws EmpresaNaoCadastradaException, NomeInvalidoException, ProdutoValorInvalidoExcepion, ProdutoCategoriaInvalidaException, ProdutoJaExisteNaEmpresaException, ErroApagarArquivoException {
        return sistemaProduto.criarProduto(idEmpresa, nomeProduto, valorProduto, categoriaProduto);
    }

    public void editarProduto(int idProduto, String nomeProduto, float valorProduto, String categoriaProduto) throws ProdutoNaoCadastradoException, NomeInvalidoException, ProdutoValorInvalidoExcepion, ProdutoCategoriaInvalidaException {
        sistemaProduto.editarProduto(idProduto, nomeProduto,valorProduto, categoriaProduto);
    }

    public String getProduto(String  nomeProduto, int idEmpresa, String atributo) throws EmpresaNaoCadastradaException, AtributoNaoExisteException, NomeInvalidoException, ProdutoNaoEncontradoException {
        return sistemaProduto.getProduto(nomeProduto, idEmpresa, atributo);
    }

    public String listarProdutos(int idEmpresa) throws EmpresaNaoEncontradaException{
        return sistemaProduto.listarProdutos(idEmpresa);
    }

    public int criarPedido(int idCliente, int idEmpresa) throws EmpresaNaoCadastradaException, DonoNaoFazPedidoException, UsuarioNaoCadastradoException, AtributoInvalidoException, NaoPermitidoPedidosAbertoMesmaEmpresaException, ErroApagarArquivoException {
        return sistemaPedido.criarPedido(idCliente, idEmpresa);
    }

    public int getNumeroPedido(int idCliente, int idEmpresa, int indice) throws PedidoNaoEncontradoException {

        return sistemaPedido.getNumeroPedido(idCliente, idEmpresa, indice);
    }

    public void adicionarProduto(int numeroPedido, int idProduto) throws NaoExistePedidoAbertoException, ProdutoNaoEncontradoException, ProdutoNaoPerteceEmpresaException, PedidoFechadoException {
        sistemaPedido.adicionarProduto(numeroPedido, idProduto);
    }

    public String getPedidos(int numeroPedido, String atributo) throws AtributoInvalidoException, AtributoNaoExisteException, PedidoNaoEncontradoException {

        return sistemaPedido.getPedidos(numeroPedido, atributo);
    }

    public void fecharPedido(int numeroPedido) throws PedidoNaoEncontradoException {
        sistemaPedido.fecharPedido(numeroPedido);
    }

    public void removerProduto(int numeroPedido, String produto) throws ProdutoInvalidoException, PedidoNaoEncontradoException, NaoPossivelRemoverProdutoException, ProdutoNaoEncontradoException, EmpresaNaoCadastradaException, AtributoNaoExisteException, NomeInvalidoException {
        sistemaPedido.removerProduto(numeroPedido, produto);
    }
    public int criarEntrega(int pedido, int idEntregador, String destino) throws UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, PedidoNaoProntoException, EntregadorEmEntregaException, ErroApagarArquivoException {
        return sistemaEntrega.criarEntrega(pedido, idEntregador, destino);
    }

    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, NaoPossivelLiberarPedidoException, PedidoLiberadoException {
        sistemaEntrega.liberarPedido(numero);
    }

    public int obterPedido(int idEntregador) throws EmpresaNaoCadastradaException, UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, EntregadorSemEmpresaException, NaoExistePedidoEntregaException {
        return sistemaEntrega.obterPedido(idEntregador);
    }

    public String getEntrega(int idEntrega, String atributo) throws AtributoInvalidoException, EntregaNaoEncontradaException, AtributoNaoExisteException {
        return sistemaEntrega.getEntrega(idEntrega, atributo);
    }
    public int getIdEntrega(int pedido) throws NaoExisteEntregaIdException, NaoExisteNadaEntregaException{
        return sistemaEntrega.getIdEntrega(pedido);
    }

    public void entregar(int idEntrega) throws NaoExisteNadaEntregaException {
        sistemaEntrega.entregar(idEntrega);
    }

}
