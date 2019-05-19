package services.techport;

import java.util.ArrayList;

public class LibraryItem{
    private int id;
    private String title;
    private String type;
    private String description;
    private String externalUrl;
    private String publishedBy;
    private String publishedDate;
    private ArrayList<LibraryFile> files;

    // <editor-fold defaultstate="collapsed" desc=" getters() ">

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public ArrayList<LibraryFile> getFiles() {
        return files;
    }

    // </editor-fold>
}
