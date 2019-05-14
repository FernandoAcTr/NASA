package services.library;

import services.Service;

public class LinkItem {
    private String render; //how render this preview.
    private String href; //the link of the preview of the item.
    private String rel;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getHref() {
        return Service.removeCommonsCharacter(href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }
    // </editor-fold>
}
