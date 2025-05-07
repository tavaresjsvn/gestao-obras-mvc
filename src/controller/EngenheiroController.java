package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Engenheiro;
import model.Especialidade;
import model.Obra;
import persistence.EngenheiroRepository;

public class EngenheiroController {
	
	private EngenheiroRepository repository;
	private ObraController obraController;
	private Map<String, Engenheiro> engenheirosCadastrados = new HashMap<>();
	
	
	public EngenheiroController(EngenheiroRepository repository, ObraController obraController) {
		this.obraController = obraController;
		this.repository = repository;
		carregarTodosDoArquivo();
	}

	public void cadastrarEngenheiro(Engenheiro e) {
		
		if (!e.validarCREA()) {
			throw new IllegalArgumentException("Registro CREA inválido");
		}
		
		if (existeEngenheiro(e)) {
			throw new IllegalArgumentException("Engenheiro já cadastrado");
		}
		
		engenheirosCadastrados.put(e.getRegistroCREA(), e);
		repository.salvar(e);
	}
	
	public void alocarEngenheiro(String crea, Obra o) {
		Engenheiro engenheiro = buscarPorCREA(crea);
	    o.alocarEngenheiro(engenheiro);
	    engenheiro.adicionarObra(o);
	    repository.atualizar(engenheiro);
	    
	}
	
	public void removerEngenheiro(String registroCREA) {
	    if (!engenheirosCadastrados.containsKey(registroCREA)) {
	        throw new IllegalArgumentException("Não foi encontrado nenhum engenheiro cadastrado com o CREA informado");
	    }
	    
	    Engenheiro engenheiro = engenheirosCadastrados.get(registroCREA);

	    for (Obra o : obraController.listarObras().values()) {
	        if (o.getEngenheirosAlocados().containsKey(registroCREA)) {
	            o.getEngenheirosAlocados().remove(registroCREA);
	            engenheiro.removerObra(o.getCodigo());
	            obraController.atualizarObra(o); 
	        }
	    }
	    engenheirosCadastrados.remove(registroCREA);
	    repository.remover(registroCREA);
	}
	
	
	public Engenheiro buscarPorCREA(String crea) {
		if (!engenheirosCadastrados.containsKey(crea)) {
			throw new IllegalArgumentException("Não foi encontrado nenhum engenheiro cadastrado com o CREA informado");
		}
		
		return engenheirosCadastrados.get(crea);
	}
	
	public boolean existeEngenheiro(Engenheiro engenheiro) {
	    return engenheirosCadastrados.containsKey(engenheiro.getRegistroCREA());
	}
	
	public ArrayList<Engenheiro> listarEngenheiros() {
	    return new ArrayList<>(engenheirosCadastrados.values());
	}
	
	public void editarRegistroCREA(String registroCREAAtual, String novoRegistroCREA) {
		
		if (!engenheirosCadastrados.containsKey(registroCREAAtual)) {
            throw new IllegalArgumentException("Engenheiro não encontrado");
        }
		
	    Engenheiro engenheiro = buscarPorCREA(registroCREAAtual);

	    if (!engenheiro.validarCREA(novoRegistroCREA)) {
	        throw new IllegalArgumentException("Novo registro CREA inválido.");
	    }

	    engenheirosCadastrados.remove(registroCREAAtual);
	    engenheiro.setRegistroCREA(novoRegistroCREA);
	    engenheirosCadastrados.put(novoRegistroCREA, engenheiro);
	    repository.atualizar(engenheiro);
	    obraController.atualizarCREAEmTodasObras(registroCREAAtual, novoRegistroCREA);
	}
	
	public void editarEspecialidadeEngenheiro(String crea, Especialidade novaEspecialidade) {
    	
    	if (!engenheirosCadastrados.containsKey(crea)) {
            throw new IllegalArgumentException("Engenheiro não encontrado");
        }
	    Engenheiro engenheiro = buscarPorCREA(crea);
	    engenheiro.setEspecialidade(novaEspecialidade);
	    repository.atualizar(engenheiro);
	}
	
	public void editarNomeEngenheiro(String crea, String novoNome) {
		
		if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }
    	
    	if (!engenheirosCadastrados.containsKey(crea)) {
            throw new IllegalArgumentException("Engenheiro não encontrado");
        }
    	
	    Engenheiro engenheiro = buscarPorCREA(crea);
	    engenheiro.setNome(novoNome);
	    repository.atualizar(engenheiro);
	}
	
	public Map<String, Engenheiro> listarEngenheirosCadastrados() {
        return new HashMap<>(engenheirosCadastrados);
    }
	
	public void limparEngenheiros() {
		engenheirosCadastrados.clear();
    }
	
	public void atualizarEngenheiro(Engenheiro e) {
	    engenheirosCadastrados.put(e.getRegistroCREA(), e);
	    repository.atualizar(e);
	}
	
	public void carregarTodosDoArquivo() {
	    for (Engenheiro e : repository.carregarTodos()) {
	        engenheirosCadastrados.put(e.getRegistroCREA(), e);
	    }
	}

}
