package model;

public class APODBean {
    private int id;
    private String title;
    private String date;
    private String explanation;
    private String copyright;
    private String media_type;
    private String url;
    private String service_version;
    private MyImage image;

    public APODBean() {
    }

    public APODBean(String title, String date, String explanation, String copyright, String media_type, String url, String service_version) {
        this.title = title;
        this.date = date;
        this.explanation = explanation;
        this.copyright = copyright;
        this.media_type = media_type;
        this.url = url;
        this.service_version = service_version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getService_version() {
        return service_version;
    }

    public void setService_version(String service_version) {
        this.service_version = service_version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MyImage getImage() {
        return image;
    }

    public void setImage(MyImage image) {
        this.image = image;
    }
}
