package br.ufal.ic.p2.jackut.modelo.sistemaControle;

import br.ufal.ic.p2.jackut.modelo.empresa.Empresa;
import br.ufal.ic.p2.jackut.modelo.empresa.Farmacia;
import br.ufal.ic.p2.jackut.modelo.empresa.Mercado;
import br.ufal.ic.p2.jackut.modelo.empresa.Restaurante;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.*;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.EmpresaNomeEnderecoEmUsoException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.EmpresaNomeExisteException;
import br.ufal.ic.p2.jackut.modelo.exception.atributo.NomeInvalidoException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoCadastradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.EmpresaNaoEncontradaException;
import br.ufal.ic.p2.jackut.modelo.exception.busca.NaoExisteEmpresaException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.NaoMercadoValidoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.NumeroFuncionariosException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.modelo.exception.cadastro.UsuarioNaoCriaEmpresaException;
import br.ufal.ic.p2.jackut.modelo.exception.verificacao.ErroApagarArquivoException;
import br.ufal.ic.p2.jackut.modelo.usuario.Usuario;

import java.util.ArrayList;


public class SistemaEmpresa {
    private SistemaDados dados;



    public SistemaEmpresa(SistemaDados dados){
        this.dados = dados;
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nomeEmpresa, String endereco, String tipoCozinha) throws
            UsuarioNaoCadastradoException, EmpresaEnderecoInvalidoException, EmpresaTipoCozinhaInvalidoException, UsuarioNaoCriaEmpresaException,
            EmpresaNomeExisteException, EmpresaNomeEnderecoEmUsoException, TipoEmpresaInvalidoException, NomeInvalidoException, ErroApagarArquivoException {
        dados.validaDadosRestaurante(tipoEmpresa, dono, nomeEmpresa, endereco, tipoCozinha);
        Restaurante restaurante = new Restaurante(dados.contadorIdEmpresa, dono, nomeEmpresa, endereco, tipoCozinha);

        if (tipoEmpresa.matches("restaurante")){
            dados.empresasPorID.put(dados.contadorIdEmpresa, restaurante);
            dados.contadorIdEmpresa++;
            dados.xml.inserirEmpresa(restaurante);
        }
        else{
            throw new TipoEmpresaInvalidoException();
        }
        return restaurante.getIdEmpresa();
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nomeMercado, String endereco, String abre, String fecha, String tipoMercado)
            throws UsuarioNaoCriaEmpresaException, EmpresaEnderecoInvalidoException, UsuarioNaoCadastradoException, EmpresaNomeEnderecoEmUsoException, EmpresaNomeExisteException, FormatoHoraInvalidoException, HorarioInvalidoException, TipoEmpresaInvalidoException, NomeInvalidoException, TipoMercadoInvalidoException, ErroApagarArquivoException {

        dados.validaDadosMercado(tipoEmpresa, dono, nomeMercado, endereco, abre, fecha,tipoMercado);
        Mercado mercado = new Mercado(dados.contadorIdEmpresa, dono, nomeMercado, endereco,abre, fecha, tipoMercado);
        if(tipoEmpresa.matches("mercado")){
            dados.empresasPorID.put(dados.contadorIdEmpresa, mercado);
            dados.contadorIdEmpresa++;
            dados.xml.inserirEmpresa(mercado);
        }
        else{
            throw new TipoEmpresaInvalidoException();
        }

        return mercado.getIdEmpresa();
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nomeFarmacia, String endereco,  boolean aberto24Horarios, int numeroFuncionarios)
            throws UsuarioNaoCriaEmpresaException, EmpresaEnderecoInvalidoException, NomeInvalidoException,
            UsuarioNaoCadastradoException, TipoEmpresaInvalidoException, EmpresaNomeEnderecoEmUsoException,
            NumeroFuncionariosException, EmpresaNomeExisteException, ErroApagarArquivoException {

        dados.validaDadosFarmacia(tipoEmpresa, dono, nomeFarmacia, endereco, numeroFuncionarios);
        Farmacia farmacia = new Farmacia(dados.contadorIdEmpresa, dono, nomeFarmacia, endereco, aberto24Horarios, numeroFuncionarios);
        if(tipoEmpresa.matches("farmacia")){
            dados.empresasPorID.put(dados.contadorIdEmpresa, farmacia);
            dados.contadorIdEmpresa++;
            dados.xml.inserirEmpresa(farmacia);
        }
        else{
            throw new TipoEmpresaInvalidoException();
        }
        return farmacia.getIdEmpresa();
    }


    public String getEmpresasDoUsuario(int idDono) throws UsuarioNaoCriaEmpresaException {
        if(dados.usuariosPorID.get(idDono).getTipoObjeto().matches("cliente"))
        {
            throw new UsuarioNaoCriaEmpresaException();
        }
        String empresasPorDono = "";
        if(!dados.empresasPorID.isEmpty()){
            int qntEmpresas = dados.empresasPorID.size();

            for(int i = 1; i <= qntEmpresas; i++){

                Empresa empresa = dados.empresasPorID.get(i);

                if(i == 1){
                    empresasPorDono = empresasPorDono.concat("{[");
                }
                if(empresa.getIdDono() == idDono ){


                    if(empresasPorDono.matches("^\\{\\[\\[.*")){
                        empresasPorDono = empresasPorDono.concat(", ");
                    }
                    empresasPorDono = empresasPorDono.concat("["+empresa.getNomeEmpresa()+", "+empresa.getEnderecoEmpresa()+"]");
                }
                if(i == qntEmpresas){
                    empresasPorDono = empresasPorDono.concat("]}");
                }
            }
        }
        else {
            empresasPorDono = empresasPorDono.concat("{[]}");
        }
        return empresasPorDono;
    }
    public int getIdEmpresa(int idDono, String nome, int indice)
            throws UsuarioNaoCadastradoException, NomeInvalidoException, UsuarioNaoCriaEmpresaException,
            IndiceInvalidoException, NaoExisteEmpresaException, IndiceMaiorException {

        int idEmpresa = 0;
        if(!dados.usuariosPorID.containsKey(idDono)){
            throw new UsuarioNaoCadastradoException();
        }
        if(dados.validaNome(nome)){
            throw new NomeInvalidoException();
        }


        ArrayList<String> empresasProcurada = new ArrayList<>();
        ArrayList<String> empresasProcuradaEndereco = new ArrayList<>();


        String empresasPorDonoSemColchetes = getEmpresasDoUsuario(idDono).replaceAll("[\\[\\]{}]", "");
        String[] empresasPorDono = empresasPorDonoSemColchetes.split(", ");

        for (int i = 0; i < empresasPorDono.length; i+=2) {
            if(empresasPorDono[i].matches(nome)){
                empresasProcurada.add(empresasPorDono[i]);
                empresasProcuradaEndereco.add(empresasPorDono[i+1]);
            }
        }
        if(indice < 0){
            throw new IndiceInvalidoException();
        }
        if(empresasProcurada.isEmpty()){
            throw new NaoExisteEmpresaException();
        }

        if(indice >= empresasProcurada.size()){
            throw new IndiceMaiorException();
        }
        if(!dados.empresasPorID.isEmpty()){
            String nomeEmpresa;
            String enderecoEmpresa;
            for(Empresa empresa: dados.empresasPorID.values()){
                nomeEmpresa = empresa.getNomeEmpresa();
                enderecoEmpresa = empresa.getEnderecoEmpresa();
                if(nomeEmpresa.matches(empresasProcurada.get(indice)) && enderecoEmpresa.matches(empresasProcuradaEndereco.get(indice))){
                    idEmpresa =  empresa.getIdEmpresa();
                }
            }
        }

        return idEmpresa;
    }
    public String getAtributoEmpresa(int idEmpresa, String atributo) throws EmpresaNaoCadastradaException, AtributoInvalidoException {
        if(dados.empresasPorID.containsKey(idEmpresa)) {
            if(dados.validaNome(atributo)) {
                throw new AtributoInvalidoException();
            }

            Empresa empresa = dados.empresasPorID.get(idEmpresa);
            return switch (atributo) {
                case "nome" -> empresa.getNomeEmpresa();
                case "endereco" -> empresa.getEnderecoEmpresa();
                case "tipoCozinha" -> empresa.getTipoCozinha();
                case "dono" -> {
                    Usuario usuario = dados.usuariosPorID.get(empresa.getIdDono());
                    yield usuario.getNome();
                }
                case "abre" -> empresa.getAbre();
                case "fecha" -> empresa.getFecha();
                case "tipoMercado" -> empresa.getTipoMercado();
                case "aberto24Horas" -> empresa.getAberto24Horas() + "";
                case "numeroFuncionarios" -> empresa.getNumeroFuncionarios() + "";

                default -> throw new AtributoInvalidoException();
            };
        }
        else{
            throw new EmpresaNaoCadastradaException();
        }

    }
    public void alterarFuncionamento(int idEmpresa, String abre, String fecha) throws EmpresaNaoEncontradaException, FormatoHoraInvalidoException, HorarioInvalidoException, NaoMercadoValidoException {
        if(!dados.empresasPorID.containsKey(idEmpresa)){
            throw new EmpresaNaoEncontradaException();
        }
        dados.horarioFormato(abre, fecha);
        dados.horarioNull(abre, fecha);
        dados.horarioPadrao(abre, fecha);

        Empresa empresa = dados.empresasPorID.get(idEmpresa);
        if(!empresa.getTipoEmpresa().matches("mercado")){
            throw new NaoMercadoValidoException();
        }
        empresa.setAbre(abre);
        empresa.setFecha(fecha);

    }
}
