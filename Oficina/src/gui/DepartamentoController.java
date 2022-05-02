package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.DepartamentoLista;
import model.services.ServicoDepartamento;

public class DepartamentoController implements Initializable{
    
	private ServicoDepartamento servico;
	
    
    @FXML
	private TableView<DepartamentoLista>tableViewDepartamento;
	@FXML
	private TableColumn<DepartamentoLista, Integer> tableColumnId;
	@FXML
	private TableColumn<DepartamentoLista, String> tableColumnName;
	@FXML
	private Button btNovo;
	private ObservableList<DepartamentoLista> obsList;
	
	@FXML
	
	public void onBtNovo() {
		System.out.println("novo");
	}
	
	public void setDepartamento(ServicoDepartamento servico) {
		this.servico = servico;
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
	public void updateTableView() {
		if(servico ==null) {
			throw new IllegalStateException("serviço esta nullo");
		}
		List<DepartamentoLista> list = servico.findAll();
		obsList= FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}

}
