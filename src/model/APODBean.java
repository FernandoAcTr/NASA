package model;

public class APODBean {
    private String copyright;
    private String date;
    private String explanation;
    private String media_type;
    private String service_version;
    private String title;
    private String url;

    public APODBean() {
    }

    public APODBean(String copyright, String date, String explanation, String media_type, String service_version, String title, String url) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.media_type = media_type;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
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

    public String formatExplanation(String explanation){
        String formatExplain = "";
        char character;

        for (int i = 0, limit = 0; i < explanation.length(); i++, limit++) {

             character = explanation.charAt(i);
             formatExplain += character;

            if(limit > 150)
                if(character == ' '){
                    formatExplain += "\n";
                    limit = 0;
                }
        }

        return formatExplain;
    }
}