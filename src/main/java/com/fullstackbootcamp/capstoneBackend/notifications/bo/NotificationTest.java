package com.fullstackbootcamp.capstoneBackend.notifications.bo;

public class NotificationTest {
    private String from;
    private String text;

    public NotificationTest() {}

    public NotificationTest(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
