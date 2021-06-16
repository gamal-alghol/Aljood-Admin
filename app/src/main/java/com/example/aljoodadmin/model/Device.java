package com.example.aljoodadmin.model;

import java.util.List;

public class Device {
    public String devicesId,name,describe;
    public List<String> images;
    public boolean available, promotion;

    public Device(String devicesId, String name, String describe, List<String> images, boolean available, boolean promotion) {
        this.devicesId = devicesId;
        this.name = name;
        this.describe = describe;
        this.images = images;
        this.available = available;
        this.promotion = promotion;
    }
    public Device(){}

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }
}
