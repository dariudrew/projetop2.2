package br.ufal.ic.p2.jackut.modelo.xml;

import br.ufal.ic.p2.jackut.modelo.entrega.Entrega;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.ErroApagarArquivoException;
import br.ufal.ic.p2.jackut.modelo.pedido.Pedido;
import br.ufal.ic.p2.jackut.modelo.produto.Produto;
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
import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;

public class XML {

    private final String arquivoNome = "arquivo.xml";


    public Document carregarOuCriarXML() {
        try {
            File arquivo = new File(arquivoNome);

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            if (arquivo.exists()) {
                // Se o arquivo j� existir, carrega o documento
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

    public void salvarXML(Document document) {
        try {
            // Cria��o de um TransformerFactory e Transformer para escrever o XML no arquivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            //Configura o transformador para indentar o XML
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Indenta com 4 espa�os

            //Define a codifica��o como UTF-8 para o arquivo
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            //Remove as quebras de linha extras
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            //Cria a estrutura do XML no arquivo
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("arquivo.xml"));

            // Aplica a transforma��o
            transformer.transform(domSource, streamResult);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //cria o XML se n�o existir
    public void criarXML() {
        carregarOuCriarXML();
    }

    // metodo auxiliar para inserir elementos no XML
    public void inserirElemento(Document document, Element elemento) {
        try {
            Element raiz = document.getDocumentElement();
            raiz.appendChild(elemento); // Adiciona o elemento na raiz do mesmo documento
            salvarXML(document); // Salva as altera��es
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inserirUsuario(Usuario usuario) throws ErroApagarArquivoException {
        Document document = carregarOuCriarXML(); 
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
        else if(usuario.getTipoObjeto().equals("entregador")){
            usuarioElement.appendChild(criarElemento(document, "veiculo", ((Entregador) usuario).getVeiculo()));
            usuarioElement.appendChild(criarElemento(document, "placa", ((Entregador) usuario).getPlaca()));
        }

        inserirElemento(document, usuarioElement); // Agora passa o document correto ao inserir
    }


    // M�todo para criar um elemento Empresa
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

     // M�todo para criar um elemento Produto
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

    // M�todo para criar um elemento Pedido
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

    // M�todo auxiliar para criar um elemento com texto
    public Element criarElemento(Document document, String tagName, String textContent) throws ErroApagarArquivoException {
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
                System.out.println("Arquivo XML n�o existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void atualizarProduto(Produto produto) throws ErroApagarArquivoException {
        try {
            // Carrega o documento XML
            Document document = carregarOuCriarXML();
    
            // Obtém a lista de elementos "produto"
            NodeList listaProdutos = document.getElementsByTagName("produto");
    
            // Percorre a lista de produtos para encontrar o produto com o ID correspondente
            for (int i = 0; i < listaProdutos.getLength(); i++) {
                Element elementoProduto = (Element) listaProdutos.item(i);
    
                // Verifica se o atributo "id" do produto corresponde ao ID do produto que será atualizado
                if (elementoProduto.getAttribute("id").equals(String.valueOf(produto.getIdProduto()))) {
                    // Atualiza os elementos do produto
                    elementoProduto.getElementsByTagName("nome").item(0).setTextContent(produto.getNomeProduto());
                    elementoProduto.getElementsByTagName("idEmpresa").item(0).setTextContent(String.valueOf(produto.getIdEmpresa()));
                    elementoProduto.getElementsByTagName("preco").item(0).setTextContent(String.valueOf(produto.getValorProduto()));
                    elementoProduto.getElementsByTagName("descricao").item(0).setTextContent(produto.getCategoria());
    
                    // Salva as alterações no XML
                    salvarXML(document);
                    return; // Termina o método após atualizar o produto
                }
            }
    
            // Caso o produto não seja encontrado
            throw new ErroApagarArquivoException("Produto com ID " + produto.getIdProduto() + " não encontrado no XML.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErroApagarArquivoException("Erro ao atualizar produto no XML: " + e.getMessage());
        }
    }
    public void editarPedido(Pedido pedido) throws ErroApagarArquivoException {
        try {
            // Carrega o documento XML
            Document document = carregarOuCriarXML();
    
            // Obtém a lista de elementos "pedido"
            NodeList listaPedidos = document.getElementsByTagName("pedido");
    
            // Percorre a lista de pedidos para encontrar o pedido com o ID correspondente
            for (int i = 0; i < listaPedidos.getLength(); i++) {
                Element elementoPedido = (Element) listaPedidos.item(i);
    
                // Verifica se o atributo "id" do pedido corresponde ao ID do pedido que será atualizado
                if (elementoPedido.getAttribute("id").equals(String.valueOf(pedido.getNumeroPedido()))) {
                    // Atualiza os elementos do pedido
                    elementoPedido.getElementsByTagName("idCliente").item(0).setTextContent(String.valueOf(pedido.getIdCliente()));
                    elementoPedido.getElementsByTagName("idEmpresa").item(0).setTextContent(String.valueOf(pedido.getIdEmpresa()));
                    elementoPedido.getElementsByTagName("nomeCliente").item(0).setTextContent(pedido.getNomeCliente());
                    elementoPedido.getElementsByTagName("nomeEmpresa").item(0).setTextContent(pedido.getNomeEmpresa());
                    elementoPedido.getElementsByTagName("estadoPedido").item(0).setTextContent(pedido.getEstadoPedido());
                    elementoPedido.getElementsByTagName("produtos").item(0).setTextContent(pedido.getProdutos());
                    elementoPedido.getElementsByTagName("valorPedido").item(0).setTextContent(String.valueOf(pedido.getValorPedido()));
    
                    // Salva as alterações no XML
                    salvarXML(document);
                    return; // Termina o método após atualizar o pedido
                }
            }
    
            // Caso o pedido não seja encontrado
            throw new ErroApagarArquivoException("Pedido com ID " + pedido.getNumeroPedido() + " não encontrado no XML.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErroApagarArquivoException("Erro ao atualizar pedido no XML: " + e.getMessage());
        }
    }
    
    public void editarEmpresa(Empresa empresa) throws ErroApagarArquivoException {
        try {
            // Carrega o documento XML
            Document document = carregarOuCriarXML();
    
            // Obtém a lista de elementos "empresa"
            NodeList listaEmpresas = document.getElementsByTagName("empresa");
    
            // Percorre a lista para localizar a empresa pelo ID
            for (int i = 0; i < listaEmpresas.getLength(); i++) {
                Element elementoEmpresa = (Element) listaEmpresas.item(i);
    
                // Verifica se o atributo "id" do elemento corresponde ao ID da empresa
                if (elementoEmpresa.getAttribute("id").equals(String.valueOf(empresa.getIdEmpresa()))) {
                    // Atualiza os elementos da empresa
                    elementoEmpresa.getElementsByTagName("idDono").item(0).setTextContent(String.valueOf(empresa.getIdDono()));
                    elementoEmpresa.getElementsByTagName("nome").item(0).setTextContent(empresa.getNomeEmpresa());
                    elementoEmpresa.getElementsByTagName("endereco").item(0).setTextContent(empresa.getEnderecoEmpresa());
                    elementoEmpresa.getElementsByTagName("entregadoresVinculados").item(0).setTextContent(empresa.getEntregadoresVinculados());
    
                    // Atualiza elementos específicos com base no tipo de empresa
                    if (empresa.getTipoEmpresa().equals("restaurante")) {
                        elementoEmpresa.getElementsByTagName("tipoCozinha").item(0).setTextContent(empresa.getTipoCozinha());
                    } else if (empresa.getTipoEmpresa().equals("mercado")) {
                        elementoEmpresa.getElementsByTagName("abre").item(0).setTextContent(empresa.getAbre());
                        elementoEmpresa.getElementsByTagName("fecha").item(0).setTextContent(empresa.getFecha());
                        elementoEmpresa.getElementsByTagName("tipoMercado").item(0).setTextContent(empresa.getTipoMercado());
                    } else if (empresa.getTipoEmpresa().equals("farmacia")) {
                        elementoEmpresa.getElementsByTagName("aberto24Horas").item(0).setTextContent(String.valueOf(empresa.getAberto24Horas()));
                        elementoEmpresa.getElementsByTagName("numeroFuncionarios").item(0).setTextContent(String.valueOf(empresa.getNumeroFuncionarios()));
                    }
    
                    // Salva as alterações no XML
                    salvarXML(document);
                    return; // Termina o método após atualizar a empresa
                }
            }
    
            // Caso a empresa não seja encontrada
            throw new ErroApagarArquivoException("Empresa com ID " + empresa.getIdEmpresa() + " não encontrada no XML.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErroApagarArquivoException("Erro ao atualizar empresa no XML: " + e.getMessage());
        }
    }
    
    

}
