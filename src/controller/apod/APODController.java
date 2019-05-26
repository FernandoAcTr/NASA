package controller.apod;

import DAO.APODDAO;
import DAO.APOD_User_DAO;
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
import javafx.scene.web.WebView;
import model.APODBean;
import model.MyImage;
import model.User;
import mysql.MySQL;
import services.apod.APODService;
import services.RequestException;
import utils.DownloadUtils;
import utils.FormatDate;
import utils.MyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class APODController implements Initializable {

    @FXML
    private VBox paneRoot;

    @FXML
    private ImageView imageAPOD;

    @FXML
    private WebView webViewAPOD;

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
    private APODDAO apodDAO;
    private APOD_User_DAO apod_user_dao;
    private User user;

    private File imageFile;
    private boolean existData;
    private String errorMessage;

    public APODController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();

        requestLocalAPOD();
        startTread(queryTask);
    }

    private void initData() {
        apodDAO = new APODDAO(MySQL.getConnection());
        apod_user_dao = new APOD_User_DAO(MySQL.getConnection());
        existData = false;
        apodBean = null;

        queryTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                startTread(downloadImageTask);
            }
        });

        downloadImageTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (apodBean != null) {
                    setValues(); //muestra los valores obtenidos
                    saveData(); //si no existian guarda un registro en la BD
                    spnWait.setVisible(false);
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

        if (isImage()) {
            imageAPOD.setVisible(true);

            if (apodBean.getImage().getImage() != null)
                imageAPOD.setImage(apodBean.getImage().getImage());
            else
                imageAPOD.setImage(new Image(apodBean.getUrl()));
        } else {
            webViewAPOD.setVisible(true);
            webViewAPOD.getEngine().load(apodBean.getUrl());
        }

    }

    private void requestRemoteAPOD() {
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
        Si aun no existe registro entonces si, dar un valor a existData dependiendo si
        encuentra o no registro
     */
    private void requestLocalAPOD() {
        apodBean = apodDAO.getAPODByDate(new FormatDate().toString());
        existData = apodBean != null;
    }

    /*
       Si no existe datos del dia actual entonces se guarda un registro
     */
    private void saveData() {
        if (!existData) {
            apodBean.setImage(new MyImage(imageFile));
            apodDAO.insertAPOD(apodBean);
        }

        int id = apodDAO.getNextID()-1;
        apod_user_dao.insertSearch(id, user.getId());
    }

    private boolean isImage() {
        String url = apodBean.getUrl().toLowerCase();
        return url.endsWith("jpg") || url.endsWith("jpeg") || url.endsWith("png");
    }

    private void downloadImage() {
        String extension = null;
        String url = apodBean.getUrl().toLowerCase();
        if (url.endsWith("jpg"))
            extension = "jpg";
        else if (url.endsWith("png"))
            extension = "png";
        else if (url.endsWith("jpeg"))
            extension = "jpeg";

        if (extension != null)
            imageFile = DownloadUtils.downloadFile(apodBean.getTitle(), "." + extension, apodBean.getUrl());
    }

    private Task<Void> queryTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            if (!existData)
                requestRemoteAPOD();
            return null;
        }
    };

    private Task<Void> downloadImageTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            if (!existData)
                downloadImage();
            return null;
        }
    };

    /**
     * Start a thread
     *
     * @param task
     */
    private void startTread(Task task) {
        Thread thread = new Thread(task);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }
}
