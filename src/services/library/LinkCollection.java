package services.library;

import services.Service;

public class LinkCollection {
    private String rel; //the abbreviation of the promt --> next or prev
    private String prompt; //word Next or Previous depending of link is for the previous or Next page
    private String href; //the link of the previous or next page

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getHref() {
        return Service.removeCommonsCharacter(href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    //</editor-fold>
}
