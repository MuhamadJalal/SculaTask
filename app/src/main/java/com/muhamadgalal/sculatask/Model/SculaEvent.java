package com.muhamadgalal.sculatask.Model;

import java.io.Serializable;

public class SculaEvent implements Serializable{

    private final static long serializable =10l;
    private String title;
    private String description;
    private String eventAddedSince;
    private int eventID;

    public SculaEvent() {
    }

    public SculaEvent(String title, String description, String eventAddedSince, int eventID) {
        this.title = title;
        this.description = description;
        this.eventAddedSince = eventAddedSince;
        this.eventID = eventID;
    }

    public static long getSerializable() {
        return serializable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventAddedDate() {
        return eventAddedSince;
    }

    public void setEventAddedDate(String eventAddedSince) {
        this.eventAddedSince = eventAddedSince;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
