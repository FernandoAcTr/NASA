package controller.apod;

import DAO.APODDAO;
import DAO.APOD_User_DAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.stage.FileChooser;
import model.APODBean;
import model.MyImage;
import model.User;
import mysql.MySQL;
import services.RequestException;
import services.apod.APODService;
import utils.DownloadUtils;
import utils.FormatDate;
import utils.MyUtils;
import utils.PDFReport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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

    @FXML
    private VBox paneCalendar, paneReport;

    @FXML
    private JFXButton btnOk, btnCalendar, btnReport, btnGenerateReport;

    @FXML
    private JFXDatePicker datePicker, datePickerInit, datePickerEnd;

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
        paneCalendar.setVisible(false);
        paneReport.setVisible(false);

        hideUserComponets();

        MyUtils.setResizeListener(paneRoot);

        btnCalendar.setOnAction(event -> {
            paneCalendar.setVisible(!paneCalendar.isVisible());
            paneReport.setVisible(false);
        });

        btnReport.setOnAction(event -> {
            paneReport.setVisible(!paneReport.isVisible());
            paneCalendar.setVisible(false);
        });

        btnOk.setOnAction(event -> {
            if(datePicker.getValue() != null) {
                APODBean apod = apodDAO.getAPODByDate(datePicker.getValue().toString());
                if(apod != null){
                    apodBean = apod;
                    setValues();
                }else
                    MyUtils.makeDialog("Warning", null, "There isn't data of this day. Sorry :c", Alert.AlertType.WARNING).show();
            }
        });

        btnGenerateReport.setOnAction(event -> generateReport());
    }

    private void hideUserComponets(){
        if(user.getType().equalsIgnoreCase("user")){
            btnReport.setVisible(false);
        }
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

    private void generateReport(){
        String headers[] = {"User ID", "User", "APOD ID", "Title", "Date", "Hour"};
        String data[][];
        LocalDate initDate = datePickerInit.getValue();
        LocalDate endDate = datePickerEnd.getValue();

        if (initDate == null)
            if (endDate == null)
                data = apod_user_dao.fetchSearch(null, null);
            else
                data = apod_user_dao.fetchSearch(null, endDate.toString());
        else if (endDate == null)
            data = apod_user_dao.fetchSearch(initDate.toString(), null);
        else
            data = apod_user_dao.fetchSearch(initDate.toString(), endDate.toString());

        File file = getSaveFile();
        if(file != null){
            try {
                PDFReport report = new PDFReport(file.getPath(), "Searches Performed");
                report.addTable(headers, data);
                report.closeDocument();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private File getSaveFile(){
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("*.pdf");
        chooser.setTitle("Save as...");
        File file = chooser.showSaveDialog(btnReport.getScene().getWindow());
        return MyUtils.refactorFileName(file, "pdf");
    }
}
