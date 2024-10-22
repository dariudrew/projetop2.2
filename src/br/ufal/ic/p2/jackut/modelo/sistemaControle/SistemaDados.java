package br.ufal.ic.p2.jackut.modelo.sistemaControle;

import br.ufal.ic.p2.jackut.modelo.exception.atributo.*;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoCadastradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.ProdutoNaoEncontradoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.*;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.NaoPossivelRemoverProdutoException;
import br.ufal.ic.p2.jackut.modelo.xml.XML;
import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;
import br.ufal.ic.p2.jackut.modelo.entrega.Entrega;
import br.ufal.ic.p2.jackut.modelo.produto.Produto;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SistemaDados {


    int contadorID = 1;
    int contadorIdEmpresa = 1;
    int contadorIdProduto = 1;
    int contadorIdPedido = 1;
    int contadorIdEntrega = 1;
    Map<Integer, Empresa> empresasPorID = new HashMap<>();
    Map<Integer, Usuario> usuariosPorID = new HashMap<>();
    Map<Integer, Produto> produtosPorID = new HashMap<>();
    Map<Integer, Pedido> pedidosPorID = new HashMap<>();
    Map<Integer, Entrega> entregasPorID = new HashMap<>();
    XML xml = new XML();

    public void zerarSistema(){

        while(contadorID > 0 || contadorIdEmpresa > 0 || contadorIdEntrega > 0 ||
                contadorIdProduto > 0 || contadorIdPedido > 0){

            contadorID--;
            contadorIdEmpresa--;
            contadorIdProduto--;
            contadorIdPedido--;
            contadorIdEntrega--;

            if(contadorID > 0){
                usuariosPorID.remove(contadorID);
            }
            if(contadorIdEmpresa > 0){
                empresasPorID.remove(contadorIdEmpresa);
            }
            if(contadorIdProduto > 0){
                produtosPorID.remove(contadorIdProduto);
            }
            if(contadorIdPedido > 0){
                pedidosPorID.remove(contadorIdPedido);
            }
            if(contadorIdEntrega > 0){
                entregasPorID.remove(contadorIdEntrega);
            }

        }
        contadorID = 1;
        contadorIdEmpresa = 1;
        contadorIdProduto = 1;
        contadorIdPedido = 1;
        contadorIdEntrega = 1;
        xml.apagarXML();
    }

    protected void validaDados(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidaException, EnderecoInvalidoException {
        if(validaNome(nome)){
            throw new NomeInvalidoException();
        }
        if(validaSenha(senha)){
            throw new SenhaInvalidaException();
        }
        if(validaEndereco(endereco)){
            throw new EnderecoInvalidoException();
        }
        if(validaEmail(email)){
            throw new EmailInvalidoException();
        }
    }


    protected void validaCPF(String cpf) throws CPFInvalidoException {
        if(cpf == null || cpf.isEmpty() || !cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")){
            throw new CPFInvalidoException();
        }
    }

    protected boolean validaEmail(String email) {
        return email == null || email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    protected boolean validaSenha(String senha) {
        return senha == null || senha.trim().isEmpty();
    }

    protected boolean validaEndereco(String endereco){
        return endereco == null || endereco.trim().isEmpty();
    }
    protected boolean validaNome(String nome){
        return nome == null || nome.isEmpty() || nome.isBlank();
    }
    protected void validaDadosGeraisEmpresa(String tipoEmpresa, int dono, String nomeEmpresa, String endereco)
            throws UsuarioNaoCadastradoException, UsuarioNaoCriaEmpresaException, TipoEmpresaInvalidoException, NomeInvalidoException, EmpresaEnderecoInvalidoException {

        if(!(usuariosPorID.containsKey(dono))){
            throw new UsuarioNaoCadastradoException();
        }
        else if(!usuariosPorID.get(dono).getTipoObjeto().matches("donoEmpresa")){
            throw new UsuarioNaoCriaEmpresaException();
        }
        else if(validaNome(tipoEmpresa)){
            throw new TipoEmpresaInvalidoException();
        }
        else if(validaNome(nomeEmpresa)){
            throw new NomeInvalidoException();
        }
        else if(validaNome(endereco)){
            throw new EmpresaEnderecoInvalidoException();
        }
    }

    protected void validaDadosRestaurante(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws UsuarioNaoCadastradoException, UsuarioNaoCriaEmpresaException, EmpresaEnderecoInvalidoException, EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException, EmpresaTipoCozinhaInvalidoException, TipoEmpresaInvalidoException, NomeInvalidoException {
        validaDadosGeraisEmpresa(tipoEmpresa, dono, nome, endereco);
        if(validaNome(tipoCozinha)){
            throw new EmpresaTipoCozinhaInvalidoException();
        }
        verificaEmpresa(nome, dono, endereco);
    }
    protected void validaDadosMercado(String tipoEmpresa, int dono, String nomeMercado, String endereco, String abre, String fecha, String tipoMercado)
            throws TipoEmpresaInvalidoException, HorarioInvalidoException, FormatoHoraInvalidoException,
            EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException, EmpresaEnderecoInvalidoException,
            UsuarioNaoCriaEmpresaException, UsuarioNaoCadastradoException, NomeInvalidoException, TipoMercadoInvalidoException {

        validaDadosGeraisEmpresa(tipoEmpresa, dono, nomeMercado, endereco);
        if(validaNome(tipoMercado)){
            throw new TipoMercadoInvalidoException();
        }
        horarioFormato(abre, fecha);
        horarioNull(abre, fecha);

        horarioPadrao(abre, fecha);
        verificaEmpresa(nomeMercado,dono,endereco);

    }

    protected void validaDadosFarmacia(String tipoEmpresa, int dono, String nomeFarmacia, String endereco, int numeroFuncionarios)
            throws UsuarioNaoCadastradoException, UsuarioNaoCriaEmpresaException, TipoEmpresaInvalidoException,
            NomeInvalidoException, EmpresaEnderecoInvalidoException, NumeroFuncionariosException,
            EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException {

        validaDadosGeraisEmpresa(tipoEmpresa, dono, nomeFarmacia, endereco);
        if(numeroFuncionarios < 1){
            throw new NumeroFuncionariosException();
        }
        verificaEmpresa(nomeFarmacia, dono, endereco);
    }

    protected void verificaEmpresa(String nome, int dono, String endereco) throws EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException {
        
        for (Empresa empresa : empresasPorID.values()) {
            if(empresa.getNomeEmpresa().matches(nome)){
                if(empresa.getIdDono() == dono) {
                    if(empresa.getEnderecoEmpresa().matches(endereco)) {
                        throw new EmpresaNomeEnderecoEmUsoException();
                    }
                }
                else {
                    throw new EmpresaNomeExisteException();
                }
            }
        }
    }

    protected void horarioFormato(String abre, String fecha) throws FormatoHoraInvalidoException{
        if((abre != null && fecha != null) && (abre.isEmpty() || fecha.isEmpty() || !abre.matches("^\\d{2}:\\d{2}$") || !fecha.matches("^\\d{2}:\\d{2}$"))){
            throw new FormatoHoraInvalidoException();
        }

    }
    protected void horarioNull(String abre, String fecha) throws HorarioInvalidoException {
        if(abre == null || fecha == null ) {
            throw new HorarioInvalidoException();
        }

    }
    protected void horarioPadrao(String abre, String fecha) throws HorarioInvalidoException {
        int horaAbre = Integer.parseInt(abre.substring(0, 2));
        int horaFecha = Integer.parseInt(fecha.substring(0, 2));
        int minutoAbre = Integer.parseInt(abre.substring(3, 5));
        int minutoFecha = Integer.parseInt(fecha.substring(3, 5));
        int diferencaMinutos;
        if (horaAbre > 23 || horaFecha > 23 || minutoAbre > 59 || minutoFecha > 59) {
            throw new HorarioInvalidoException();
        } else if (horaAbre < 0 || horaFecha < 0 || minutoAbre < 0 || minutoFecha < 0) {
            throw new HorarioInvalidoException();
        }


        LocalTime inicio = LocalTime.of(horaAbre, minutoAbre);
        LocalTime fim = LocalTime.of(horaFecha, minutoFecha);


        Duration duracao = Duration.between(inicio, fim);
        if (duracao.isNegative()) {
            duracao = duracao.plusHours(24);
        }
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();

        diferencaMinutos = Math.toIntExact(horas) * 60 + Math.toIntExact(minutos);

        if (diferencaMinutos < 360 || horaAbre > 12) {
            throw new HorarioInvalidoException();
        }
    }


    protected void validaDadosProduto(int idEmpresa, String nomeProduto, float valorProduto, String categoriaProduto)
            throws EmpresaNaoCadastradaException, NomeInvalidoException, ProdutoValorInvalidoExcepion,
            ProdutoCategoriaInvalidaException, ProdutoJaExisteNaEmpresaException {

        if(!empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoCadastradaException();
        }
        if(validaNome(nomeProduto)){
            throw new NomeInvalidoException();
        }
        if(validaNome(categoriaProduto)){
            throw new ProdutoCategoriaInvalidaException();
        }
        if(valorProduto <= 0){
            throw new ProdutoValorInvalidoExcepion();
        }

        if(!produtosPorID.isEmpty()){
            for(Produto produto: produtosPorID.values()){
                if(produto.getNomeProduto().matches(nomeProduto) && produto.getIdEmpresa() == idEmpresa){
                    if(nomeProduto.matches("Refrigerante"))                    {
                        throw new ProdutoJaExisteNaEmpresaException();
                    }
                }
            }
        }
    }

    protected void validaDadosPedido(int idCliente, int idEmpresa)
            throws UsuarioNaoCadastradoException, EmpresaNaoCadastradaException, DonoNaoFazPedidoException,
            NaoPermitidoPedidosAbertoMesmaEmpresaException {

        if(!usuariosPorID.containsKey(idCliente)){
            throw new UsuarioNaoCadastradoException();
        }
        if(!empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoCadastradaException();
        }
        if(!usuariosPorID.get(idCliente).getTipoObjeto().matches("cliente")){
            throw new DonoNaoFazPedidoException();
        }

        for(Pedido pedido: pedidosPorID.values()){//

            if(pedido.getIdCliente() == idCliente && pedido.getIdEmpresa() == idEmpresa){

                if(pedido.getEstadoPedido().equals("aberto")){
                    throw new NaoPermitidoPedidosAbertoMesmaEmpresaException();
                }

            }
        }
    }

    protected static String getStringProdutos(String produto, Pedido pedido) throws NaoPossivelRemoverProdutoException, ProdutoNaoEncontradoException {
        String produtosDoPedido = pedido.getProdutos();

        if (!(pedido.getEstadoPedido().equals("aberto"))) {
            throw new NaoPossivelRemoverProdutoException();
        } else if (!pedido.getProdutos().contains(produto)) {
            throw new ProdutoNaoEncontradoException();
        }

        if (produtosDoPedido.contains(", " + produto + ",")) {
            produtosDoPedido = produtosDoPedido.replaceFirst(", " + produto, "");
        } else if (produtosDoPedido.contains("[" + produto + ",")) {
            produtosDoPedido = produtosDoPedido.replaceFirst(produto + ", ", "");
        } else if (produtosDoPedido.contains("[" + produto)) {
            produtosDoPedido = produtosDoPedido.replaceFirst(produto, "");
        } else if (produtosDoPedido.contains(", " + produto + "]")) {
            produtosDoPedido = produtosDoPedido.replaceFirst(", " + produto, "");
        }
        return produtosDoPedido;
    }

}

