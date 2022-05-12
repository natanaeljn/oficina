package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.util.Alerta;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.VendedorDepartamento;

public class VendedorController implements Initializable, MudarDataListado {

	private VendedorDepartamento servico;

	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	@FXML
	private TableColumn<Seller, Date> tableColumnNiver;
	@FXML
	private TableColumn<Seller, Double> tableColumnSalarioBase;
	@FXML
	private TableColumn<Seller, Seller> tableColumnEdicao;
	@FXML
	private TableColumn<Seller, Seller> tableColumnRemover;
	@FXML
	private Button btNovo;
	private ObservableList<Seller> obsList;

	@FXML

	public void onBtNovo(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		//createDialogForm(obj, "/gui/DepartamentoForma.fxml", parentStage);
	}

	public void setVendedor(VendedorDepartamento servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		// isso e um padrao do javafx para iniciar o comportamento das colunas;
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnNiver.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnNiver, "dd/MM/yyyy");
		tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		
		
		Utils.formatTableColumnDouble(tableColumnSalarioBase,2);
		// isso serve para o tableView acompanhar a janela no tamanho e nao ficar
		// cortada;
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (servico == null) {
			throw new IllegalStateException("serviço esta nullo");
		}
		List<Seller> list = servico.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	//private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		//try {
			//FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//Pane pane = loader.load();

			//DepartamentoFormaController controller = loader.getController();
			//controller.setDepartamento(obj);
			//controller.setServicoDepartamento(new ServicoDepartamento());
			//controller.reescreverData(this);
			//controller.UpdateFormaData();

			//Stage dialogStage = new Stage();
			//dialogStage.setTitle("entre com os dados do departamento");
			//dialogStage.setScene(new Scene(pane));
			//dialogStage.setResizable(false);
			//dialogStage.initOwner(parentStage);
			//dialogStage.initModality(Modality.WINDOW_MODAL);
			//dialogStage.showAndWait();
		//} catch (IOException e) {
			//Alerta.showAlert("IO excessao", "errro de carregamento", e.getMessage(), AlertType.ERROR);
		//}
	//}

	@Override
	public void onDataMudanca() {
		updateTableView();

	}

	// vai criar um botao de ediçao em cada linha da tabela
	private void initEditButtons() {
		tableColumnEdicao.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdicao.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
			//	button.setOnAction(
						//event -> createDialogForm(obj, "/gui/DepartamentoForma.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerta.showConfirmation("confirmacao", "Tem certeza que deseja deletar?");
		// usamos o get por que o optional carrega outro objeto dentro dele
		if (result.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("servico esta nulo");
			}
			try {
				servico.remove(obj);;
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerta.showAlert("erro ao remover", null, "erro ao remover o objeto ", AlertType.ERROR);
			}
		}
	}

}
