package model.dao;

import java.util.List;

import model.entities.DepartamentoLista;


public interface DepartmentDao {

	void insert(DepartamentoLista obj);
	void update(DepartamentoLista obj);
	void deleteById(Integer id);
	DepartamentoLista findById(Integer id);
	List<DepartamentoLista> findAll();
}
