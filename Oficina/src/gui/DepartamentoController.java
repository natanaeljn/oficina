package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerta;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	
	public void onBtNovo(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("/gui/DepartamentoForma.fxml", parentStage);
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
	private void createDialogForm(String absoluteName,Stage  parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane  = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerta.showAlert("IO excessao", "errro de carregamento", e.getMessage(), AlertType.ERROR);
		}
	}

}
