package model;

import javafx.scene.image.Image;

import java.io.File;

public class MyImage {
    private Image image;
    private File imageFile;

    public MyImage() {
    }

    public MyImage(Image image) {
        this.image = image;
    }

    public MyImage(File imageFile) {
        this.imageFile = imageFile;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
