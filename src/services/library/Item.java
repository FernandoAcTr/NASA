package services.library;

import services.Service;

import java.util.ArrayList;

/**
 * --------------------------------Clase que almacena cada Item de la busqueda-------------------
 */
public class Item {
    private ArrayList<Data> data; //all data of this item
    private String href; //the link of JSON of this specific item. NOT of the media but JSON information. This is the most important link to go to the media
    private ArrayList<LinkItem> links; //the links of the preview. Generally it is only one for images and two for videos

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getHref() {
        return Service.removeCommonsCharacter(href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<LinkItem> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkItem> links) {
        this.links = links;
    }

    // </editor-fold>

}
