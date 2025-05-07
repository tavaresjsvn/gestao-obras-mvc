package persistence;

import java.io.*;
import java.util.*;
import model.Material;

public class MaterialRepository {

	private static final String CAMINHO_ARQUIVO = "data/materiais.dat";
	
	public void salvar(Material novoMaterial) {
	    List<Material> materiais = carregarTodos();

	    for (Material m : materiais) {
	        if (m.getCodigo() == novoMaterial.getCodigo()) {
	            throw new IllegalArgumentException("Já existe um material com esse código.");
	        }
	    }

	    materiais.add(novoMaterial);
	    salvarTodos(materiais);
	}
	
	public Material carregar(int codigo) {
	    List<Material> materiais = carregarTodos();

	    for (Material m : materiais) {
	        if (m.getCodigo() == codigo) {
	            return m;
	        }
	    }

	    throw new IllegalArgumentException("Material com código " + codigo + " não encontrado.");
	}
	
    public void salvarTodos(List<Material> materiais) {
        criarDiretorioSeNecessario();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(materiais);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar materiais", e);
        }
    }

    public List<Material> carregarTodos() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Material>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar materiais", e);
        }
    }
    
    private void criarDiretorioSeNecessario() {
        File pasta = new File("data");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }
    
    public void remover(int codigo) {
        List<Material> materiais = carregarTodos();
        boolean removido = materiais.removeIf(m -> m.getCodigo() == codigo);
        if (!removido) {
            throw new IllegalArgumentException("Material com código " + codigo + " não encontrado.");
        }
        salvarTodos(materiais);
    }
    
    public void atualizar(Material atualizado) {
        List<Material> materiais = carregarTodos();
        boolean atualizadoComSucesso = false;

        for (int i = 0; i < materiais.size(); i++) {
            if (materiais.get(i).getCodigo() == atualizado.getCodigo()) {
                materiais.set(i, atualizado);
                atualizadoComSucesso = true;
                break;
            }
        }

        if (!atualizadoComSucesso) {
            throw new IllegalArgumentException("Material com código " + atualizado.getCodigo() + " não encontrado.");
        }

        salvarTodos(materiais);
    }
}
