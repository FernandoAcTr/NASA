package controller;

import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.APODBean;
import services.APODService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class APODController implements Initializable {

    @FXML
    private VBox paneRoot;

    @FXML
    private ImageView imageAPOD;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtCredit;

    @FXML
    private TextArea txtExplanation;

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtMedia;

    @FXML
    private TextField txtVersion;

    @FXML
    private JFXSpinner spnWait;

    @FXML
    private GridPane paneData;

    private APODBean apodBean;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();

        queryTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                initValues();
                spnWait.setVisible(false);
                imageAPOD.setVisible(true);
                paneData.setVisible(true);
            }
        });

        Thread thread = new Thread(queryTask);
        thread.start();
    }

    private void initData(){
        apodBean = null;
    }

    private void initComponents(){
        paneData.setVisible(false);
    }

    private void requestAPOD(){
        try {
            apodBean = APODService.getAPOD();
        } catch (IOException e) {
            System.out.println("Error while tryind to get APOD");
            e.printStackTrace();
        }
    }

    private void setAPOD(){
        requestAPOD();
    }

    private void initValues(){
        if(apodBean != null){
            txtCredit.setText(apodBean.getCopyright());
            txtDate.setText(apodBean.getDate());
            txtMedia.setText(apodBean.getMedia_type());
            txtTitle.setText(apodBean.getTitle());
            txtVersion.setText(apodBean.getService_version());
            txtUrl.setText(apodBean.getUrl());
            txtExplanation.setText(apodBean.formatExplanation(apodBean.getExplanation()));
            imageAPOD.setImage(new Image(apodBean.getUrl()));
        }
    }

    private Task<Void> queryTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            setAPOD();
            return null;
        }
    };
}
