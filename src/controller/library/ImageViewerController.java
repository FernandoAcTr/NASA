package controller.library;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.MyUtils;
import services.library.Data;
import services.library.Item;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageViewerController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtCenter;

    @FXML
    private TextField txtDate;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtNasaID;

    @FXML
    private TextField txtPhothographer;

    @FXML
    private TextField txtSecondCreator;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextArea txtKeywords;

    @FXML
    private TextField txtMediaType;

    private Item item;
    private Data data;
    private String imageURL;

    public ImageViewerController(Item item, String imageURL) {
        this.item = item;
        this.data = item.getData().get(0);
        this.imageURL = imageURL;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValues();
    }

    private void setValues(){
        txtTitle.setText(data.getTitle());
        txtCenter.setText(data.getCenter());
        txtDate.setText(data.getDate_created());
        txtDescription.setText(MyUtils.formatText(data.getDescription(), 60));
        txtLocation.setText(data.getLocation());
        txtMediaType.setText(data.getMedia_type());
        txtNasaID.setText(data.getNasa_id());
        txtPhothographer.setText(data.getPhotographer());
        txtSecondCreator.setText(data.getSecondary_creator());
        txtKeywords.setText(getKeyWords());
        imageView.setImage(new Image(imageURL));
    }

    private String getKeyWords(){
        String keywords = "";
        for (String w : data.getKeywords())
            keywords += w + ", ";

        return keywords.substring(0, keywords.length()-2);
    }
}
