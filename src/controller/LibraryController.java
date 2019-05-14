package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.MyUtils;
import services.library.Collection;
import services.library.Item;
import services.library.LibraryCollection;
import services.library.LibraryService;
import services.RequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXCheckBox checkImages;

    @FXML
    private JFXCheckBox checkVideos;

    @FXML
    private JFXCheckBox checkAudio;

    @FXML
    private TilePane paneMedia;

    @FXML
    private JFXSlider slideFrom, slideTo;

    @FXML
    private JFXSpinner spnWait;

    private LibraryCollection libraryCollection;
    private LibraryService libraryService;
    private String errorMessage;

    private ThreadGroup searchGroup;

    public LibraryController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
    }

    private void initData() {
        searchGroup = new ThreadGroup("search");
        libraryService = new LibraryService();
    }

    private void initComponents() {

        slideTo.setValue(MyUtils.getCurrentYear());
        spnWait.setVisible(false);

        paneMedia.setPrefHeight(Screen.getPrimary().getBounds().getHeight());

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                spnWait.setVisible(true);
                paneMedia.setVisible(false);

                if (slideFrom.getValue() > slideTo.getValue())
                    MyUtils.makeDialog("Warning", null, "The start year is higer than end year",
                            Alert.AlertType.WARNING).show();
                else
                    startSearchThread();
            }
        });
    }

    private void startSearchThread() {
        killThreads();

        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                searchTask();
                return null;
            }
        };

        searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (libraryCollection != null) {
                    Collection collection = libraryCollection.getCollection();
                    showItems(collection);
                    spnWait.setVisible(false);
                    paneMedia.setVisible(true);
                } else
                    MyUtils.makeDialog("Error", null,
                            errorMessage, Alert.AlertType.ERROR).show();
            }
        });

        Thread thread = new Thread(searchGroup, searchTask);
        thread.start();

    }

    private void searchTask() {
        String search = txtSearch.getText().trim();
        int from = (int) slideFrom.getValue();
        int to = (int) slideTo.getValue();
        try {

            String url = libraryService.getURL(search, "1", checkImages.isSelected(), checkVideos.isSelected(),
                    checkAudio.isSelected(), from + "", to + "");
            String JSONResult = libraryService.getRequest(url);
            libraryCollection = libraryService.getLibraryCollection(JSONResult);

        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "An error had ocurred while trying get information. Please check your " +
                    "Internet Connection";
        } catch (RequestException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------
     * Procesemiento de los Items Recibidos.
     * -------------------------------------------------------------------------------------------------
     */

    private void showItems(Collection collection) {
        double width = txtSearch.getScene().getWidth();
        paneMedia.setPrefWidth(width);
        paneMedia.getChildren().clear();

        for (int i = 0; i < collection.getItems().size(); i++) {
            Item item = collection.getItems().get(i);
            if (item.getData().get(0).getMedia_type().equals("image"))
                proccesImage(item);
            else if (item.getData().get(0).getMedia_type().equals("video"))
                proccesVideo(item);
            else
                proccesAudio(item);
        }
    }

    private void proccesImage(Item item) {
        ImageView[] imageView = new ImageView[1];

        Task<Void> proccesTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LibraryCollection.Image image = new LibraryCollection.Image(item.getHref());
                String url = image.getLinkImage();
                imageView[0] = getImageView(url, item);

                if (imageView[0] != null) {
                    imageView[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showImageViewer(url, item);
                        }
                    });
                }

                System.out.println("Item: " + item.getHref());
                System.out.println("Image: -->" + url);
                return null;
            }
        };

        proccesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //add the preview thumb when the thread finish
                if (imageView[0] != null)
                    paneMedia.getChildren().add(imageView[0]);
            }
        });

        startTread(proccesTask);
    }

    private void proccesVideo(Item item) {
        ImageView[] imageView = new ImageView[1];

        Task<Void> proccesTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LibraryCollection.Video video = new LibraryCollection.Video(item.getHref());
                String url = video.getLinkVideo();
                String thumbUrl = video.getLinkThumb();

                imageView[0] = getImageView(thumbUrl, item);

                if (imageView[0] != null) {
                    imageView[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showWebViewer(url, item);
                        }
                    });
                }

                System.out.println("Item: " + item.getHref());
                System.out.println("Video: -->" + url);
                System.out.println("Thumb: -->" + video.getLinkThumb());
                System.out.println();
                return null;
            }
        };

        proccesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //add the preview thumb when the thread finish
                if (imageView[0] != null)
                    paneMedia.getChildren().add(imageView[0]);
            }
        });

        startTread(proccesTask);
    }

    private void proccesAudio(Item item) {
        ImageView[] imageView = new ImageView[1];

        Task<Void> proccesTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LibraryCollection.Audio audio = new LibraryCollection.Audio(item.getHref());
                String url = audio.getLinkAudio();
                String thumbURL = "/resources/images/audio.png";

                imageView[0] = getImageView(thumbURL, item);

                if (imageView[0] != null) {
                    imageView[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showWebViewer(url, item);
                        }
                    });
                }


                System.out.println("Audio: -->" + url);
                System.out.println();
                return null;
            }
        };

        proccesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //agregar el audio a la UI cuando termine el hilo
                if (imageView[0] != null)
                    paneMedia.getChildren().add(imageView[0]);
            }
        });

        startTread(proccesTask);
    }

    /*
     * -----------------------------------------------------------------------------------------------
     */

    private ImageView getImageView(String url, Item item) {
        if (url == null) return null;

        ImageView imageView = new ImageView(new Image(url));
        imageView.setFitWidth(230);
        imageView.setFitHeight(230);
        imageView.setPreserveRatio(true);
        imageView.setCursor(Cursor.HAND);
        Tooltip.install(imageView, new Tooltip(item.getData().get(0).getMedia_type().toUpperCase()));
        return imageView;
    }

    private void showImageViewer(String url, Item item) {
        ImageViewerController controller = new ImageViewerController(item, url);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/image_viewer_window.fxml"));
        loader.setController(controller);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWebViewer(String url, Item item) {
        Stage stage = new Stage();
        WebViewerController controller = new WebViewerController(item, url, stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/web_viewer_window.fxml"));
        loader.setController(controller);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kill all search threads
     */
    private void killThreads() {
        searchGroup.stop();

        if (!searchGroup.isDestroyed())
            searchGroup.destroy();

        searchGroup = new ThreadGroup("search");
    }

    /**
     * Start a thread
     * @param task
     */
    private void startTread(Task task){
        Thread thread = new Thread(searchGroup, task);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

}
