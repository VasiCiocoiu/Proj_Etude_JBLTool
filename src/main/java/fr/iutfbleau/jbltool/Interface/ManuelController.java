package fr.iutfbleau.jbltool.Interface;

import java.util.Objects;
import fr.iutfbleau.jbltool.documentation.DocumentationParser;
import fr.iutfbleau.jbltool.documentation.InstructionDocumentation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ManuelController {

    @FXML
    private ListView<String> listInstructions;

    @FXML
    private Label instructionNameLabel;

    @FXML
    private Label instructionOpcodeLabel;

    @FXML
    private ListView<String> instructionParametersListView;

    @FXML
    private TextArea instructionDescriptionTextArea;

    @FXML
    private TextArea instructionExempleTextArea;

    @FXML
    private TextField searchField;


    private ObservableList<String> instructionsList;

    public ManuelController() {
    }

    /**
     * Set the current instruction selected in the interface.
     * @param ins
     * The instruction to select.
     */
    public void setCurrentInstructionManuel(InstructionDocumentation ins) {
        Objects.requireNonNull(ins);

        this.listInstructions.getSelectionModel().select(ins.getName());
        int index = this.listInstructions.getSelectionModel().getSelectedIndex();
        this.listInstructions.getFocusModel().focus(index);
        this.listInstructions.scrollTo(index);
    }

    /**
     * Initialize some internal things that couldn't be initialized in the constructor.
     */
    public void init() {
        this.instructionsList = FXCollections.observableArrayList();

        listInstructions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setInfosFromInstructions(newValue);
            }
        });

        FilteredList<String> filteredData = new FilteredList<>(instructionsList, s -> true);

        this.searchField.textProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
                if(((String)newValue).length() < ((String)oldValue).length() || newValue == null) {
                    listInstructions.setItems(instructionsList);
                }
                else {
                    newValue = ((String) newValue).toUpperCase();
                    for(String ins : listInstructions.getItems()) {
                        String filterText = ins;
                        if(filterText.toUpperCase().contains((String) newValue)) {
                            filteredList.add(ins);
                        }
                    }
                    listInstructions.setItems(filteredList);
                }
            }
        });
        
        listInstructions.setItems(instructionsList);
    }

    /**
     * Add an element in the list of instructions name. It will be possible to select the new instruction to get infos about it.
     * Note that the name should <b>ALWAYS</b> be in the {@link fr.iutfbleau.jbltool.documentation.DocumentationParser#instructionDocumentationHashMap}.
     * @param element
     */
    public void addInstructionNameInView(String element) {
        instructionsList.add(element);
    }

    private void setInfosFromInstructions(String instructionName) {
        InstructionDocumentation id = DocumentationParser.getInstruction(instructionName);
        if(id == null) {
            // TODO: maybe open a popup to indicate the error
            return ;
        }
        instructionNameLabel.setText(id.getName());
        instructionOpcodeLabel.setText(String.valueOf(id.getOpcode()));
        instructionDescriptionTextArea.setText(id.getDescription());
        instructionParametersListView.getItems().clear(); // remove parameters from previous selected instruction
        instructionParametersListView.getItems().addAll(FXCollections.observableArrayList(id.getParameters()));
        instructionExempleTextArea.setText(id.getExemple());
    }
}

