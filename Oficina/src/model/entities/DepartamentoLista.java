package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class DepartamentoLista implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	
	public DepartamentoLista () {
	}

	public DepartamentoLista (Integer id, String name) {
		this.id = id;
		this.nome = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return nome;
	}

	public void setName(String name) {
		this.nome = name;
	}



	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartamentoLista other = (DepartamentoLista) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + nome + "]";
	}
}
