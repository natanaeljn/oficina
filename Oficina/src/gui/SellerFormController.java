package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alerta;
import gui.util.Restriçoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.DepartamentoLista;
import model.entities.Seller;
import model.exceptions.excessaoValidacao;
import model.services.ServicoDepartamento;
import model.services.VendedorDepartamento;

public class SellerFormController implements Initializable {

	private VendedorDepartamento servico;

	private ServicoDepartamento servicoDepart;

	private List<MudarDataListado> MudancaDeDatas = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker txtdbNascimento;
	@FXML
	private TextField txtSalario;
	@FXML
	private ComboBox<DepartamentoLista> comboBoxDepartamento;

	@FXML
	private Label labelErro;
	@FXML
	private Label labelErroEmail;
	@FXML
	private Label labelErroNascimento;
	@FXML
	private Label labelErroSalario;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancela;

	private ObservableList<DepartamentoLista> obsList;

	private Seller entidade;

	// serve para setar novos departamentos
	public void setSeller(Seller entidade) {
		this.entidade = entidade;
	}

	public void setServicos(VendedorDepartamento servico, ServicoDepartamento servicoDepart) {
		this.servico = servico;
		this.servicoDepart = servicoDepart;
	}

	public void reescreverData(MudarDataListado novo) {
		MudancaDeDatas.add(novo);
	}

	@FXML
	public void onBtSalvarAcao(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		if (servico == null) {
			throw new IllegalStateException("serviço esta nulo");
		}
		try {
			entidade = getFormaData();
			servico.saveOrUpdate(entidade);
			notificacaoMudarData();
			Utils.currentStage(event).close();
		} catch (excessaoValidacao e) {
			setMensagemErro(e.Geterros());
		} catch (DbException e) {
			Alerta.showAlert("erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificacaoMudarData() {
		for (MudarDataListado novo : MudancaDeDatas) {
			novo.onDataMudanca();
		}

	}

	private Seller getFormaData() {
		Seller obj = new Seller();
		excessaoValidacao excessao = new excessaoValidacao("erro na validaçao");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		// o trim elimina qualquer espaço em branco no inicio ou no final
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excessao.addErro("nome", "o campo nao pode ser vazio");
		}
		obj.setName(txtNome.getText());
		if (excessao.Geterros().size() > 0) {
			throw excessao;
		}
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excessao.addErro("email", "o campo nao pode ser vazio");
		}
		obj.setEmail(txtEmail.getText());
		//instant pega o valor do datepicker e converte
		if(txtdbNascimento.getValue()==null) {
			excessao.addErro("birthDate", "o campo nao pode ser vazio");
		}
		else {
		Instant instant = Instant.from(txtdbNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
		
		obj.setBirthDate(Date.from(instant));
		}
		if (txtSalario.getText() == null || txtSalario.getText().trim().equals("")) {
			excessao.addErro("baseSalary", "o campo nao pode ser vazio");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtSalario.getText()));
		
		obj.setDepartment(comboBoxDepartamento.getValue());
		if (excessao.Geterros().size() > 0) {
			throw excessao;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAcao(ActionEvent event) {
		// serve para fechar a janela ao clicar
		Utils.currentStage(event).close();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		IniatializeNode();

	}

	private void IniatializeNode() {
		// restringe so o uso de inteiros
		Restriçoes.setTextFieldInteger(txtId);
		// o nome tera no maximo 30 letras
		Restriçoes.setTextFieldMaxLength(txtNome, 70);
		Restriçoes.setTextFieldDouble(txtSalario);
		Restriçoes.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(txtdbNascimento, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	// vai colocar os valores da classe na caixinha;
	public void UpdateFormaData() {
		if (entidade == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(String.valueOf(entidade.getName()));
		txtEmail.setText(String.valueOf(entidade.getName()));
		Locale.setDefault(Locale.US);
		txtSalario.setText(String.format("%.2f", entidade.getBaseSalary()));
		if (entidade.getBirthDate() != null) {
			txtdbNascimento.setValue(LocalDate.ofInstant(entidade.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entidade.getDepartment()==null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		}
		else {
		comboBoxDepartamento.setValue(entidade.getDepartment());
		}
	}

	public void carregarObjetosAssociados() {

		List<DepartamentoLista> list = servicoDepart.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);
	}

	private void setMensagemErro(Map<String, String> erro) {
		Set<String> fields = erro.keySet();
		if (fields.contains("name")) {
			labelErro.setText(erro.get("name"));

		}
		else {
			labelErro.setText("");
		}
		if (fields.contains("email")) {
			 labelErroEmail.setText(erro.get("email"));

		}
		else {
			labelErroEmail.setText("");
		}
		if (fields.contains("baseSalary")) {
			labelErroSalario.setText(erro.get("baseSalary"));

		}
		else {
			labelErroSalario.setText("");
		}
		
		if (fields.contains("birthDate")) {
			labelErroNascimento.setText(erro.get("birthDate"));

		}
		else {
			labelErroNascimento.setText("");
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<DepartamentoLista>, ListCell<DepartamentoLista>> factory = lv -> new ListCell<DepartamentoLista>() {
			@Override
			protected void updateItem(DepartamentoLista item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
