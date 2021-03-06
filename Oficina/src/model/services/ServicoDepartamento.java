package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.DepartamentoLista;
import model.entities.Seller;


public class ServicoDepartamento {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	
	public List<DepartamentoLista> findAll(){
	    return dao.findAll();
	}
	public void saveOrUpdate(DepartamentoLista obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	public void remove (DepartamentoLista obj) {
		dao.deleteById(obj.getId());
	}

	
}
