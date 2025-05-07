package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Obra;

public class ObraRepository {

private static final String CAMINHO_ARQUIVO = "data/obras.dat";
	
	public void salvar(Obra novaObra) {
	    List<Obra> obras = carregarTodos();

	    for (Obra o : obras) {
	        if (o.getCodigo() == novaObra.getCodigo()) {
	            throw new IllegalArgumentException("Já existe uma obra com esse código.");
	        }
	    }

	    obras.add(novaObra);
	    salvarTodos(obras);
	}
	
	public Obra carregar(int codigo) {
		List<Obra> obras = carregarTodos();

		for (Obra o : obras) {
	        if (o.getCodigo() == codigo) {
	            return o;
	        }
	    }

	    throw new IllegalArgumentException("Obra com código " + codigo + " não encontrado.");
	}
	
    public void salvarTodos(List<Obra> obras) {
        criarDiretorioSeNecessario();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(obras);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar obras\n", e);
        }
    }

    public List<Obra> carregarTodos() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Obra>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar obras", e);
        }
    }
    
    private void criarDiretorioSeNecessario() {
        File pasta = new File("data");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }
    
    public void remover(int codigo) {
        List<Obra> obras = carregarTodos();
        boolean removido = obras.removeIf(m -> m.getCodigo() == codigo);
        if (!removido) {
            throw new IllegalArgumentException("Obra com código " + codigo + " não encontrado.");
        }
        salvarTodos(obras);
    }
    
    public void atualizar(Obra atualizado) {
        List<Obra> obras = carregarTodos();
        boolean atualizadoComSucesso = false;

        for (int i = 0; i < obras.size(); i++) {
            if (obras.get(i).getCodigo() == atualizado.getCodigo()) {
                obras.set(i, atualizado);
                atualizadoComSucesso = true;
                break;
            }
        }

        if (!atualizadoComSucesso) {
            throw new IllegalArgumentException("Obra com código " + atualizado.getCodigo() + " não encontrado.");
        }

        salvarTodos(obras);
    }
  
}