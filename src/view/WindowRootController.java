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
    private ComboBox<?> componentTypeBox;

    @FXML
    private TableColumn<?, ?> componentNameColumn;

    @FXML
    private TableColumn<?, ?> componentAmontColumn;

    @FXML
    private TableColumn<?, ?> componentFinalPriceColumn;

    @FXML
    private TableColumn<?, ?> componentPriceColumn;

    @FXML
    private TableColumn<?, ?> systemsNameColumn;

    @FXML
    private TableColumn<?, ?> computerBuildableColumn;

    @FXML
    private TableColumn<?, ?> computerFinalPriceColumn;

    @FXML
    private TableColumn<?, ?> computerPriceColumn;

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
    }
}