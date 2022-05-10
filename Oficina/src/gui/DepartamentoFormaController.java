package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerta;
import gui.util.Restriçoes;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.DepartamentoLista;
import model.services.ServicoDepartamento;

public class DepartamentoFormaController implements Initializable{

	private ServicoDepartamento servico ;
	
	private List<MudarDataListado> MudancaDeDatas = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Label labelErro;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancela;
	 
	private DepartamentoLista entidade;
	
	 
	//serve para setar novos departamentos
	public void setDepartamento(DepartamentoLista entidade) {
		this.entidade = entidade;
	}
	public void setServicoDepartamento(ServicoDepartamento servico) {
		this.servico= servico;
	}
	public void reescreverData(MudarDataListado novo) {
		MudancaDeDatas.add(novo);
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent event){
		if(entidade == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		if(servico == null) {
			throw new IllegalStateException("serviço esta nulo");
		}
		try {
		 entidade = getFormaData();
		 servico.saveOrUpdate(entidade);
		 notificacaoMudarData();
		 Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerta.showAlert("erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void notificacaoMudarData() {
		for(MudarDataListado novo: MudancaDeDatas) {
			novo.onDataMudanca();
		}
		
	}
	private DepartamentoLista getFormaData() {
		DepartamentoLista obj  = new DepartamentoLista();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtNome.getText());
		return obj;
	}
	@FXML
	public void onBtCancelarAcao(ActionEvent event){
        //serve para fechar a janela ao clicar
		Utils.currentStage(event).close();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		IniatializeNode();
		
	}
	private void IniatializeNode() {
		//restringe so o uso de inteiros
		Restriçoes.setTextFieldInteger(txtId);
		//o nome tera no maximo 30 letras
		Restriçoes.setTextFieldMaxLength(txtNome, 30);
	}

	//vai colocar os valores da classe na caixinha;
	public void UpdateFormaData() {
		if(entidade==null) {
			throw new IllegalStateException("entidade esta nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(String.valueOf(entidade.getName()));
	}
}
