package gui;

import java.io.IOException;
import java.net.URL;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.DepartamentoLista;
import model.services.ServicoDepartamento;

public class DepartamentoController implements Initializable, MudarDataListado {

	private ServicoDepartamento servico;

	@FXML
	private TableView<DepartamentoLista> tableViewDepartamento;
	@FXML
	private TableColumn<DepartamentoLista, Integer> tableColumnId;
	@FXML
	private TableColumn<DepartamentoLista, String> tableColumnName;
	@FXML
	private TableColumn<DepartamentoLista, DepartamentoLista> tableColumnEdicao;
	@FXML
	private TableColumn<DepartamentoLista, DepartamentoLista> tableColumnRemover;
	@FXML
	private Button btNovo;
	private ObservableList<DepartamentoLista> obsList;

	@FXML

	public void onBtNovo(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DepartamentoLista obj = new DepartamentoLista();
		createDialogForm(obj, "/gui/DepartamentoForma.fxml", parentStage);
	}

	public void setDepartamento(ServicoDepartamento servico) {
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
		// isso serve para o tableView acompanhar a janela no tamanho e nao ficar
		// cortada;
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (servico == null) {
			throw new IllegalStateException("serviço esta nullo");
		}
		List<DepartamentoLista> list = servico.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(DepartamentoLista obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartamentoFormaController controller = loader.getController();
			controller.setDepartamento(obj);
			controller.setServicoDepartamento(new ServicoDepartamento());
			controller.reescreverData(this);
			controller.UpdateFormaData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerta.showAlert("IO excessao", "errro de carregamento", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataMudanca() {
		updateTableView();

	}

	// vai criar um botao de ediçao em cada linha da tabela
	private void initEditButtons() {
		tableColumnEdicao.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdicao.setCellFactory(param -> new TableCell<DepartamentoLista, DepartamentoLista>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(DepartamentoLista obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartamentoForma.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<DepartamentoLista, DepartamentoLista>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(DepartamentoLista obj, boolean empty) {
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

	private void removeEntity(DepartamentoLista obj) {
		Optional<ButtonType> result = Alerta.showConfirmation("confirmacao", "Tem certeza que deseja deletar?");
		// usamos o get por que o optional carrega outro objeto dentro dele
		if (result.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("servico esta nulo");
			}
			try {
				servico.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerta.showAlert("erro ao remover", null, "erro ao remover o objeto ", AlertType.ERROR);
			}
		}
	}

}
