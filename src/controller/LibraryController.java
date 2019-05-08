package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXCheckBox checkImages;

    @FXML
    private JFXCheckBox checkVideos;

    @FXML
    private JFXCheckBox checkAudio;

    @FXML
    private TilePane paneMedia;

    @FXML
    private Label lblNewer;

    @FXML
    private Label lblPopular;

    public LibraryController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
    }

    private void initComponents(){
        selectItem(lblNewer);
        lblNewer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectItem(lblNewer);
                unselectItem(lblPopular);
            }
        });

        lblPopular.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectItem(lblPopular);
                unselectItem(lblNewer);
            }
        });
    }

    private void selectItem(Label label){
        label.setStyle("-fx-background-color: #000000;-fx-underline: false");
    }

    private void unselectItem(Label label){
        label.setStyle("-fx-background-color: transparent;-fx-underline: true");
    }
}
