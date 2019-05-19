package controller.techport;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;
import services.RequestException;
import services.techport.Project;
import services.techport.TechportService;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class TechportController implements Initializable {

    @FXML
    private VBox rootPane;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private ListView<Project> listViewProjects;

    @FXML
    private JFXButton btnPrevious;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXSpinner spnWait;

    @FXML
    private Label lblNumProjects;

    private ArrayList<Integer> listIDProjects;
    private ArrayList<Project> listProjects;
    private ThreadGroup searchGroup;
    private TechportService techportService;
    private String errorMessage;

    private final int itemsPerPage = 500;

    private int endIndex;
    private int startIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
        startSearchThread();
    }

    private void initData() {
        startIndex = 0;
        endIndex = itemsPerPage;
        searchGroup = new ThreadGroup("search");
        techportService = new TechportService();
        listProjects = new ArrayList<>();
    }

    private void initComponents() {
        MyUtils.setResizeListener(rootPane);

        lblNumProjects.setVisible(false);
        btnPrevious.setVisible(false);
        btnNext.setVisible(false);
        showWaitSpin();

        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                goNextPage();
                fillListView();
            }
        });

        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                goPreviousPage();
                fillListView();
            }
        });

        listViewProjects.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int id = listViewProjects.getSelectionModel().getSelectedItem().getId();
                    try {
                        Project project = techportService.getProject(id + "");
                        goProjectWindow(project);
                    } catch (RequestException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage();

                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = "An error had ocurred while trying get information. Please check your " +
                                "Internet Connection";
                        MyUtils.makeDialog("Error", null, errorMessage, Alert.AlertType.ERROR).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMessage = "An error had ocurred while trying get information. Sorry :c";
                        MyUtils.makeDialog("Error", null, errorMessage, Alert.AlertType.ERROR).show();
                    }
                }
            }
        });

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lookFor();
            }
        });
    }

    private void startSearchThread() {
        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                setListID();
                return null;
            }
        };

        searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (listIDProjects != null) {
                    sortIDs();
                    fillListProjects();
                    fillListView();
                    listIDProjects = null;
                } else
                    MyUtils.makeDialog("Error", null, errorMessage, Alert.AlertType.ERROR).show();

                hideWaitSpin();
            }
        });

        startTread(searchTask);
    }

    private void setListID() {
        try {
            String JSONResult = techportService.getListRequest();
            listIDProjects = techportService.getAllProjectsID(JSONResult);
        } catch (RequestException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "An error had ocurred while trying get information. Please check your " +
                    "Internet Connection";
        } catch (JSONException e) {
            e.printStackTrace();
            errorMessage = "An error had ocurred while trying get information. Sorry :c";
        }
    }

    /**
     * Crea una lista de Projects, para visualizar en el TextView
     */
    private void fillListProjects() {
        for (int id : listIDProjects) {
            Project p = new Project();
            p.setId(id);
            //buscar en la base de datos local por si existe su nombre y pasarselo.
            listProjects.add(p);
        }
    }

    /**
     * Start a thread
     *
     * @param task
     */
    private void startTread(Task task) {
        Thread thread = new Thread(searchGroup, task);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private void showWaitSpin() {
        spnWait.setVisible(true);
        listViewProjects.setVisible(false);
    }

    private void hideWaitSpin() {
        spnWait.setVisible(false);
        listViewProjects.setVisible(true);
    }

    private void showNavigationsButtons() {
        boolean previous = true;
        boolean next = true;

        if (startIndex == 0)
            previous = false;

        if (endIndex == listProjects.size() - 1)
            next = false;

        btnNext.setVisible(next);
        btnPrevious.setVisible(previous);
    }

    /**
     * Fill ListView with listProjects values
     */
    private void fillListView() {
        listViewProjects.getItems().clear();
        int aux = startIndex;

        while (aux < endIndex) {
            Project p = listProjects.get(aux);
            listViewProjects.getItems().add(p);
            aux++;
        }

        showNumProjects();
        showNavigationsButtons();
    }

    private void showNumProjects() {
        lblNumProjects.setVisible(true);
        lblNumProjects.setText(listViewProjects.getItems().size() + " shown of " + listProjects.size() + " index: " + startIndex);
    }

    /**
     * Avanza los indices el numero de posiciondes itemsPerPage hacia adelante
     */
    private void goNextPage() {
        startIndex = endIndex;
        int cont = 0;

        while (endIndex < listProjects.size() - 1 && cont < itemsPerPage) {
            endIndex++;
            cont++;
        }

    }

    /**
     * Retrasa los indices el numero de posiciondes itemsPerPage
     */
    private void goPreviousPage() {
        endIndex = startIndex;
        int cont = 0;

        while (startIndex > 0 && cont < itemsPerPage) {
            startIndex--;
            cont++;
        }

    }

    private void sortIDs() {
        Collections.sort(listIDProjects);
    }

    private void goProjectWindow(Project project) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/project_window.fxml"));
        Parent root = null;
        ProjectController controller = new ProjectController(project);
        try {
            loader.setController(controller);
            root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            MyUtils.undecorateWindow(stage, root, false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lookFor() {
        listViewProjects.getItems().clear();
        String lookfor = txtSearch.getText().trim();

        if (lookfor.length() == 0) {
            startIndex = 0;
            endIndex = itemsPerPage;
            fillListView();
        } else {
            for (Project p : listProjects) {
                if (String.valueOf(p.getId()).equalsIgnoreCase(lookfor))
                    listViewProjects.getItems().add(p);
                else if (p.getTitle() != null && p.getTitle().equalsIgnoreCase(lookfor))
                    listViewProjects.getItems().add(p);
            }
        }

    }

}
