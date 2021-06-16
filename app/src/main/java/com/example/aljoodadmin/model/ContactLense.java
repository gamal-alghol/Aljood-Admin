package com.example.aljoodadmin.model;

public class ContactLense {
    String id;
    Float sph;
    boolean available;
    String color;

    public ContactLense(String id, Float sph, boolean available, String color) {
        this.id = id;
        this.sph = sph;
        this.available = available;
        this.color = color;
    }
    public ContactLense(){}
    public Float getSph() {
        return sph;
    }

    public void setSph(Float sph) {
        this.sph = sph;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
