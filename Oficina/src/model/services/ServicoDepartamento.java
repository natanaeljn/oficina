package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.DepartamentoLista;

public class ServicoDepartamento {
	public List<DepartamentoLista>findAll(){
		List<DepartamentoLista> list = new ArrayList<>();
		list.add(new DepartamentoLista(1, "livro"));
		list.add(new DepartamentoLista(2, "sla"));
		list.add(new DepartamentoLista(3, "caderno"));
		return list;
	}

	
}
