package controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import controller.apod.APODController;
import controller.library.LibraryController;
import controller.techport.TechportController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, SideMenuController.onItemClick {

    @FXML
    private JFXHamburger hamburguer;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane paneContent;

    private User user;

    public MainController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
    }

    private void initComponents() {
        initSideMenu();
        initHamburguer();
    }

    /*----------------------------------------------------------------------------------------------
                                        Init Methods
     ----------------------------------------------------------------------------------------------*/
    private void initSideMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/side_bar.fxml"));
        try {
            SideMenuController controller = new SideMenuController(this, user.getUsername(),
                    user.getImageCover().getImage(), user.getImageProfile().getImage());
            loader.setController(controller);
            Parent sideMenu = loader.load();
            drawer.setSidePane(sideMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHamburguer() {
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburguer);
        transition.setRate(-1);
        hamburguer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transition.setRate(transition.getRate() * -1);
                transition.play();

                if (drawer.isClosed())
                    drawer.open();
                else
                    drawer.close();
            }
        });
    }

    /*----------------------------------------------------------------------------------------------
                                        SideMenuController Methods
     ----------------------------------------------------------------------------------------------*/
    @Override
    public void onServiceOneClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/apod_window.fxml"));

        try {
            APODController controller = new APODController();
            loader.setController(controller);

            ScrollPane root = loader.load();
            VBox vBox = (VBox) root.getContent();

            attachToRoot(root);
            resizeRoot(vBox);

            paneContent.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceOneTwo() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/library_window.fxml"));
        try {
            LibraryController controller = new LibraryController();
            loader.setController(controller);

            ScrollPane root = loader.load();
            VBox vBox = (VBox) root.getContent();

            attachToRoot(root);
            resizeRoot(vBox);

            paneContent.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceOneThree() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/techport_window.fxml"));
        try {
            TechportController controller = new TechportController();
            loader.setController(controller);

            Parent root = loader.load();

            attachToRoot(root);

            paneContent.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogOut() {
        paneContent.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/login_window.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(false);

            MyUtils.undecorateWindow(primaryStage, root, false);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attachToRoot(Parent root) {
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
    }

    /**
     * Resize a pane inside a ScrollPane, becouse Node's inside ScrollPane does not fill all the area automatically
     *
     * @param vBox
     */
    private void resizeRoot(VBox vBox) {
        double heigth = paneContent.getScene().getHeight();
        double width = paneContent.getScene().getWidth();
        vBox.setPrefHeight(heigth);
        vBox.setPrefWidth(width);
        vBox.setFocusTraversable(false);
    }

    /**-----------------------------------------------------------------------------------------------**/
}
