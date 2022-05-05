package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.SpringLayout.Constraints;

import gui.util.Restriçoes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartamentoFormaController implements Initializable{

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
	@FXML
	public void onBtSalvarAcao(){
		System.out.println("funcionou");
	}
	@FXML
	public void onBtCancelarAcao(){
		System.out.println("deu boa");
		
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

}
