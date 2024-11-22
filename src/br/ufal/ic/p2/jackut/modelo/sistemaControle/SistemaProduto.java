package br.ufal.ic.p2.jackut.modelo.sistemaControle;

import br.ufal.ic.p2.jackut.modelo.exception.atributo.AtributoNaoExisteException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.ProdutoCategoriaInvalidaException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.ProdutoValorInvalidoExcepion;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoCadastradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoEncontradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.ProdutoNaoEncontradoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.ProdutoJaExisteNaEmpresaException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.ProdutoNaoCadastradoException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.ErroApagarArquivoException;
import br.ufal.ic.p2.jackut.modelo.produto.Produto;

import java.util.Locale;

public class SistemaProduto {
    private final SistemaDados dados;
    private final SistemaUsuario sistemaUsuario;

    public SistemaProduto(SistemaDados dados){
        this.dados = dados;
        this.sistemaUsuario = new SistemaUsuario(dados);
    }
    public int criarProduto(int idEmpresa, String nomeProduto, float valorProduto, String categoriaProduto)
            throws EmpresaNaoCadastradaException, NomeInvalidoException,
            ProdutoValorInvalidoExcepion, ProdutoCategoriaInvalidaException, ProdutoJaExisteNaEmpresaException, ErroApagarArquivoException {

        dados.validaDadosProduto(idEmpresa, nomeProduto, valorProduto, categoriaProduto);
        Produto produto = new Produto(dados.contadorIdProduto, nomeProduto, valorProduto, categoriaProduto,idEmpresa);
        dados.produtosPorID.put(dados.contadorIdProduto, produto);
        dados.contadorIdProduto++;
        dados.xml.inserirProduto(produto);
        return produto.getIdProduto();
    }

    public void editarProduto(int idProduto, String nomeProduto, float valorProduto, String categoriaProduto)
            throws NomeInvalidoException, ProdutoValorInvalidoExcepion, ProdutoCategoriaInvalidaException, ProdutoNaoCadastradoException, ErroApagarArquivoException {

        if(dados.validaNome(nomeProduto)){
            throw new NomeInvalidoException();
        }
        else if(valorProduto < 0){
            throw new ProdutoValorInvalidoExcepion();
        }
        else if(dados.validaNome(categoriaProduto)){
            throw new ProdutoCategoriaInvalidaException();
        }
        else if(!dados.produtosPorID.containsKey(idProduto)){
            throw new ProdutoNaoCadastradoException();
        }

        Produto produto = dados.produtosPorID.get(idProduto);

        produto.setNomeProduto(nomeProduto);
        produto.setValorProduto(valorProduto);
        produto.setCategoriaProduto(categoriaProduto);
        dados.xml.atualizarProduto(produto);

    }
    public String getProduto(String  nomeProduto, int idEmpresa, String atributo)
            throws EmpresaNaoCadastradaException, NomeInvalidoException, AtributoNaoExisteException, ProdutoNaoEncontradoException {
        if(dados.validaNome(nomeProduto)){
            throw new NomeInvalidoException();
        }
        else if(!dados.empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoCadastradaException();
        }
        Produto produto = null;
        for(int i = 1; i <= dados.produtosPorID.size(); i++){
            Produto p = dados.produtosPorID.get(i);
            if(p.getNomeProduto().matches(nomeProduto) && p.getIdEmpresa() == idEmpresa){
                produto = p;
                break;
            }
            if(i == dados.produtosPorID.size()){
                throw new ProdutoNaoEncontradoException();
            }
        }
        String str;

        switch (atributo) {
            case "valor" -> {
                assert produto != null;
                float valor = produto.getValorProduto();
                str = String.format(Locale.US, "%.2f", valor);
            }
            case "categoria" -> {
                assert produto != null;
                str = produto.getCategoria();
            }
            case "empresa" -> str = dados.empresasPorID.get(idEmpresa).getNomeEmpresa();
            default -> str = "invalido";
        }

        if(str.matches("invalido")){
            throw new AtributoNaoExisteException();
        }
        return str;
    }

    public String listarProdutos(int idEmpresa) throws EmpresaNaoEncontradaException {
        if(!dados.empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoEncontradaException();
        }

        String produtosPorEmpresa = "";

        if(!dados.produtosPorID.isEmpty()){
            int qntProdutos = dados.produtosPorID.size();
            for(int i = 1; i <= qntProdutos; i++){

                Produto produto = dados.produtosPorID.get(i);

                if(i == 1){
                    produtosPorEmpresa = produtosPorEmpresa.concat("{[");
                }
                if(produto.getIdEmpresa() == idEmpresa ){

                    if(produtosPorEmpresa.matches(".*[a-zA-Z0-9]$")){       //veifica se a string terminar com letras, para saber quando add virgula e espa�amento entre as produtos.
                        produtosPorEmpresa = produtosPorEmpresa.concat(", ");
                    }
                    produtosPorEmpresa = produtosPorEmpresa.concat(produto.getNomeProduto());
                }
                if(i == qntProdutos){
                    produtosPorEmpresa = produtosPorEmpresa.concat("]}");
                }
            }
        }
        else{
            produtosPorEmpresa = produtosPorEmpresa.concat("{[]}");
        }


        return produtosPorEmpresa;
    }

}
