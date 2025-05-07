package model;

import java.io.Serializable;

public class Material implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigo;
	private String nome;
	private int quantidade;
	private UnidadeMedida unidadeMedida;
	
	public Material(int codigo, String nome, int quantidade, UnidadeMedida unidadeMedida) {
		this.codigo = codigo;
		this.nome = nome;
		this.quantidade = quantidade;
		this.unidadeMedida = unidadeMedida;
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

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
	 if (quantidade < 0) {
	        throw new IllegalArgumentException("Quantidade não pode ser negativa");
	    }
		this.quantidade = quantidade;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	
	
	
}
