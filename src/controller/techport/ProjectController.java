package controller.techport;

import com.jfoenix.controls.JFXSpinner;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import services.techport.*;
import utils.DownloadUtils;
import utils.MyUtils;
import utils.PDFReport;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextArea txtBenefits;

    @FXML
    private TextField txtAcronym;

    @FXML
    private TextField txtlastUpdate;

    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtResponsive;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txtMissionType;

    @FXML
    private TextField txtTechStart;

    @FXML
    private TextField txtTechCurrent;

    @FXML
    private TextField txtWebsite;

    @FXML
    private VBox paneContent;

    @FXML
    private HBox paneImage;

    @FXML
    private Button btnClose, btnNext, btnPrevious;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lblImageFooter;

    @FXML
    private JFXSpinner spnWait;

    @FXML
    private Button btnReport;

    private Project project;
    private ArrayList<LibraryFile> listImages;
    private ArrayList<LibraryFile> listDocs;
    private int currentImage = 0;

    public ProjectController(Project project) {
        this.project = project;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        startDownloadThread();
        initComponents();
        showValues();
    }

    private void initData() {
        listImages = new ArrayList<>();
        listDocs = new ArrayList<>();
    }

    private void initComponents() {
        btnNext.setVisible(false);
        btnPrevious.setVisible(false);
        lblImageFooter.setVisible(false);

        btnClose.setOnAction(e -> {
            btnClose.getScene().getWindow().hide();
        });

        txtWebsite.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!txtWebsite.getText().isEmpty()) {
                    try {
                        Runtime.getRuntime().exec(new String[]{"google-chrome", project.getWebsite()});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        txtWebsite.setCursor(Cursor.HAND);

        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showNextImage();
            }
        });

        btnPrevious.setOnAction(event -> showPreviousImage());

        btnReport.setOnAction(event -> {
            File saveFile = getSaveFile();
            if(saveFile != null)
                createPDF(saveFile);
        });
    }

    private void startDownloadThread() {
        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                downloadFiles();
                return null;
            }
        };

        downloadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (listImages.size() > 0) {
                    lblImageFooter.setVisible(true);
                    setImage(listImages.get(currentImage).getLocalFile());
                }

                if (listImages.size() > 1) {
                    btnNext.setVisible(true);
                    btnPrevious.setVisible(true);
                }

                if (listDocs.size() > 0) {
                    List<String> val = new ArrayList<>();
                    int row = 0;
                    String headers[] = {"Title", "Size"};
                    String values[][] = new String[listDocs.size()][2];

                    for (LibraryFile f : listDocs) {
                        values[row][0] = f.getTitle();
                        values[row][1] = f.getSize() / 1000 + " kb";
                        row++;
                    }

                    VBox vb = createTableView("Documents", headers, values);
                    TableView table = (TableView) vb.getChildren().get(1);
                    table.setOnMouseClicked(event1 -> {
                        if (event1.getClickCount() == 2) {
                            openDocument(listDocs.get(table.getSelectionModel().getSelectedIndex()));
                        }
                    });
                    paneContent.getChildren().add(vb);
                    paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
                }
                spnWait.setVisible(false);
                paneImage.setVisible(true);
            }
        });

        Thread thread = new Thread(downloadTask);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private void setImage(File local) {
        imageView.setImage(new Image(local.toURI().toString()));
        lblImageFooter.setText(listImages.get(currentImage).getTitle());
    }

    private void showValues() {
        txtTitle.setText(project.getTitle());
        txtID.setText(project.getId() + "");
        txtAcronym.setText(project.getAcronym());
        if (project.getBenefits() != null)
            txtBenefits.setText(MyUtils.formatText(project.getBenefits().replaceAll("<p>", "").replaceAll("</p>", ""), 100));
        if (project.getDescription() != null)
            txtDescription.setText(MyUtils.formatText(project.getDescription().replaceAll("<p>", "").replaceAll("</p>", ""), 100));
        txtEndDate.setText(project.getEndDate());
        txtStartDate.setText(project.getStartDate());
        txtlastUpdate.setText(project.getLastUpdated());
        txtMissionType.setText(project.getSupportedMissionType());
        txtResponsive.setText(project.getResponsibleProgram());
        txtStatus.setText(project.getStatus());
        txtTechCurrent.setText(project.getTechnologyMaturityCurrent());
        txtTechStart.setText(project.getTechnologyMaturityStart());
        txtWebsite.setText(project.getWebsite());

        if (project.getProgramDirectors() != null) {
            VBox vb = createListView("Program Directors", project.getProgramDirectors());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getProjectManagers() != null) {
            VBox vb = createListView("Project Managers", project.getProjectManagers());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getPrincipalInvestigators() != null) {
            VBox vb = createListView("Principal Investigators", project.getPrincipalInvestigators());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getCoInvestigators() != null) {
            VBox vb = createListView("CoInvestigators", project.getCoInvestigators());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getDestinations() != null) {
            VBox vb = createListView("Destinations", project.getDestinations());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getWorkLocations() != null) {
            VBox vb = createListView("Work Locations", project.getWorkLocations());
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
        }

        if (project.getLeadOrganization() != null) {
            List<String> val = new ArrayList<>();
            Organization or = project.getLeadOrganization();
            val.add("Name: " + or.getName());
            val.add("Acronym: " + or.getAcronym());
            val.add("City: " + or.getCity());
            val.add("State: " + or.getState());
            val.add("Type: " + or.getType());

            VBox vb = createListView("Leader Organization", val);
            paneContent.getChildren().add(vb);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());

        }

        if (project.getSupportingOrganizations() != null) {
            List<String> val = new ArrayList<>();
            int count = 0;
            for (Organization or : project.getSupportingOrganizations()) {
                val.clear();
                val.add("Name: " + or.getName());
                val.add("Acronym: " + or.getAcronym());
                val.add("City: " + or.getCity());
                val.add("State: " + or.getState());
                val.add("Type: " + or.getType());

                VBox vb = count < 1 ? createListView("Supporting Organizations", val) : createListView("", val);
                paneContent.getChildren().add(vb);
                paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
                count++;
            }
        }

        if (project.getCoFundingPartners() != null) {
            List<String> val = new ArrayList<>();
            int count = 0;
            for (Organization or : project.getCoFundingPartners()) {
                val.clear();
                val.add("Name: " + or.getName());
                val.add("Acronym: " + or.getAcronym());
                val.add("City: " + or.getCity());
                val.add("State: " + or.getState());
                val.add("Type: " + or.getType());

                VBox vb = count < 1 ? createListView("Cofunding Partners", val) : createListView("", val);
                paneContent.getChildren().add(vb);
                paneContent.setPrefHeight(paneContent.getPrefHeight() + vb.getPrefHeight());
                count++;
            }
        }

        if (project.getPrimaryTas() != null) {
            List<String> val = new ArrayList<>();
            Label lbl = getLabel("Impact Area's");
            lbl.getStyleClass().add("lbl-info");
            paneContent.getChildren().addAll(lbl);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + 30);
            for (ImpactArea im : project.getPrimaryTas()) {
                lbl = getLabel("- " + im.getName());
                paneContent.getChildren().addAll(lbl);
                paneContent.setPrefHeight(paneContent.getPrefHeight() + 30);
            }
        }

        if (project.getAdditionalTas() != null) {
            List<String> val = new ArrayList<>();
            Label lbl = getLabel("Additional Area's");
            lbl.getStyleClass().add("lbl-info");
            paneContent.getChildren().addAll(lbl);
            paneContent.setPrefHeight(paneContent.getPrefHeight() + 30);
            for (ImpactArea im : project.getAdditionalTas()) {
                lbl = getLabel("- " + im.getName());
                paneContent.getChildren().addAll(lbl);
                paneContent.setPrefHeight(paneContent.getPrefHeight() + 30);
            }
        }


    }

    private void downloadFiles() {
        for (LibraryItem item : project.getLibraryItems())
            if (item.getType().equalsIgnoreCase("image")) {
                if (item.getFiles() != null) {
                    LibraryFile libraryFile = item.getFiles().get(0);
                    File dowload = null;
                    if (libraryFile.getUrl() != null)
                        dowload = DownloadUtils.downloadFile(libraryFile.getId() + "", ".jpg", libraryFile.getUrl());
                    if (dowload != null) {
                        libraryFile.setLocalFile(dowload);
                        libraryFile.setTitle(item.getTitle());
                        listImages.add(libraryFile);
                    }
                }
            } else if (item.getType().equalsIgnoreCase("Story")) {
                if (item.getFiles() != null) {
                    LibraryFile libraryFile = item.getFiles().get(0);
                    File dowload = null;
                    if (libraryFile.getUrl() != null)
                        dowload = DownloadUtils.downloadFile(libraryFile.getId() + "", ".pdf", libraryFile.getUrl());
                    if (dowload != null) {
                        libraryFile.setLocalFile(dowload);
                        libraryFile.setTitle(item.getTitle());
                        listDocs.add(libraryFile);
                    }
                }
            } else if (item.getType().equalsIgnoreCase("Link")) {
                LibraryFile libraryFile = new LibraryFile();
                libraryFile.setId(item.getId());
                libraryFile.setUrl(item.getExternalUrl());
                libraryFile.setSize(0);
                File dowload = null;
                if (libraryFile.getUrl() != null)
                    dowload = DownloadUtils.downloadFile(libraryFile.getId() + "", ".pdf", libraryFile.getUrl());
                if (dowload != null) {
                    libraryFile.setLocalFile(dowload);
                    libraryFile.setTitle(item.getTitle());
                    listDocs.add(libraryFile);
                }
            }

        if (project.getCloseoutDocuments() != null)
            for (int i = 0; i < project.getCloseoutDocuments().size(); i++) {
                LibraryFile libraryFile = new LibraryFile();
                libraryFile.setId(88556985);
                libraryFile.setTitle("Close Out Document " + i);
                libraryFile.setUrl(project.getCloseoutDocuments().get(i));
                libraryFile.setSize(0);
                File dowload = DownloadUtils.downloadFile(libraryFile.getId() + "", ".pdf", libraryFile.getUrl());
                if (dowload != null) {
                    libraryFile.setLocalFile(dowload);
                    listDocs.add(libraryFile);
                }
            }

    }

    private void openDocument(LibraryFile file) {
        try {
            Runtime.getRuntime().exec(new String[]{"google-chrome", file.getLocalFile().getAbsolutePath()});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNextImage() {
        if (currentImage < listImages.size() - 1) {
            currentImage++;
        } else
            currentImage = 0;

        setImage(listImages.get(currentImage).getLocalFile());

    }

    private void showPreviousImage() {
        if (currentImage > 0) {
            currentImage--;
        } else
            currentImage = listImages.size() - 1;

        setImage(listImages.get(currentImage).getLocalFile());
    }

    private VBox createListView(String title, List<String> items) {
        Label lblTitle = new Label(title);
        lblTitle.setPrefHeight(30);
        lblTitle.getStyleClass().addAll("lbl", "lbl-info", ".lbl-h2");

        ListView<String> listView = new ListView<>();
        for (String it : items)
            listView.getItems().add(it);

        listView.setPrefHeight(items.size() * 25);

        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().addAll(lblTitle, listView);
        pane.setPrefHeight(listView.getPrefHeight() + lblTitle.getPrefHeight());

        return pane;
    }

    private VBox createTableView(String title, String headers[], String[][] values) {
        TableView<List<String>> tableView = new TableView<>();
        TableColumn<List<String>, String> column;
        for (int i = 0; i < headers.length; i++) {
            column = new TableColumn<>(headers[i]);
            int finalI = i;
            column.setCellValueFactory(param ->
                    new SimpleStringProperty(param.getValue().get(finalI))
            );
            column.setSortable(false);
            column.setPrefWidth(350);
            column.setResizable(false);
            tableView.getColumns().add(column);
        }

        tableView.setFocusTraversable(false);

        //add Values
        ArrayList<String> rowVal;
        for (int row = 0; row < values.length; row++) {
            rowVal = new ArrayList<>();
            for (int col = 0; col < headers.length; col++) {
                rowVal.add(values[row][col]);
            }
            tableView.getItems().add(rowVal);
        }

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().addAll("lbl", "lbl-info", ".lbl-h2");
        lblTitle.setPrefHeight(30);

        tableView.setPrefHeight(values.length * 30 + 30);

        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().addAll(lblTitle, tableView);
        pane.setPrefHeight(tableView.getPrefHeight() + lblTitle.getPrefHeight());
        return pane;
    }

    private Label getLabel(String content) {
        Label lblTitle = new Label(content);
        lblTitle.getStyleClass().addAll("lbl");
        lblTitle.setPrefHeight(30);
        return lblTitle;
    }

    private void createPDF(File file){
        PDFReport report = null;
        try {
            report = new PDFReport(file.getPath(), project.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }

        report.addParagraph("ID: " + project.getId());
        report.addParagraph("Acronym: " + project.getAcronym());
        if(project.getStatus() != null)
            report.addParagraph("Status: " + project.getStatus());
        if (project.getDescription() != null)
            report.addParagraph("Description: " + project.getDescription().replaceAll("<p>", "").replaceAll("</p>", ""));
        if (project.getBenefits() != null)
            report.addParagraph("Benefits: " + project.getBenefits().replaceAll("<p>", "").replaceAll("</p>", ""));

        if(project.getStartDate() != null) report.addParagraph("Start Date: " + project.getStartDate());
        if(project.getEndDate() != null) report.addParagraph("End Date: " + project.getEndDate());
        if(project.getLastUpdated() != null) report.addParagraph("Last Update: " + project.getLastUpdated());
        if(project.getSupportedMissionType() != null) report.addParagraph("Support Mission Type: " + project.getSupportedMissionType());
        if(project.getResponsibleProgram() != null) report.addParagraph("Responsible Program: " + project.getResponsibleProgram());
        if(project.getTechnologyMaturityStart() != null) report.addParagraph("Technology Maturity Start: " + project.getTechnologyMaturityStart());
        if(project.getTechnologyMaturityCurrent() != null) report.addParagraph("Technology Maturity Current: " + project.getTechnologyMaturityCurrent());
        if(project.getTechnologyMaturityEnd() != null) report.addParagraph("Technology Maturity End: " + project.getTechnologyMaturityEnd());
        if(project.getWebsite() != null) report.addParagraph("Website: " + project.getWebsite());

        if (project.getDestinations() != null){
            String mess = "Destinations: ";
            for (String s : project.getDestinations())
                mess += s + ", ";
            report.addParagraph(mess.substring(0, mess.length()-2));
        }

        if (project.getWorkLocations() != null){
            String mess = "Work Locations: ";
            for (String s : project.getWorkLocations())
                mess += s + ", ";
            report.addParagraph(mess.substring(0, mess.length()-2));
        }

        if (project.getProgramDirectors() != null){
            report.addSubtitle("Program Directors:");
            for (String s : project.getProgramDirectors())
                report.addParagraph("- " + s);
        }

        if (project.getProjectManagers() != null){
            report.addSubtitle("Project Managers:");
            for (String s : project.getProjectManagers())
                report.addParagraph("- " + s);
        }

        if (project.getPrincipalInvestigators() != null){
            report.addSubtitle("Principal Investigators:");
            for (String s : project.getPrincipalInvestigators())
                report.addParagraph("- " + s);
        }

        if (project.getCoInvestigators() != null){
            report.addSubtitle("Co-Investigators:");
            for (String s : project.getCoInvestigators())
                report.addParagraph("- " + s);
        }

        if(project.getLeadOrganization() != null){
            report.addSubtitle("Leader Organization:");
            Organization or = project.getLeadOrganization();
            report.addParagraph("Name: " + or.getName());
            report.addParagraph("Acronym: " + or.getAcronym());
            report.addParagraph("City: " + or.getCity());
            report.addParagraph("State: " + or.getState());
            report.addParagraph("Type: " + or.getType());
        }

        if(project.getSupportingOrganizations() != null){
            report.addSubtitle("Supporting Organizations:");
            String headers[] = {"Name", "Acronym", "City", "State", "Type"};
            String values[][] = new String[project.getSupportingOrganizations().size()][5];

            for (int row = 0; row < project.getSupportingOrganizations().size(); row++) {
                Organization or = project.getSupportingOrganizations().get(row);
                values[row][0] = or.getName() != null ? or.getName() : "null";
                values[row][1] = or.getAcronym() != null ? or.getAcronym() : "null";
                values[row][2] = or.getCity() != null ? or.getCity() : "null";
                values[row][3] = or.getState() != null ? or.getState() : "null";
                values[row][4] = or.getType() != null ? or.getType() : "null";
            }

            try {
                report.addTable(headers, values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(project.getCoFundingPartners() != null){
            report.addSubtitle("Co-funding Partners:");
            String headers[] = {"Name", "Acronym", "City", "State", "Type"};
            String values[][] = new String[project.getCoFundingPartners().size()][5];

            for (int row = 0; row < project.getCoFundingPartners().size(); row++) {
                Organization or = project.getCoFundingPartners().get(row);
                values[row][0] = or.getName() != null ? or.getName() : "null";
                values[row][1] = or.getAcronym() != null ? or.getAcronym() : "null";
                values[row][2] = or.getCity() != null ? or.getCity() : "null";
                values[row][3] = or.getState() != null ? or.getState() : "null";
                values[row][4] = or.getType() != null ? or.getType() : "null";
            }

            try {
                report.addTable(headers, values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(project.getPrimaryTas() != null){
            report.addSubtitle("Principal Impact Area's");
            String mess = "";
            for (ImpactArea a : project.getPrimaryTas())
                mess += a.getName() + ", ";

            report.addParagraph(mess.substring(0, mess.length()-2));
        }

        if(project.getAdditionalTas() != null){
            report.addSubtitle("Additional Impact Area's");
            String mess = "";
            for (ImpactArea a : project.getAdditionalTas())
                mess += a.getName() + ", ";

            report.addParagraph(mess.substring(0, mess.length()-2));
        }

        if(listImages.size() > 0){
            report.addSubtitle("Images");
            for (LibraryFile it : listImages){
                try {
                    report.addImage(it.getLocalFile().getAbsolutePath(), it.getTitle());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        report.closeDocument();
    }

    private File getSaveFile(){
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("*.pdf");
        chooser.setTitle("Save as...");
        File file = chooser.showSaveDialog(btnReport.getScene().getWindow());
        return MyUtils.refactorFileName(file, "pdf");
    }

}
