package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entities.DepartamentoLista;
import model.services.ServicoDepartamento;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemVendedor() {
		System.out.println(" action");
	}

	@FXML
	public void onMenuItemDepartamento() {
		loadView("/gui/Departamento.fxml",(DepartamentoController controller) -> {
			controller.setDepartamento(new ServicoDepartamento());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSobre() {
		loadView("/gui/Sobre.fxml", x->{} );
	}

	@Override
	public void initialize(URL url, ResourceBundle arg1) {

	}

	// Instancia o fxml com o loader
	//esse synchronized garante que o processo nao vai ser interrompido ;
	private synchronized<T> void loadView(String absoluteName,Consumer<T>acaoInicial) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			T controller = loader.getController();
			acaoInicial.accept(controller);
			
			
		} catch (IOException e) {
			Alerta.showAlert("Excessao IO", "erro de carregamento da pagina", e.getMessage(), AlertType.ERROR);
		}
	}
	private synchronized void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			DepartamentoController controller = loader.getController();
			controller.setDepartamento(new ServicoDepartamento());
			controller.updateTableView();
		} catch (IOException e) {
			Alerta.showAlert("Excessao IO", "erro de carregamento da pagina", e.getMessage(), AlertType.ERROR);
		}
	}

}



