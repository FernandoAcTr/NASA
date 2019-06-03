package controller;

import DAO.UserDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.User;
import mysql.MySQL;
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

    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
        userDAO = new UserDAO(MySQL.getConnection());
    }

    private void initComponents(){
        btnClose.setOnAction(event -> System.exit(0));

        btnSingin.setOnAction(event -> {
            User user = verifyCredentials();
            if(user != null)
                goToMainWindow(user);
            else
                MyUtils.makeDialog("Error", null, "Username or password are not correct. Please check your credentials", Alert.AlertType.WARNING).show();
        });

        btnSingup.setOnAction(event -> gotoSingUpWindoiw());
    }

    private void goToMainWindow(User user){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/main_window.fxml"));
        Parent root = null;
        try {
            MainController controller = new MainController(user);
            loader.setController(controller);
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

    private void gotoSingUpWindoiw(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/singup_window.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);

        MyUtils.undecorateWindow(primaryStage, root, false);

        primaryStage.show();
    }

    private User verifyCredentials(){
        String username = txtUser.getText();
        String password = txtPassword.getText();
        return userDAO.validUser(username, password);
    }
}
