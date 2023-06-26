package fr.iutfbleau.jbltool;

import fr.iutfbleau.jbltool.Interface.ControlChoixFichier;
import fr.iutfbleau.jbltool.Interface.ManuelInterface;
import fr.iutfbleau.jbltool.documentation.DocumentationParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);

		ManuelInterface mi = new ManuelInterface();
		mi.init();

		ControlChoixFichier mainController = (ControlChoixFichier) loader.getController();
		mainController.setStage(root);
		mainController.setManuelInterface(mi);


		// set the stylesheet (used by CodeArea)
		scene.getStylesheets().add(App.class.getResource("/java-keywords.css").toExternalForm());

		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		// init the documentation
		try {
			DocumentationParser.readFrom(new File(App.class.getResource("/documentation.xml").toURI()));
		} catch (URISyntaxException|SAXException|ParserConfigurationException|IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible de lire la documentation.");
			alert.setHeaderText(e.getClass().getName());
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return ; // don't continue
		}
		launch(args);
	}
}
