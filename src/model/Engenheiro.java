package model;

import java.util.HashMap;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engenheiro implements Serializable {

	private String registroCREA;
	private String nome;
	private Especialidade especialidade;
	
	private static final long serialVersionUID = 1L;
	private Map<Integer, Obra> obrasAssociadas = new HashMap<>();
	
	public Engenheiro(String registroCREA, String nome, Especialidade especialidade) {
		this.registroCREA = registroCREA;
		this.nome = nome;
		this.especialidade = especialidade;
	}
	
	public boolean validarCREA() {
		String regex = "^\\d{5,7}-?\\d{1}[DTA]?\\/[A-Z]{2}$";
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.registroCREA);
        return matcher.matches();
	}
	
	public boolean validarCREA(String registroCREA) {
		String regex = "^\\d{5,7}-?\\d{1}[DTA]?\\/[A-Z]{2}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(registroCREA);
		return matcher.matches();
	}
	
	public void adicionarObra(Obra obra) {
		if (obra.getStatus() == Status.CANCELADA) {
		    throw new IllegalStateException("Não é possível fazer alocação, obra cancelada.");
		}
		
		if (obrasAssociadas.containsKey(obra.getCodigo())) {
			throw new IllegalArgumentException("Obra já adicionada");
		}
		
	    this.obrasAssociadas.put(obra.getCodigo(), obra);
	}
	
	public void removerObra(int codigoObra) {
		if (!obrasAssociadas.containsKey(codigoObra)) {
			throw new IllegalArgumentException("Não encontrada na lista de obras associadas");
		}
		
		obrasAssociadas.remove(codigoObra);
	}
	
	public void setRegistroCREA(String registroCREA) {
		if (!validarCREA(registroCREA)) {
	       throw new IllegalArgumentException("CREA inválido! Campo ficou vazio, preencha o quanto antes.");
	    }
		
		this.registroCREA = registroCREA;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEspecialidade(Especialidade novaEspecialidade) {
		this.especialidade = novaEspecialidade;
	}
	
	public String getRegistroCREA() {
		return registroCREA;
	}

	public String getNome() {
		return nome;
	}
	
	public Especialidade getEspecialidade() {
		return especialidade;
	}
	
	public Map<Integer, Obra> getObrasAssociadas() {
		return obrasAssociadas;
	}
	
	@Override
	public String toString() {
	    return String.format("Engenheiro: %s (CREA: %s) - Especialidade: %s", this.nome, this.registroCREA, this.especialidade);
	}
	
}
