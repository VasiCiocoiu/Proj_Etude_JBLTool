package fr.iutfbleau.jbltool.Interface;

import java.io.File;
import java.io.IOException;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import fr.iutfbleau.jbltool.managers.ClassManager;
import fr.iutfbleau.jbltool.managers.JfileReader;
import fr.iutfbleau.jbltool.utils.ASMStringyfier;
import fr.iutfbleau.jbltool.utils.RichTextUtils;
import fr.iutfbleau.jbltool.utils.VisibleParagraphStyler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.text.SimpleDateFormat;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ControlChoixFichier {

    private ClassManager classManager;
    private Parent stage;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");;


    @FXML
    private Pane classPane;

    @FXML
    private Pane javaPane;

    @FXML
    private Button buttonJava;

    @FXML
    private Button buttonClass;

    @FXML
    private Button buttonDoc;

    @FXML
    private Label filepath1;

    @FXML
    private Label creation1;

    @FXML
    private Label modification1;

    @FXML
    private Label filepath2;

    @FXML
    private Label creation2;

    @FXML
    private Label modification2;

    ManuelInterface mi;

    /**
     * Controller used by the main interface.
     */
    public ControlChoixFichier() {
        this.classManager = new ClassManager(); // init the class manager
    }

    /**
     * Set the {@link ManuelInterface} to show when clicking on the Documentation button.
     * @param mi
     * The {@link ManuelInterface} to use.
     */
    public void setManuelInterface(ManuelInterface mi) {
        this.mi = mi;
    }
    public String getCreationDate(File selectedFile) throws IOException{

      BasicFileAttributes attr = Files.readAttributes(selectedFile.toPath(),BasicFileAttributes.class);
      FileTime date = attr.creationTime();
      String dateCreated = dateFormat.format(date.toMillis());
      return dateCreated;

    }

    @FXML
    void onChargementFichierClassPressed(ActionEvent event) {
        // should not happen in production
        if(this.classManager == null) {
            System.err.println("ClassManager is not set, please set it using setClassManager().");
            return ;
        } else if(this.stage == null) {
            System.err.println("Stage is not set, please set it using setStage().");
            return ;
        }

        // create and display a file chooser with some extensions filter
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .class file");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Class file", "*.class"),
                new ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(this.stage.getScene().getWindow());
        if (selectedFile != null) {
            // handle file
            try {
                this.classManager.readFromFile(selectedFile);
            } catch(Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur de lecture.");
                alert.setHeaderText("Erreur de lecture du fichier.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return ;
            }
        } else {
            return ;
        }

        // create the accordion containing two CodeArea
        Accordion accordion = new Accordion();
        accordion.prefWidthProperty().bind(classPane.widthProperty());
        accordion.prefHeightProperty().bind(classPane.heightProperty());
        VBox infoClassBox = new VBox();
        VBox codeBox = new VBox();

        TitledPane infoTitledPane = new TitledPane("Informations classe", infoClassBox);
        infoTitledPane.setLayoutX(1);
        accordion.getPanes().add(infoTitledPane);
        accordion.getPanes().add(new TitledPane("Code", codeBox));
        this.classPane.getChildren().add(accordion);

        // ADD CONTENT

        // info classe
        CodeArea ca_info = new CodeArea(ASMStringyfier.getClassInfo(this.classManager.getClassNode()));
        ca_info.setEditable(false);

        // code
        CodeArea ca_code = new CodeArea(ASMStringyfier.getClassCode(this.classManager.getClassNode()));
        // set syntax highlighting for java
        ca_code.setParagraphGraphicFactory(LineNumberFactory.get(ca_code));
        ca_code.getVisibleParagraphs().addModificationObserver( // set the syntax highlighting
            new VisibleParagraphStyler<>(ca_code, RichTextUtils::computeHighlighting)
        );
        ca_code.setEditable(false); // not meant to be edited

        // take whole parent pane's size
        ca_info.prefWidthProperty().bind(infoClassBox.widthProperty());
        ca_info.prefHeightProperty().bind(infoClassBox.heightProperty());
        ca_code.prefWidthProperty().bind(codeBox.widthProperty());
        ca_code.prefHeightProperty().bind(codeBox.heightProperty());

        // add class content
        infoClassBox.getChildren().add(new VirtualizedScrollPane<CodeArea>(ca_info));
        codeBox.getChildren().add(new VirtualizedScrollPane<CodeArea>(ca_code));

        //info selected file
        filepath2.setText(selectedFile.getPath());
        modification2.setText("Dernière Modification: "+dateFormat.format(selectedFile.lastModified()));
        try{
          creation2.setText("Date Creation: " + getCreationDate(selectedFile));
        }catch (IOException exception){
          creation2.setText("Date Creation: Indisponible");
        }

        // hide select button
        buttonClass.setManaged(false);
        buttonClass.setVisible(false);
    }

    @FXML
    void onChargementFichierJavaPressed(ActionEvent event) {
        // should not happen when in production
        if(this.classManager == null) {
            System.err.println("ClassManager is not set, please set it using setClassManager().");
            return;
        } else if(this.stage == null) {
            System.err.println("Stage is not set, please set it using setStage().");
            return;
        }

        // create and display a file chooser with some extensions filter
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .java file");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Java source code file", "*.java"),
                new ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(this.stage.getScene().getWindow());
        String contentCode = null;
        if (selectedFile != null) {
            // handle file
            try {
                contentCode = JfileReader.readFile(selectedFile);
            } catch(IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur de lecture.");
                alert.setHeaderText("Erreur de lecture du fichier.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return ;
            }
        } else {
            return ;
        }

        HBox pane = new HBox();
        pane.prefWidthProperty().bind(javaPane.widthProperty());
        pane.prefHeightProperty().bind(javaPane.heightProperty());

        CodeArea ca = new CodeArea(contentCode);
        // set syntax highlighting for java
        ca.setParagraphGraphicFactory(LineNumberFactory.get(ca));
        ca.getVisibleParagraphs().addModificationObserver(
            new VisibleParagraphStyler<>(ca, RichTextUtils::computeHighlighting)
        );
        ca.setEditable(false);

        // take full width and height of the parent even if it's width and height change
        ca.prefWidthProperty().bind(pane.widthProperty());
        ca.prefHeightProperty().bind(pane.heightProperty());

        pane.getChildren().add(new VirtualizedScrollPane<CodeArea>(ca));
        javaPane.getChildren().add(pane);

        //info selected file
        filepath1.setText(selectedFile.getPath());
        modification1.setText("Dernière Modification: "+dateFormat.format(selectedFile.lastModified()));
        try{
          creation1.setText("Date Creation: " + getCreationDate(selectedFile));
        }catch (IOException exception){
          creation1.setText("Date Creation: Indisponible");
        }


        // hide select button
        buttonJava.setManaged(false);
        buttonJava.setVisible(false);
    }

    /**
     * Set the Stage of this controller.
     * @param stage
     * The stage of this controller.
     */
    public void setStage(Parent stage)  {
        this.stage = stage;
    }

    @FXML
    void onChargementDocumentationPressed(ActionEvent event) {
      if(this.mi != null) { // should happen
        this.mi.show();
      }
    }

}
