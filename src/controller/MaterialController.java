package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Material;
import model.UnidadeMedida;
import persistence.MaterialRepository;

public class MaterialController {
	
   private MaterialRepository repository;
   private Map<Integer, Material> materiaisCadastrados = new HashMap<>();

    public MaterialController(MaterialRepository repository) {
	this.repository = repository;
	carregarTodosDoArquivo();
}

	public void adicionarMaterial(Material m) {
        if (m == null || m.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Material inválido");
        }

        if (materiaisCadastrados.containsKey(m.getCodigo())) {
            throw new IllegalArgumentException("Já existe material com esse código");
        }

        materiaisCadastrados.put(m.getCodigo(), m);
        repository.salvar(m);
    }

    public void removerMaterial(int codigo) {
        
        if (!materiaisCadastrados.containsKey(codigo)) {
            throw new IllegalArgumentException("Material não encontrado");
        }

        materiaisCadastrados.remove(codigo);
        repository.remover(codigo);
    }

    public Map<Integer, Material> listarMateriaisCadastrados() {
        return new HashMap<>(materiaisCadastrados);
    }

    public Material buscarMaterialPorCodigo(int codigo) {
        if (!materiaisCadastrados.containsKey(codigo)) {
        	throw new IllegalArgumentException("Material não encontrado");
        }
        return materiaisCadastrados.get(codigo);
    }
    
    
    public void editarNome(int codigo, String novoNome) {
    	if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }
    	
    	if (!materiaisCadastrados.containsKey(codigo)) {
            throw new IllegalArgumentException("Material não encontrado");
        }
    	
    	Material m = buscarMaterialPorCodigo(codigo);
    	m.setNome(novoNome);
    	repository.atualizar(m);
    }
    
    public void editarQuantidade(int codigo, int novaQuantidade) {
    	
    	if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
    	
    	if (!materiaisCadastrados.containsKey(codigo)) {
            throw new IllegalArgumentException("Material não encontrado");
        }
    	
    	Material m = buscarMaterialPorCodigo(codigo);
    	m.setQuantidade(novaQuantidade);
    	repository.atualizar(m);
    }
    
    public void editarUnidade(int codigo, UnidadeMedida novaUnidade) {
    	Material m = buscarMaterialPorCodigo(codigo);
    	m.setUnidadeMedida(novaUnidade);
    	repository.atualizar(m);
    }
    
    public void limparMateriais() {
        materiaisCadastrados.clear();
        repository.salvarTodos(new ArrayList<>(materiaisCadastrados.values()));
    }
    
    public void carregarTodosDoArquivo() {
        for (Material m : repository.carregarTodos()) {
            materiaisCadastrados.put(m.getCodigo(), m);
        }
    }

}
