package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class APODController implements Initializable {

    @FXML
    private VBox paneRoot;

    @FXML
    private ImageView imageAPOD;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblCredit;

    @FXML
    private Label lblExplanation;

    @FXML
    private Label lblUrl;

    @FXML
    private Label lblMedia;

    @FXML
    private Label lblVersion;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
