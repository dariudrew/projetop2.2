package br.ufal.ic.p2.jackut.modelo;

import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;
import br.ufal.ic.p2.jackut.modelo.exception.*;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;

import java.util.ArrayList;

public class SistemaEntrega {
    private SistemaDados dados;
    private SistemaUsuario sistemaUsuario;
    private SistemaEmpresa sistemaEmpresa;
    private SistemaProduto sistemaProduto;
    private SistemaPedido sistemaPedido;
    private ArrayList pedidosProntos;

    public SistemaEntrega(SistemaDados dados){
        this.dados = dados;
        this.sistemaUsuario = new SistemaUsuario(dados);
        this.sistemaEmpresa = new SistemaEmpresa(dados);
        this.sistemaProduto = new SistemaProduto(dados);
        this.sistemaPedido = new SistemaPedido(dados);
    }
    public int criarEntrega(int pedido, int idEntregador, String destino) throws PedidoNaoEncontradoException, UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, EnderecoInvalidoException, PedidoNaoProntoException, NaoEntregadorValidoException, EntregadorEmEntregaException {

        if(!dados.usuariosPorID.containsKey(idEntregador)){
            throw new UsuarioNaoCadastradoException();
        }
        if(!dados.pedidosPorID.get(pedido).getEstadoPedido().matches("pronto")){
            throw new PedidoNaoProntoException();
        }
        if(!dados.usuariosPorID.get(idEntregador).getTipoObjeto().matches("entregador")){
            throw new UsuarioNaoEntregadorException();
        }




        //entregador ta livre?
        Pedido p;
        for(int i = 1; i<= dados.entregasPorID.size(); i++){
            Entrega entrega = dados.entregasPorID.get(i);
            p = dados.pedidosPorID.get(entrega.getIdPedido());

            if(p.getEstadoPedido().equals("entregando") && entrega.getIdEntregador() == idEntregador) {
                throw new EntregadorEmEntregaException();
            }
        }

        p = dados.pedidosPorID.get(pedido);

        String nomeCliente = p.getNomeCliente();
        String nomeEmpresa = p.getNomeEmpresa();
        String produtos = p.getProdutos();
        p.setEstadoPedido("entregando");

        if(sistemaUsuario.validaNome(destino)){
            destino = sistemaUsuario.getAtributoUsuario(p.getIdCliente(), "endereco");
        }

        Entrega entrega = new Entrega(dados.contadorIdEntrega,nomeCliente, nomeEmpresa, pedido,idEntregador, destino, produtos);
        dados.entregasPorID.put(dados.contadorIdEntrega, entrega);
        dados.contadorIdEntrega++;
        return entrega.getIdEntrega(); // id do produto a ser entregue
    }

    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, PedidoLiberadoException, NaoPossivelLiberarPedidoException {
        if(dados.pedidosPorID.isEmpty() || !dados.pedidosPorID.containsKey(numero)){
            throw new PedidoNaoEncontradoException();
        }
        Pedido pedido = dados.pedidosPorID.get(numero);
        if(pedido.getEstadoPedido().matches("pronto")){
            throw new PedidoLiberadoException();
        }
        else if(pedido.getEstadoPedido().matches("aberto")){
            throw new NaoPossivelLiberarPedidoException();
        }

            pedido.setEstadoPedido("pronto");
    }

    public int obterPedido(int idEntregador) throws UsuarioNaoCadastradoException, UsuarioNaoEntregadorException, EmpresaNaoCadastradaException, EntregadorSemEmpresaException, AtributoInvalidoException, AtributoNaoExisteException, PedidoNaoEncontradoException, NaoEntregadorValidoException, NaoExistePedidoEntregaException {

        if(dados.usuariosPorID.isEmpty() || !dados.usuariosPorID.containsKey(idEntregador)){
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = dados.usuariosPorID.get(idEntregador);
        if(!usuario.getTipoObjeto().matches("entregador")){
            throw new UsuarioNaoEntregadorException();
        }

        String empresassDoEntregador = sistemaUsuario.getEmpresas(idEntregador);
        if(empresassDoEntregador.replaceAll("[\\[\\]{}]", "").isEmpty()){
            throw new EntregadorSemEmpresaException();
        }

        int proximoPedido = 0;
        Empresa empresa;

        int tamPedidos = dados.pedidosPorID.size();
        for(int j = 1; j <= tamPedidos; j++){
           Pedido p = dados.pedidosPorID.get(j);
           empresa = dados.empresasPorID.get(p.getIdEmpresa());
           if(p.getEstadoPedido().matches("pronto")){

               String emailEntregadores = empresa.getEntregadoresVinculados();
               emailEntregadores = emailEntregadores.replaceAll("[\\[\\]{}]", "");
               String[] emailsArray = emailEntregadores.split(", ");

               boolean vinculo = false;

               for (int i = 0; i < emailsArray.length; i++) {

                   if(emailsArray[i].equals(usuario.getEmail())){
                       vinculo = true;
                   }
               }
               if(vinculo){
                   if(empresa.getTipoEmpresa().matches("farmacia")){
                       proximoPedido = p.getNumeroPedido();
                       break;
                   }
                   else if(proximoPedido == 0){
                       proximoPedido = p.getNumeroPedido();
                   }
               }
           }
           if(proximoPedido == 0 && j == tamPedidos)
           {
               throw new NaoExistePedidoEntregaException();
           }
        }
        return proximoPedido;
    }

    public String getEntrega(int idEntrega, String atributo) throws AtributoInvalidoException, EntregaNaoEncontradaException, AtributoNaoExisteException {
        if(sistemaUsuario.validaNome(atributo)){
            throw new AtributoInvalidoException();
        }
        if(!dados.entregasPorID.isEmpty() && !dados.entregasPorID.containsKey(idEntrega)){
            throw new EntregaNaoEncontradaException();
        }
        Entrega entrega = dados.entregasPorID.get(idEntrega);

        switch (atributo){
            case "destino":
                return entrega.getDestino();
            case "empresa":
                return entrega.getNomeEmpresa();
            case "produtos":
                return entrega.getProdutos();
            case "cliente":
               return entrega.getNomeCliente();
            case "pedido":
                return String.valueOf(entrega.getIdPedido());
            case "entregador":
                Usuario usuario = dados.usuariosPorID.get(entrega.getIdEntregador());
                return usuario.getNome();
            default:
                throw new AtributoNaoExisteException();
        }
    }
    public int getIdEntrega(int idPedido) throws NaoExisteEntregaIdException, NaoExisteNadaEntregaException, AtributoInvalidoException, AtributoNaoExisteException, PedidoNaoEncontradoException {
       if(dados.pedidosPorID == null || !dados.pedidosPorID.containsKey(idPedido)){
            throw new NaoExisteNadaEntregaException();
        }

        int idEntrega = 0;
        for(Entrega entrega : dados.entregasPorID.values()){

            if(entrega.getIdPedido() == idPedido){
                idEntrega = entrega.getIdEntrega();
            }
        }

        if(idEntrega == 0){
            throw new NaoExisteNadaEntregaException();
        }

        return idEntrega;
    }


    public void entregar(int idEntrega) throws NaoExisteEntregaIdException, NaoExisteNadaEntregaException {
        if (!dados.entregasPorID.containsKey(idEntrega)) {
            throw new NaoExisteNadaEntregaException();
        }
        Entrega entrega = dados.entregasPorID.get(idEntrega);
        Pedido pedido = dados.pedidosPorID.get(entrega.getIdPedido());
        pedido.setEstadoPedido("entregue");

        // entregador existe mas n tem entrega para entregar
    }

}
