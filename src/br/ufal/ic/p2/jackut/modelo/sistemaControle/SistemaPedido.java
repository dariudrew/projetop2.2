package br.ufal.ic.p2.jackut.modelo.sistemaControle;

import br.ufal.ic.p2.jackut.modelo.produto.Produto;
import br.ufal.ic.p2.jackut.modelo.exception.*;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;

import java.util.Locale;

public class SistemaPedido {
    private final SistemaDados dados;
    private final SistemaUsuario sistemaUsuario;
    private final SistemaEmpresa sistemaEmpresa;
    private final SistemaProduto sistemaProduto;

    public SistemaPedido(SistemaDados dados){
        this.dados = dados;
        this.sistemaUsuario = new SistemaUsuario(dados);
        this.sistemaEmpresa = new SistemaEmpresa(dados);
        this.sistemaProduto = new SistemaProduto(dados);
    }


    public int criarPedido(int idCliente, int idEmpresa)
            throws EmpresaNaoCadastradaException, DonoNaoFazPedidoException, UsuarioNaoCadastradoException,
            AtributoInvalidoException, NaoPermitidoPedidosAbertoMesmaEmpresaException, ErroApagarArquivoException {

        validaDadosPedido(idCliente, idEmpresa);
        String nomeCliente = sistemaUsuario.getAtributoUsuario(idCliente, "nome");
        String nomeEmpresa = sistemaEmpresa.getAtributoEmpresa(idEmpresa, "nome");
        Pedido pedido = new Pedido(dados.contadorIdPedido, nomeCliente, nomeEmpresa, "aberto", idCliente, idEmpresa);
        dados.pedidosPorID.put(dados.contadorIdPedido, pedido);
        dados.contadorIdPedido++;
        dados.xml.inserirPedido(pedido);
        return pedido.getNumeroPedido(); //numero do pedido
    }

    public void validaDadosPedido(int idCliente, int idEmpresa)
            throws UsuarioNaoCadastradoException, EmpresaNaoCadastradaException, DonoNaoFazPedidoException,
            NaoPermitidoPedidosAbertoMesmaEmpresaException {

        if(!dados.usuariosPorID.containsKey(idCliente)){
            throw new UsuarioNaoCadastradoException();
        }
        if(!dados.empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoCadastradaException();
        }
        if(!dados.usuariosPorID.get(idCliente).getTipoObjeto().matches("cliente")){
            throw new DonoNaoFazPedidoException();
        }

        for(Pedido pedido: dados.pedidosPorID.values()){//

            if(pedido.getIdCliente() == idCliente && pedido.getIdEmpresa() == idEmpresa){

                if(pedido.getEstadoPedido().equals("aberto")){
                    throw new NaoPermitidoPedidosAbertoMesmaEmpresaException();
                }

            }
        }
    }

    public int getNumeroPedido(int idCliente, int idEmpresa, int indice) throws PedidoNaoEncontradoException {


        int qntPedidos = dados.pedidosPorID.size();
        int numeroPedido = 0;
        int indiceProcurado = -1;  //iniciando a contagem padrao em 1 ao ives de 0 para reutilizar a variavel "indice" como contador e saber qual o pedido correto

        for(int i=1; i<= qntPedidos; i++){
            Pedido pedido = dados.pedidosPorID.get(i);
            if(pedido.getIdCliente() == idCliente && pedido.getIdEmpresa() == idEmpresa){
                indiceProcurado++;

                if(indiceProcurado == indice){
                    numeroPedido = pedido.getNumeroPedido();
                    break;
                }
            }
        }
        if(indiceProcurado < indice || indiceProcurado > indice)
        {
            throw new PedidoNaoEncontradoException();
        }

        return numeroPedido;
    }

    public void adicionarProduto(int numeroPedido, int idProduto)
            throws NaoExistePedidoAbertoException, ProdutoNaoEncontradoException, ProdutoNaoPerteceEmpresaException, PedidoFechadoException{

        if(!dados.pedidosPorID.containsKey(numeroPedido)){
            throw new NaoExistePedidoAbertoException();
        }
        else if(!dados.produtosPorID.containsKey(idProduto)){
            throw new ProdutoNaoEncontradoException();
        }

        Pedido pedido = dados.pedidosPorID.get(numeroPedido);
        Produto produto = dados.produtosPorID.get(idProduto);
        if(!(pedido.getEstadoPedido().matches("aberto"))){
            throw new PedidoFechadoException();
        }
        if(produto.getIdEmpresa() == pedido.getIdEmpresa()){
            String str = pedido.getProdutos();
            str = str.replaceAll("]}", "");

            if(str.matches(".*[a-zA-Z0-9]$")){ // se h� produtos
                str = str.concat(", "+produto.getNomeProduto()+"]}");
            }
            else{
                str = str.concat(produto.getNomeProduto()+"]}");
            }

            pedido.setProdutos(str);
            pedido.setValorPedido(produto.getValorProduto());
        }
        else{
            throw new ProdutoNaoPerteceEmpresaException();
        }

    }

    public String getPedidos(int numeroPedido, String atributo)
            throws PedidoNaoEncontradoException, AtributoInvalidoException, AtributoNaoExisteException {

        if(!dados.pedidosPorID.containsKey(numeroPedido)){
            throw new PedidoNaoEncontradoException();
        }
        else if(sistemaUsuario.validaNome(atributo)){
            throw new AtributoInvalidoException();
        }
        Pedido pedido = dados.pedidosPorID.get(numeroPedido);
        return switch (atributo) {
            case "cliente" -> pedido.getNomeCliente();
            case "empresa" -> pedido.getNomeEmpresa();
            case "estado" -> pedido.getEstadoPedido();
            case "produtos" -> pedido.getProdutos();
            case "valor" -> String.format(Locale.US, "%.2f", pedido.getValorPedido());
            default -> throw new AtributoNaoExisteException();
        };
    }

    public void fecharPedido(int numeroPedido) throws PedidoNaoEncontradoException {
        if(!dados.pedidosPorID.containsKey(numeroPedido)){
            throw new PedidoNaoEncontradoException();
        }
        Pedido pedido = dados.pedidosPorID.get(numeroPedido);
        pedido.setEstadoPedido("preparando");
    }

    public void removerProduto(int numeroPedido, String produto)
            throws ProdutoInvalidoException, PedidoNaoEncontradoException, ProdutoNaoEncontradoException,
            NaoPossivelRemoverProdutoException, EmpresaNaoCadastradaException, AtributoNaoExisteException,
            NomeInvalidoException {

        if (!dados.pedidosPorID.containsKey(numeroPedido)) {
            throw new PedidoNaoEncontradoException();
        } else if (sistemaUsuario.validaNome(produto)) {
            throw new ProdutoInvalidoException();
        }


        Pedido pedido = dados.pedidosPorID.get(numeroPedido);
        String produtosDoPedido = getStringProdutos(produto, pedido);

        pedido.setProdutos(produtosDoPedido);
        String valorProduto = sistemaProduto.getProduto(produto, pedido.getIdEmpresa(), "valor");
        float valorP = Float.parseFloat(valorProduto);
        pedido.setValorPedido(-valorP);


    }

    private static String getStringProdutos(String produto, Pedido pedido) throws NaoPossivelRemoverProdutoException, ProdutoNaoEncontradoException {
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
