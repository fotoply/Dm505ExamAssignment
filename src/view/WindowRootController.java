package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class WindowRootController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> componentTypeBox;

    @FXML
    private TableColumn<?, String> componentNameColumn;

    @FXML
    private TableColumn<?, Integer> componentAmontColumn;

    @FXML
    private TableColumn<?, Integer> componentFinalPriceColumn;

    @FXML
    private TableColumn<?, Integer> componentPriceColumn;

    @FXML
    private TableColumn<?, String > systemsNameColumn;

    @FXML
    private TableColumn<?, Integer> computerBuildableColumn;

    @FXML
    private TableColumn<?, Integer> computerFinalPriceColumn;

    @FXML
    private TableColumn<?, Integer> computerPriceColumn;

    @FXML
    private TableView<?> RestockingTable;

    @FXML
    private TableColumn<?, ?> restockingNameColumn;

    @FXML
    private TableColumn<?, ?> restockingAmountColumn;

    @FXML
    void componentTypeChanged(ActionEvent event) {

    }

    @FXML
    void newTabSelected(ActionEvent event) {

    }

    @FXML
    void initialize() {
        componentTypeBox.getItems().add("All");
        componentTypeBox.getItems().add("CPU");
        componentTypeBox.getItems().add("Graphics card");
        componentTypeBox.getItems().add("Mainboard");
        componentTypeBox.getItems().add("Case");
        componentTypeBox.getItems().add("RAM");

    }
}