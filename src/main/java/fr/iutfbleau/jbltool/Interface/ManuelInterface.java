package fr.iutfbleau.jbltool.Interface;

import fr.iutfbleau.jbltool.documentation.DocumentationParser;
import fr.iutfbleau.jbltool.documentation.InstructionDocumentation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/**
 * A class handling the manuel.
 * It loads itself the fxml and populate itself the instructions from the {@link DocumentationParser}.
 */
public class ManuelInterface {

    private Parent root;
    private Stage stage;
    private FXMLLoader loader;
    private ManuelController manuelController;
    private Scene scene;
    private boolean initialized = false;

    /**
     * Represent the interface for the manuel.
     * Note that this class <b>DOES NOT</b> initialize the {@link DocumentationParser} with {@link DocumentationParser#readFrom(File)}.
     * You have to do it before calling {@link ManuelInterface#init()}.
     */
    public ManuelInterface() {
        this.stage = new Stage();
        loader = new FXMLLoader(getClass().getResource("/Manuel.fxml"));
    }

    /**
     * Returns the controller of this instance.
     * @return
     */
    public ManuelController getController() {
        return this.manuelController;
    }

    /**
     * Initialize the interface.
     * @throws IOException
     * If the interface couldn't be load from it's FXML file.
     */
    public void init() throws IOException {
        root = (Parent) loader.load(); // throws IOException
        manuelController = (ManuelController) loader.getController();
        manuelController.init();
        this.populateInstructionsList();

        scene = new Scene(root);

        stage.initModality(Modality.APPLICATION_MODAL); // always on top
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Dictionnaire des instructions");
        stage.setScene(scene);
        this.initialized = true;
    }

    /**
     * Show the interface.
     * @throws IllegalStateException
     * If init() was not called before.
     * @see ManuelInterface#init()
     */
    public void show() throws IllegalStateException {
        if(!initialized) {
            throw new IllegalStateException("Not initialized, please call init() before trying to show the interface.");
        }
        stage.show();
    }

    /**
     * Add instruction names in the list of instructions names.
     */
    private void populateInstructionsList() {
        for(InstructionDocumentation id : DocumentationParser.getInstructions()) {
            manuelController.addInstructionNameInView(id.getName());
        }
    }
}
