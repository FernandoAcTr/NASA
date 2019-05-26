package controller;

import DAO.UserDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.MyImage;
import model.User;
import mysql.MySQL;
import utils.MyUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SingUpController implements Initializable {

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConfPassword;

    @FXML
    private JFXButton btnProfilePhoto, btnAccept;

    @FXML
    private JFXButton btnCoverPhoto;

    @FXML
    private ImageView imageViewCover;

    @FXML
    private Circle circleImageProfile;

    @FXML
    private Button btnClose;

    private FileChooser fileChooser;
    private File imageCoverFile;
    private File imageProfileFile;
    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
    }

    private void initData() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Formats", "*.png", "*.jpeg", "*.jpg"));
        userDAO = new UserDAO(MySQL.getConnection());

    }

    private void initComponents() {
        imageViewCover.setPreserveRatio(false);
        imageCoverFile = new File("src/resources/images/cover_default.jpg");
        imageViewCover.setImage(new Image(imageCoverFile.toURI().toString()));

        circleImageProfile.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLACK));
        imageProfileFile = new File("src/resources/images/user_default.png");
        circleImageProfile.setFill(new ImagePattern(new Image(imageProfileFile.toURI().toString())));

        btnClose.setOnAction(event -> btnClose.getScene().getWindow().hide());
        btnCoverPhoto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageCoverFile = getOpenFileChooser();
                if (imageCoverFile != null)
                    imageViewCover.setImage(new Image(imageCoverFile.toURI().toString()));

            }
        });

        btnProfilePhoto.setOnAction(event -> {
            imageProfileFile = getOpenFileChooser();
            if (imageProfileFile != null)
                circleImageProfile.setFill(new ImagePattern(new Image(imageProfileFile.toURI().toString())));
        });

        btnAccept.setOnAction(event -> saveUser());

    }

    private File getOpenFileChooser() {
        Window ownerWindow = btnClose.getScene().getWindow();
        return fileChooser.showOpenDialog(ownerWindow);
    }

    private boolean checkPassword(){
        return txtPassword.getText().equals(txtConfPassword.getText());
    }

    private void saveUser(){
        String username = txtUser.getText();
        String password = txtPassword.getText();
        if(checkPassword()){
            if(!userDAO.existUser(username)){
                User user = new User();
                user.setUsername(username);
                user.setPassword(userDAO.encryptPassword(password));
                user.setImageProfile(new MyImage(imageProfileFile));
                user.setImageCover(new MyImage(imageCoverFile));
                user.setType("user");
                if(userDAO.insertUser(user)) {
                    MyUtils.makeDialog("Succeful", null, username + " has been registered correctly", Alert.AlertType.INFORMATION).show();
                    btnAccept.getScene().getWindow().hide();
                }
                else
                    MyUtils.makeDialog("Error", null, "An error has occurred. Please try again", Alert.AlertType.ERROR).show();
            }else
                MyUtils.makeDialog("Error", null, "This username already exist", Alert.AlertType.WARNING).show();
        }else
            MyUtils.makeDialog("Error", null, "Both password aren't equals", Alert.AlertType.WARNING).show();
    }
}
