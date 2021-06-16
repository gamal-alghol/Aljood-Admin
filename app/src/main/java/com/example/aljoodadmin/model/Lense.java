package com.example.aljoodadmin.model;

public class Lense {
    String id;
    Float sph, cyl;
    boolean available;

    public Lense(String id, Float sph, Float cyl, boolean available) {
        this.id = id;
        this.sph = sph;
        this.cyl = cyl;
        this.available = available;
    }
public Lense(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getSph() {
        return sph;
    }

    public void setSph(Float sph) {
        this.sph = sph;
    }

    public Float getCyl() {
        return cyl;
    }

    public void setCyl(Float cyl) {
        this.cyl = cyl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
