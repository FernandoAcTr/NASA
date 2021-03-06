package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController implements Initializable {
    @FXML
    private ImageView imageViewCover;

    @FXML
    private Circle circleUser;

    @FXML
    private Label lblUserName;

    @FXML
    private JFXButton btnService1;

    @FXML
    private JFXButton btnService2;

    @FXML
    private JFXButton btnService3;

    @FXML
    private JFXButton btnLogOut;

    @FXML
    private JFXButton btnExit;

    private onItemClick itemClick;

    private String userName;
    private Image coverImage;
    private Image profileImage;

    public SideMenuController(onItemClick itemClick, String userName, Image coverImage, Image profileImage) {
        this.itemClick = itemClick;
        this.userName = userName;
        this.coverImage = coverImage;
        this.profileImage = profileImage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUserName.setText(userName);
        lblUserName.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLACK));

        if (coverImage != null)
            imageViewCover.setImage(coverImage);
        imageViewCover.setPreserveRatio(false);

        if (profileImage != null)
            circleUser.setFill(new ImagePattern(profileImage));
        circleUser.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLACK));


        btnService1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemClick.onServiceOneClick();
            }
        });

        btnService2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemClick.onServiceOneTwo();
            }
        });

        btnService3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemClick.onServiceOneThree();
            }
        });

        btnLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemClick.onLogOut();
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }

    public interface onItemClick {
        void onServiceOneClick();

        void onServiceOneTwo();

        void onServiceOneThree();

        void onLogOut();
    }
}
