package services.library;

import services.Service;

import java.util.ArrayList;

public class Collection {
    private String version;
    private Metadata metadata; //metadata of search. Only total of results. But JSON send it like a class.
    private String href;       //link of the search
    private ArrayList<LinkCollection> links; //links to go to next Page and Previous Page
    private ArrayList<Item> items;   //All media items

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getHref() {
        return Service.removeCommonsCharacter(href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<LinkCollection> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkCollection> links) {
        this.links = links;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    //</editor-fold>
}
