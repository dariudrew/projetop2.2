package br.ufal.ic.p2.jackut.modelo.xml;

import br.ufal.ic.p2.jackut.modelo.entrega.Entrega;
import br.ufal.ic.p2.jackut.modelo.exception.ErroApagarArquivoException;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;
import br.ufal.ic.p2.jackut.modelo.produto.Produto;
import br.ufal.ic.p2.jackut.modelo.usuario.DonoEmpresa;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;

public class XML {

    private final String arquivoNome = "arquivo.xml";


    private Document carregarOuCriarXML() {
        try {
            File arquivo = new File(arquivoNome);

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            if (arquivo.exists()) {
                // Se o arquivo já existir, carrega o documento
                return documentBuilder.parse(arquivo);
            } else {
                Document document = documentBuilder.newDocument();
                Element raiz = document.createElement("myfood");
                document.appendChild(raiz);
                salvarXML(document); // Salva o novo arquivo XML
                return document;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void salvarXML(Document document) {
        try {
            // Criação de um TransformerFactory e Transformer para escrever o XML no arquivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            //Configura o transformador para indentar o XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Indenta com 4 espaços

            //Define a codificação como UTF-8 para o arquivo
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            //Remove as quebras de linha extras
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            //Cria a estrutura do XML no arquivo
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("arquivo.xml"));

            // Aplica a transformação
            transformer.transform(domSource, streamResult);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //cria o XML se não existir
    public void criarXML() {
        carregarOuCriarXML();
    }

    // metodo auxiliar para inserir elementos no XML
    private void inserirElemento(Document document, Element elemento) {
        try {
            Element raiz = document.getDocumentElement();
            raiz.appendChild(elemento); // Adiciona o elemento na raiz do mesmo documento
            salvarXML(document); // Salva as alterações
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inserirUsuario(Usuario usuario) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML(); // Carrega ou cria o documento existente
        Element usuarioElement = document.createElement("usuario");
        usuarioElement.setAttribute("id", String.valueOf(usuario.getId()));

        usuarioElement.appendChild(criarElemento(document, "id", String.valueOf(usuario.getId())));
        usuarioElement.appendChild(criarElemento(document, "nome", usuario.getNome()));
        usuarioElement.appendChild(criarElemento(document, "email", usuario.getEmail()));
        usuarioElement.appendChild(criarElemento(document, "senha", usuario.getSenha()));
        usuarioElement.appendChild(criarElemento(document, "endereco", usuario.getEndereco()));

        if (usuario.getTipoObjeto().equals("donoEmpresa")) {
            usuarioElement.appendChild(criarElemento(document, "cpf", ((DonoEmpresa) usuario).getCpf()));
        }

        inserirElemento(document, usuarioElement); // Agora passa o document correto ao inserir
    }


    // Método para criar um elemento Empresa
    public void inserirEmpresa(Empresa empresa) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML();
        Element empresaElement = document.createElement("empresa");
        empresaElement.setAttribute("id", String.valueOf(empresa.getIdEmpresa()));

        empresaElement.appendChild(criarElemento(document, "idDono", String.valueOf(empresa.getIdDono())));

        empresaElement.appendChild(criarElemento(document, "nome", empresa.getNomeEmpresa()));
        empresaElement.appendChild(criarElemento(document, "endereco", empresa.getEnderecoEmpresa()));
        empresaElement.appendChild(criarElemento(document, "entregadoresVinculados", empresa.getEntregadoresVinculados()));

        if(empresa.getTipoEmpresa().equals("restaurante")){
            empresaElement.appendChild(criarElemento(document, "tipoCozinha", empresa.getTipoCozinha()));
        }
        else if(empresa.getTipoEmpresa().equals("mercado")){
            empresaElement.appendChild(criarElemento(document, "abre", empresa.getAbre()));
            empresaElement.appendChild(criarElemento(document, "fecha", empresa.getFecha()));
            empresaElement.appendChild(criarElemento(document, "tipoMercado", empresa.getTipoMercado()));
        }
        else if(empresa.getTipoEmpresa().equals("farmacia")){
            empresaElement.appendChild(criarElemento(document, "aberto24Horas", String.valueOf(empresa.getAberto24Horas())));
            empresaElement.appendChild(criarElemento(document, "numeroFuncionarios", String.valueOf(empresa.getNumeroFuncionarios())));
        }


        inserirElemento(document, empresaElement);
    }

     // Método para criar um elemento Produto
    public void inserirProduto(Produto produto) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML();
        Element produtoElement = document.createElement("produto");
        produtoElement.setAttribute("id", String.valueOf(produto.getIdProduto()));

        produtoElement.appendChild(criarElemento(document, "nome", produto.getNomeProduto()));
        produtoElement.appendChild(criarElemento(document, "idEmpresa", String.valueOf(produto.getIdEmpresa())));
        produtoElement.appendChild(criarElemento(document, "preco", String.valueOf(produto.getValorProduto())));
        produtoElement.appendChild(criarElemento(document, "descricao", produto.getCategoria()));

        inserirElemento(document, produtoElement);
    }

    // Método para criar um elemento Pedido
    public void inserirPedido(Pedido pedido) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML();
        Element pedidoElement = document.createElement("pedido");
        pedidoElement.setAttribute("id", String.valueOf(pedido.getNumeroPedido()));

        pedidoElement.appendChild(criarElemento(document, "idCliente", String.valueOf(pedido.getIdCliente())));
        pedidoElement.appendChild(criarElemento(document, "idEmpresa", String.valueOf(pedido.getIdEmpresa())));
        pedidoElement.appendChild(criarElemento(document, "nomeCliente", pedido.getNomeCliente()));
        pedidoElement.appendChild(criarElemento(document, "nomeEmpresa", pedido.getNomeEmpresa()));
        pedidoElement.appendChild(criarElemento(document, "estadoPedido", pedido.getEstadoPedido()));
        pedidoElement.appendChild(criarElemento(document, "produtos", pedido.getProdutos()));
        pedidoElement.appendChild(criarElemento(document, "valorPedido", String.valueOf(pedido.getValorPedido())));

        inserirElemento(document, pedidoElement);
    }

    public void inserirEntrega(Entrega entrega) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML();
        Element entregaElement = document.createElement("entrega");
        entregaElement.setAttribute("id", String.valueOf(entrega.getIdEntrega()));

        entregaElement.appendChild(criarElemento(document, "nomeCliente", entrega.getNomeCliente()));
        entregaElement.appendChild(criarElemento(document, "nomeEmpresa", entrega.getNomeEmpresa()));
        entregaElement.appendChild(criarElemento(document, "idPedido", String.valueOf(entrega.getIdPedido())));
        entregaElement.appendChild(criarElemento(document, "idEntregador", String.valueOf(entrega.getIdEntregador())));
        entregaElement.appendChild(criarElemento(document, "destino", entrega.getDestino()));
        entregaElement.appendChild(criarElemento(document, "produtos", entrega.getProdutos()));

        inserirElemento(document, entregaElement);
    }

    // Método auxiliar para criar um elemento com texto
    private Element criarElemento(Document document, String tagName, String textContent) throws ErroApagarArquivoException {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(textContent));
        return element;
    }
    public void apagarXML() {
        try {
            File arquivo = new File("arquivo.xml");

            // Verifica se o arquivo existe
            if (arquivo.exists()) {
                // Tenta deletar o arquivo
                if (!arquivo.delete()) {
                    throw new ErroApagarArquivoException();
                }
            } else {
                System.out.println("Arquivo XML não existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
