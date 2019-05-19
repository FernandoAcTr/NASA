package services.techport;

import java.io.File;

public class LibraryFile{
    private int id;
    private String url;
    private int size;
    private File localFile;
    private String title;

    // <editor-fold defaultstate="collapsed" desc=" getters() ">

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    public File getLocalFile() {
        return localFile;
    }

    public String getTitle() {
        return title;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" setters() ">

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    // </editor-fold>



}
