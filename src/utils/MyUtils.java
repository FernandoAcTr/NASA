package utils;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.GregorianCalendar;

public class MyUtils {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void undecorateWindow(final Stage stage, Parent root, boolean enableFullScreen){
        stage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });


        if(enableFullScreen) {
            stage.setFullScreen(enableFullScreen);
            root.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2)
                        stage.setFullScreen(!stage.isFullScreen());
                }
            });
        }
    }

    public static Alert makeDialog(String title, String header, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Agrega saltos de linea a una cadena muy larga
     * @return
     */
    public static String formatText(String text, int maxLength){
        String formatExplain = "";
        char character;

        for (int i = 0, limit = 0; i < text.length(); i++, limit++) {

            character = text.charAt(i);
            formatExplain += character;

            if(limit > maxLength)
                if(character == ' '){
                    formatExplain += "\n";
                    limit = 0;
                }
        }

        return formatExplain;
    }

    /**
     * get current year
     * @return
     */
    public static int getCurrentYear(){
        return GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
    }

    /**
     * Add Listener to root pane to resize at screen heigth at double click on it
     * @param root
     */
    public static void setResizeListener(Pane root){
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    double screenHeigth = Screen.getPrimary().getBounds().getHeight();
                    double screenWidth = Screen.getPrimary().getBounds().getWidth();
                    root.setPrefHeight(screenHeigth);
                    root.setPrefWidth(screenWidth);
                }
            }
        });
    }

}
