package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Obra implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigo;
	private String nome;
	private String localizacao;
	private double orcamento;
	private Status status;
	private int indiceEtapaAtual;
	private LocalDate dataInicio;
	private LocalDate dataEntregaPrevista;
	private LocalDate dataEntregaReal;
	
	private ArrayList<String> etapas = new ArrayList<>();
	private Map<String, Engenheiro> engenheirosAlocados = new HashMap<>();
	private ArrayList<Material> materiaisUtilizados = new ArrayList<>();
	
	public Obra(int codigo, 
			String nome, String localizacao, 
			double orcamento, Status status,
			LocalDate dataInicio, LocalDate dataEntregaPrevista) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.localizacao = localizacao;
		this.orcamento = orcamento;
		this.status = status;
		this.indiceEtapaAtual = 0;
		this.dataInicio = dataInicio;
		this.dataEntregaPrevista = dataEntregaPrevista;
		this.dataEntregaReal = null;
		
	}
	
	public void alocarEngenheiro(Engenheiro e) {
		if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível fazer alocação, obra cancelada.");
		}
		
		if(engenheirosAlocados.containsKey(e.getRegistroCREA())) {
			throw new IllegalArgumentException("Engenheiro já alocado para está obra");
		}
		
		engenheirosAlocados.put(e.getRegistroCREA(), e);
	}
	
	public void removerEngenheiro(Engenheiro e) {
		if(!engenheirosAlocados.containsKey(e.getRegistroCREA())) {
			throw new IllegalArgumentException("Engenheiro não encontrado");
		}
		
		engenheirosAlocados.remove(e.getRegistroCREA());
	}
	
	public void adicionarEtapa(String etapa) {
		
		if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível modificar, obra cancelada.");
		}
		
        this.etapas.add(etapa);
    }

    public void adicionarEtapas(ArrayList<String> novasEtapas) {
    	if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível modificar, obra cancelada.");
		}
    	
        this.etapas.addAll(novasEtapas);
    }
	
	public void avancarEtapa() {
		if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível modificar, obra cancelada.");
		}
		
		if (indiceEtapaAtual == etapas.size()) {
			throw new IllegalArgumentException("Obra finalizada! Não tem mais etapas para avançar");
		} else {
			this.indiceEtapaAtual += 1;
		}
	}
	
	public String getEtapaAtual() {
		return this.etapas.get(indiceEtapaAtual);
	}
	
	public boolean estaNaEtapa(String etapa) {
	    return this.etapas.get(indiceEtapaAtual).equals(etapa);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public double getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(double orcamento) {
		if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível modificar, obra cancelada.");
		}
		
		if (orcamento <= 0) {
	        throw new IllegalArgumentException("Orçamento deve ser maior que zero!");
	    }
	    
		this.orcamento = orcamento;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getIndiceEtapaAtual() {
		return indiceEtapaAtual;
	}

	public void setIndiceEtapaAtual(int indiceEtapaAtual) {
		if (this.isCancelada()) {
		    throw new IllegalStateException("Não é possível modificar, obra cancelada.");
		} 
		
		this.indiceEtapaAtual = indiceEtapaAtual;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataEntregaPrevista() {
		return dataEntregaPrevista;
	}

	public void setDataEntregaPrevista(LocalDate dataEntregaPrevista) {
		if (status == Status.CANCELADA || status == Status.ATRASADA || status == Status.CONCLUIDA) {
		    throw new IllegalStateException("Não é possível modificar a data em obras finalizadas ou atrasadas.");
		}
		
		this.dataEntregaPrevista = dataEntregaPrevista;
	}

	public LocalDate getDataEntregaReal() {
		return dataEntregaReal;
	}

	public ArrayList<String> getEtapas() {
		return new ArrayList<>(etapas);
	}

	public Map<String, Engenheiro> getEngenheirosAlocados() {
		return Map.copyOf(engenheirosAlocados);
	}

	public ArrayList<Material> getMateriaisUtilizados() {
		return new ArrayList<>(materiaisUtilizados);
	}
	
	public void adicionarMaterial(Material m) {
	    materiaisUtilizados.add(m);
	}
	
	public void verificarAtraso() {
	    if (status == Status.EM_ANDAMENTO && LocalDate.now().isAfter(dataEntregaPrevista)) {
	        this.status = Status.ATRASADA;
	    }
	}
	
	public boolean isCancelada() {
	    return status == Status.CANCELADA;
	}

	public boolean isAtrasada() {
	    return status == Status.ATRASADA;
	}

	public boolean isFinalizada() {
	    return status == Status.CONCLUIDA;
	}
	
	public boolean isEngenheiroAlocado(String registroCREA) {
	    return engenheirosAlocados.containsKey(registroCREA);
	}
	
	@Override
	public String toString() {
	    return String.format(
	        "Obra [%d] - %s | Local: %s | Status: %s | Orçamento: R$ %.2f",
	        codigo,
	        nome,
	        localizacao,
	        status.getDescricao(),
	        orcamento
	    );
	}

	public void setDataEntregaReal(LocalDate dataEntregaReal) {
		this.dataEntregaReal = dataEntregaReal;
	}

	
}
