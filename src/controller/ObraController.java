package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import model.Status;
import persistence.ObraRepository;
import model.Engenheiro;
import model.Obra;

public class ObraController {

	private ObraRepository repository;
    private EngenheiroController engController;
    private Map<Integer, Obra> obrasCadastradas = new HashMap<>();
    
    public ObraController(ObraRepository repository, EngenheiroController engController) {
    	this.repository = repository;
    	this.engController = engController;
    	carregarTodosDoArquivo();
    }
    
    public void cadastrarObra(Obra o, Engenheiro responsavel) {
        if (o == null || responsavel == null) {
            throw new IllegalArgumentException("Dados inválidos");
        }
        
        if (o.getOrcamento() <= 0) {
        	throw new IllegalArgumentException("Orçamento inválido!");
        }
        
        if (o.getEtapas().isEmpty()) {
        	throw new IllegalArgumentException("O campo de etapas està vázio");
        }
        
        if (obrasCadastradas.containsKey(o.getCodigo())) {
        	throw new IllegalArgumentException("Obra já cadastrada");
        }
        
        if (!engController.existeEngenheiro(responsavel)) {
            throw new IllegalStateException("Engenheiro não cadastrado");
        }
        
        obrasCadastradas.put(o.getCodigo(), o);
        responsavel.adicionarObra(o);
        repository.salvar(o);
        
    }
    
   public void atualizarStatus(int codigo, Status novoStatus) {
	   if (!obrasCadastradas.containsKey(codigo)) {
		   throw new IllegalArgumentException("Obra não encontrada"); 
	   }   
	   
	   Obra o = buscarObraPorCodigo(codigo);
	   
	   if (o.getEngenheirosAlocados().isEmpty()) {
		   throw new IllegalArgumentException("Está obra não tem nenhum engenheiro responsável cadastrado");  
	   } 
	   
	   o.setStatus(novoStatus);
	   repository.atualizar(o);
   }
   
   public Obra buscarObraPorCodigo(int codigo) {
	   if (!obrasCadastradas.containsKey(codigo)) {
		   throw new IllegalArgumentException("Obra não encontrada"); 
	   }
	   
	   return obrasCadastradas.get(codigo);
	}
   
   public void removerObra(int codigo) {
	   if (!obrasCadastradas.containsKey(codigo)) {
		   throw new IllegalArgumentException("Obra não encontrada"); 
	   }
	   
	   Obra o = buscarObraPorCodigo(codigo);
	   obrasCadastradas.remove(codigo);
	   for (Engenheiro e : o.getEngenheirosAlocados().values()) {
		    e.removerObra(o.getCodigo());
		    engController.atualizarEngenheiro(e);
		    
		}
	   
	   repository.remover(codigo);
	}
   
   public void adicionarEtapa(int codigo, String etapa) {
	   Obra o = buscarObraPorCodigo(codigo);
	   
	   if (o.getStatus() == Status.CANCELADA) {
		    throw new IllegalStateException("Não é possível editar uma obra cancelada.");
		}
	   
	   o.adicionarEtapa(etapa);
	   repository.atualizar(o);
   }
    
   public Map<Integer, Obra> listarObras() {
	    return new HashMap<>(obrasCadastradas);
	}
   
   public void verificarObrasAtrasadas() {
	    for (Obra o : obrasCadastradas.values()) {
	        o.verificarAtraso();
	    }
	}
   
   public void editarNome(int codigo, String novoNome) {
	   if (novoNome == null || novoNome.trim().isEmpty()) {
	        throw new IllegalArgumentException("Nome inválido");
	    } 
	   
	    Obra o = buscarObraPorCodigo(codigo);
	    o.setNome(novoNome);
	    repository.atualizar(o);
	}

	public void editarLocalizacao(int codigo, String novaLocalizacao) {
		if (novaLocalizacao == null || novaLocalizacao.trim().isEmpty()) {
	        throw new IllegalArgumentException("Localização inválida");
	    }
		
	    Obra o = buscarObraPorCodigo(codigo);
	    o.setLocalizacao(novaLocalizacao);
	    repository.atualizar(o);
	}

	public void editarOrcamento(int codigo, double novoOrcamento) {
	    Obra o = buscarObraPorCodigo(codigo);
	    o.setOrcamento(novoOrcamento);
	    repository.atualizar(o);
	}
	
	public void editarDataEntregaPrevista(int codigo, LocalDate novaData) {
	    Obra o = buscarObraPorCodigo(codigo);
	    o.setDataEntregaPrevista(novaData);
	    repository.atualizar(o);
	}
	
	public void removerEtapa(int codigo, String etapa) {
		Obra o = buscarObraPorCodigo(codigo);
		
	 if (o.getStatus() == Status.CANCELADA) {
	        throw new IllegalStateException("Não é possível editar uma obra cancelada.");
	    }

	    if (!o.getEtapas().contains(etapa)) {
	        throw new IllegalArgumentException("Etapa não encontrada");
	    }

	    if (o.getEtapaAtual().equals(etapa)) {
	        throw new IllegalStateException("Não é possível remover a etapa atual");
	    }

	    o.getEtapas().remove(etapa);
	    repository.atualizar(o);
	}
	
	public Map<Integer, Obra> listarObrasPorStatus(Status status) {
	    Map<Integer, Obra> filtradas = new HashMap<>();
	    for (Map.Entry<Integer, Obra> entry : obrasCadastradas.entrySet()) {
	        if (entry.getValue().getStatus() == status) {
	            filtradas.put(entry.getKey(), entry.getValue());
	        }
	    }
	    return filtradas;
	}
	
	public Map<Integer, Obra> listarObrasPorEngenheiro(String crea) {
	    Map<Integer, Obra> resultado = new HashMap<>();
	    for (Map.Entry<Integer, Obra> entry : obrasCadastradas.entrySet()) {
	        Obra o = entry.getValue();
	        if (o.getEngenheirosAlocados().containsKey(crea)) {
	            resultado.put(entry.getKey(), o);
	        }
	    }
	    return resultado;
	}
	
	public void encerrarObra(int codigo) {
	    Obra o = buscarObraPorCodigo(codigo);

	    if (o.getStatus() == Status.CANCELADA) {
	        throw new IllegalStateException("Obra cancelada não pode ser encerrada");
	    }

	    o.setStatus(Status.CONCLUIDA);
	    o.setDataEntregaReal(LocalDate.now());
	    repository.atualizar(o);
	}
	
	public void reabrirObra(int codigo) {
	    Obra o = buscarObraPorCodigo(codigo);

	    if (o.getStatus() != Status.CANCELADA && o.getStatus() != Status.CONCLUIDA) {
	        throw new IllegalStateException("Apenas obras finalizadas ou canceladas podem ser reabertas");
	    }

	    o.setStatus(Status.EM_ANDAMENTO);
	    o.setDataEntregaReal(null);
	    repository.atualizar(o);
	}
	
	public void carregarTodosDoArquivo() {
	    for (Obra o : repository.carregarTodos()) {
	        obrasCadastradas.put(o.getCodigo(), o);
	    }
	}

	public void atualizarObra(Obra o) {
	    obrasCadastradas.put(o.getCodigo(), o);
	    repository.atualizar(o);
	}
	
	public void atualizarCREAEmTodasObras(String antigoCREA, String novoCREA) {
	    for (Obra o : obrasCadastradas.values()) {
	        if (o.getEngenheirosAlocados().containsKey(antigoCREA)) {
	            Engenheiro e = o.getEngenheirosAlocados().remove(antigoCREA);
	            e.setRegistroCREA(novoCREA);
	            o.getEngenheirosAlocados().put(novoCREA, e);
	            repository.atualizar(o);
	        }
	    }
	}
}
