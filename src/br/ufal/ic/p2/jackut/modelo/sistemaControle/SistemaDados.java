package br.ufal.ic.p2.jackut.modelo.sistemaControle;

import br.ufal.ic.p2.jackut.modelo.exception.atributo.*;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoCadastradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.ProdutoNaoEncontradoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.*;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.NaoPossivelRemoverProdutoException;
import br.ufal.ic.p2.jackut.modelo.xml.XML;
import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;
import br.ufal.ic.p2.jackut.modelo.empresa.Farmacia;
import br.ufal.ic.p2.jackut.modelo.empresa.Mercado;
import br.ufal.ic.p2.jackut.modelo.empresa.Restaurante;
import br.ufal.ic.p2.jackut.modelo.entrega.Entrega;
import br.ufal.ic.p2.jackut.modelo.produto.Produto;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;
import br.ufal.ic.p2.jackut.modelo.usuario.Cliente;
import br.ufal.ic.p2.jackut.modelo.usuario.DonoEmpresa;
import br.ufal.ic.p2.jackut.modelo.usuario.Entregador;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

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

       empresasPorID.clear();
       usuariosPorID.clear();
       produtosPorID.clear();
       pedidosPorID.clear();
       entregasPorID.clear();
       
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

    // CARREGAR MAPS



    public void carregarTodosDados() {
        carregarUsuariosDoXML();
        carregarEmpresasDoXML();
        carregarProdutosDoXML();
        carregarPedidosDoXML();
        carregarEntregasDoXML();
    }

    private void carregarUsuariosDoXML() {
    Document document = xml.carregarOuCriarXML();
    if (document != null) {
        NodeList usuarioNodes = document.getElementsByTagName("usuario");

        for (int i = 0; i < usuarioNodes.getLength(); i++) {
            Element usuarioElement = (Element) usuarioNodes.item(i);

            int id = Integer.parseInt(usuarioElement.getAttribute("id"));
            String nome = usuarioElement.getElementsByTagName("nome").item(0).getTextContent();
            String email = usuarioElement.getElementsByTagName("email").item(0).getTextContent();
            String senha = usuarioElement.getElementsByTagName("senha").item(0).getTextContent();
            String endereco = usuarioElement.getElementsByTagName("endereco").item(0).getTextContent();

            Usuario usuario;
            // Verifica se o usuário é DonoEmpresa (tem CPF)
            if (usuarioElement.getElementsByTagName("cpf").getLength() > 0) {
                String cpf = usuarioElement.getElementsByTagName("cpf").item(0).getTextContent();
                usuario = new DonoEmpresa(id, nome, email, senha, endereco, cpf);
            } 
            else if (usuarioElement.getElementsByTagName("veiculo").getLength() > 0){
            
                String veiculo = usuarioElement.getElementsByTagName("veiculo").item(0).getTextContent();
                String placa = usuarioElement.getElementsByTagName("placa").item(0).getTextContent();
                usuario = new Entregador(id, nome, email, senha, endereco, veiculo, placa);
            }
            else{
                usuario = new Cliente(id, nome, email, senha, endereco);
            }

            // Adiciona no mapa de usuários
            usuariosPorID.put(id, usuario);
        }
    }
}

public void carregarEmpresasDoXML() {
    Document document = xml.carregarOuCriarXML();
    if (document != null) {
        NodeList empresaNodes = document.getElementsByTagName("empresa");

        for (int i = 0; i < empresaNodes.getLength(); i++) {
            Element empresaElement = (Element) empresaNodes.item(i);

            int id = Integer.parseInt(empresaElement.getAttribute("id"));
            int idDono = Integer.parseInt(empresaElement.getElementsByTagName("idDono").item(0).getTextContent());
            String nome = empresaElement.getElementsByTagName("nome").item(0).getTextContent();
            String endereco = empresaElement.getElementsByTagName("endereco").item(0).getTextContent();
            String entregadoresVinculados = empresaElement.getElementsByTagName("entregadoresVinculados").item(0).getTextContent();

            Empresa empresa;

            // Verifica o tipo de empresa
            if (empresaElement.getElementsByTagName("tipoCozinha").getLength() > 0) {
                String tipoCozinha = empresaElement.getElementsByTagName("tipoCozinha").item(0).getTextContent();
                empresa = new Restaurante(id, idDono, nome, endereco, tipoCozinha);
                empresa.setEntregadoresVinculados(entregadoresVinculados);
            } else if (empresaElement.getElementsByTagName("tipoMercado").getLength() > 0) {
                String abre = empresaElement.getElementsByTagName("abre").item(0).getTextContent();
                String fecha = empresaElement.getElementsByTagName("fecha").item(0).getTextContent();
                String tipoMercado = empresaElement.getElementsByTagName("tipoMercado").item(0).getTextContent();
                empresa = new Mercado(id, idDono, nome, endereco, abre, fecha, tipoMercado);
                empresa.setEntregadoresVinculados(entregadoresVinculados);

            } else if (empresaElement.getElementsByTagName("aberto24Horas").getLength() > 0) {
                boolean aberto24Horas = Boolean.parseBoolean(empresaElement.getElementsByTagName("aberto24Horas").item(0).getTextContent());
                int numeroFuncionarios = Integer.parseInt(empresaElement.getElementsByTagName("numeroFuncionarios").item(0).getTextContent());
                empresa = new Farmacia(id, idDono, nome, endereco, aberto24Horas, numeroFuncionarios);
                empresa.setEntregadoresVinculados(entregadoresVinculados);

            } else {
                // Caso não tenha nenhum tipo válido, assume uma empresa genérica
                empresa = new Empresa(id, idDono, nome, endereco);
                
            }

            // Adiciona no mapa de empresas
            empresasPorID.put(id, empresa);
        }
    }
}


private void carregarProdutosDoXML() {
    Document document = xml.carregarOuCriarXML();
    if (document != null) {
        NodeList produtoNodes = document.getElementsByTagName("produto");

        for (int i = 0; i < produtoNodes.getLength(); i++) {
            Element produtoElement = (Element) produtoNodes.item(i);

            int id = Integer.parseInt(produtoElement.getAttribute("id"));
            String nome = produtoElement.getElementsByTagName("nome").item(0).getTextContent();
            int idEmpresa = Integer.parseInt(produtoElement.getElementsByTagName("idEmpresa").item(0).getTextContent());
            Float preco = Float.parseFloat(produtoElement.getElementsByTagName("preco").item(0).getTextContent());
            String categoria = produtoElement.getElementsByTagName("descricao").item(0).getTextContent();

            Produto produto = new Produto(id, nome, preco, categoria, idEmpresa);

            // Adiciona no mapa de produtos
            produtosPorID.put(id, produto);
        }
    }
}


private void carregarPedidosDoXML() {
    Document document = xml.carregarOuCriarXML();
    if (document != null) {
        NodeList pedidoNodes = document.getElementsByTagName("pedido");

        for (int i = 0; i < pedidoNodes.getLength(); i++) {
            Element pedidoElement = (Element) pedidoNodes.item(i);

            int id = Integer.parseInt(pedidoElement.getAttribute("id"));
            int idCliente = Integer.parseInt(pedidoElement.getElementsByTagName("idCliente").item(0).getTextContent());
            int idEmpresa = Integer.parseInt(pedidoElement.getElementsByTagName("idEmpresa").item(0).getTextContent());
            String nomeCliente = pedidoElement.getElementsByTagName("nomeCliente").item(0).getTextContent();
            String nomeEmpresa = pedidoElement.getElementsByTagName("nomeEmpresa").item(0).getTextContent();
            String estadoPedido = pedidoElement.getElementsByTagName("estadoPedido").item(0).getTextContent();
            String produtos = pedidoElement.getElementsByTagName("produtos").item(0).getTextContent();
            float valorPedido = Float.parseFloat(pedidoElement.getElementsByTagName("valorPedido").item(0).getTextContent());

            Pedido pedido = new Pedido(id, nomeCliente, nomeEmpresa, estadoPedido, idCliente, idEmpresa);
            pedido.setProdutos(produtos);
            pedido.setValorPedido(valorPedido);
            // Adiciona no mapa de pedidos
            pedidosPorID.put(id, pedido);
        }
    }
}


private void carregarEntregasDoXML() {
    Document document = xml.carregarOuCriarXML();
    if (document != null) {
        NodeList entregaNodes = document.getElementsByTagName("entrega");

        for (int i = 0; i < entregaNodes.getLength(); i++) {
            Element entregaElement = (Element) entregaNodes.item(i);

            int id = Integer.parseInt(entregaElement.getAttribute("id"));
            String nomeCliente = entregaElement.getElementsByTagName("nomeCliente").item(0).getTextContent();
            String nomeEmpresa = entregaElement.getElementsByTagName("nomeEmpresa").item(0).getTextContent();
            int idPedido = Integer.parseInt(entregaElement.getElementsByTagName("idPedido").item(0).getTextContent());
            int idEntregador = Integer.parseInt(entregaElement.getElementsByTagName("idEntregador").item(0).getTextContent());
            String destino = entregaElement.getElementsByTagName("destino").item(0).getTextContent();
            String produtos = entregaElement.getElementsByTagName("produtos").item(0).getTextContent();

            Entrega entrega = new Entrega(id, nomeCliente, nomeEmpresa, idPedido, idEntregador, destino, produtos);

            // Adiciona no mapa de entregas
            entregasPorID.put(id, entrega);
        }
    }
}


}

