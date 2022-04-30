package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.DepartamentoLista;

public class DepartamentoController implements Initializable{

	@FXML
	private TableView<DepartamentoLista>tableViewDepartamento;
	@FXML
	private TableColumn<DepartamentoLista, Integer> tableColumnId;
	@FXML
	private TableColumn<DepartamentoLista, String> tableColumnName;
	@FXML
	private Button btNovo;
	@FXML
	public void onBtNovo() {
		System.out.println("novo");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}


	private void initializeNodes() {
		//isso e um padrao do javafx  para iniciar o comportamento das colunas;
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		//isso serve para o tableView acompanhar a janela no tamanho e nao ficar cortada;
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
		
		
	}

}
