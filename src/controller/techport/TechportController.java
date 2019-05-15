package controller.techport;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import utils.MyUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class TechportController implements Initializable {

    @FXML
    private VBox rootPane;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private ListView<String> listViewProjects;

    @FXML
    private JFXButton btnPrevious;

    @FXML
    private JFXButton btnNext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
        listViewProjects.getItems().add("Item 1");
    }

    private void initComponents(){
        MyUtils.setResizeListener(rootPane);
        listViewProjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
