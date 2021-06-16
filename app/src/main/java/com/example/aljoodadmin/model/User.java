package com.example.aljoodadmin.model;

public class User {
 private    String name , email , password,tokenId,firebaseId;
    private Double account;
    private long time;
    private  int points;

    public User(String name, String email, String password, String tokenId, String firebaseId, Double account,int points, long time) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.tokenId = tokenId;
        this.firebaseId = firebaseId;
        this.account = account;
        this.time = time;
        this.points = points;
    }
    public User(){}

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
