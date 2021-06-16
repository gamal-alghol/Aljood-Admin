package com.example.aljoodadmin.notifcation;

public class NotificationSender {
    public Notification data;
    public String to;

    public NotificationSender(Notification data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {
    }
}
