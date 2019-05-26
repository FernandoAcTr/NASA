package controller.apod;

import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.apod.APODService;
import services.RequestException;
import utils.MyUtils;

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
    private boolean existData;
    private String errorMessage;

    public APODController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();

        Thread thread = new Thread(queryTask);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private void initData() {
        existData = false;
        apodBean = null;

        queryTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (apodBean != null) {
                    setValues(); //muestra los valores obtenidos
                    saveData(); //si no existian guarda un registro en la BD
                    spnWait.setVisible(false);
                    imageAPOD.setVisible(true);
                    paneData.setVisible(true);
                } else {
                    spnWait.setVisible(false);
                    MyUtils.makeDialog("Error", null, errorMessage, Alert.AlertType.ERROR).show();
                }

            }
        });

    }

    private void initComponents() {
        paneData.setVisible(false);

       MyUtils.setResizeListener(paneRoot);
    }

    private void setValues() {
        txtCredit.setText(apodBean.getCopyright());
        txtDate.setText(apodBean.getDate());
        txtMedia.setText(apodBean.getMedia_type());
        txtTitle.setText(apodBean.getTitle());
        txtVersion.setText(apodBean.getService_version());
        txtUrl.setText(apodBean.getUrl());
        txtExplanation.setText(MyUtils.formatText(apodBean.getExplanation(), 150));
        imageAPOD.setImage(new Image(apodBean.getUrl()));
    }

    private void requestAPOD() {
        try {
            apodBean = APODService.getAPOD();
        } catch (IOException e) {
            errorMessage = "An error had ocurred while trying get APOD. Please check your " +
                            "Internet Connection";
        } catch (RequestException e) {
            errorMessage = e.getMessage();
        }
    }

    /*
        Este metodo debe buscar primero una copia de los datos en la base de datos local con la fecha actual
        Si aun no existe registro entonces si, hacer la peticion de APOD y dar un valor a existData dependiendo si
        encuenttra o no registro
     */
    private void setAPOD() {
        requestAPOD();
    }

    /*
       Si no existe datos del dia actual entonces se guarda un registro
     */
    private void saveData() {
        if (!existData) {

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
