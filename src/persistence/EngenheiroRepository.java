package persistence;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Engenheiro;

public class EngenheiroRepository {

	private static final String CAMINHO_ARQUIVO = "data/engenheiros.dat";
	
	public void salvar(Engenheiro novoEngenheiro) {
	    List<Engenheiro> engenheiros = carregarTodos();

	    for (Engenheiro e : engenheiros) {
	        if (e.getRegistroCREA().equals(novoEngenheiro.getRegistroCREA())) {
	            throw new IllegalArgumentException("Já existe um engenheiro com esse CREA.");
	        }
	    }

	    engenheiros.add(novoEngenheiro);
	    salvarTodos(engenheiros);
	}
	
	public Engenheiro carregar(String crea) {
	    List<Engenheiro> engenheiros = carregarTodos();

	    for (Engenheiro e : engenheiros) {
	        if (e.getRegistroCREA().equals(crea)) {
	            return e;
	        }
	    }

	    throw new IllegalArgumentException("Engenheiro com CREA: " + crea + " não encontrado.");
	}
	
    public void salvarTodos(List<Engenheiro> engenheiros) {
        criarDiretorioSeNecessario();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(engenheiros);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar engenheiros", e);
        }
    }

    public List<Engenheiro> carregarTodos() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Engenheiro>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar engenheiros", e);
        }
    }
    
    private void criarDiretorioSeNecessario() {
        File pasta = new File("data");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }
    
    public void remover(String crea) {
        List<Engenheiro> engenheiros = carregarTodos();
        boolean removido = engenheiros.removeIf(m -> m.getRegistroCREA().equals(crea));
        if (!removido) {
            throw new IllegalArgumentException("Engenheiro com CREA: " + crea + " não encontrado.");
        }
        salvarTodos(engenheiros);
    }
    
    public void atualizar(Engenheiro atualizado) {
        List<Engenheiro> engenheiros = carregarTodos();
        boolean atualizadoComSucesso = false;

        for (int i = 0; i < engenheiros.size(); i++) {
            if (engenheiros.get(i).getRegistroCREA().equals(atualizado.getRegistroCREA())) {
                engenheiros.set(i, atualizado);
                atualizadoComSucesso = true;
                break;
            }
        }

        if (!atualizadoComSucesso) {
            throw new IllegalArgumentException("Engenheiro com CREA: " + atualizado.getRegistroCREA() + " não encontrado.");
        }

        salvarTodos(engenheiros);
    }
}
