package com.example.aljoodadmin.model;

import androidx.annotation.Keep;

import java.util.Date;


@Keep
public class Msg {

    private String sender;
    private int msgfrom;
    private String message;
    private Date time;

    public Msg() {}


    public Msg(String sender, int msgfrom, String message, Date time) {
        this.sender = sender;
        this.msgfrom = msgfrom;
        this.message = message;
        this.time = time;
    }

    public int getMsgfrom() {
        return msgfrom;
    }

    public void setMsgfrom(int msgfrom) {
        this.msgfrom = msgfrom;
    }

    public String getSender() {return sender;}
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
}
