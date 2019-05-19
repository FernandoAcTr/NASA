package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnSingin;

    @FXML
    private JFXButton btnSingup;

    @FXML
    private Button btnClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
        txtUser.setText("User");
        txtPassword.setText("12345");
    }

    private void initComponents(){
        btnClose.setOnAction(event -> System.exit(0));

        btnSingin.setOnAction(event -> {
            String user = txtUser.getText();
            String pass = txtPassword.getText();
            if(user.equals("User") && pass.equals("12345"))
                goToMainWindow();
        });
    }

    private void goToMainWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/main_window.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        MyUtils.undecorateWindow(primaryStage, root, true);

        primaryStage.show();
        btnSingin.getScene().getWindow().hide();
    }
}
