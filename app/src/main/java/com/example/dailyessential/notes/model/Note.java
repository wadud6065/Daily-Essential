package com.example.dailyessential.notes.model;

/**
 * It is class which will help you to retrive data from firebase database.
 * */
public class Note {
    private String title;
    private String subTitle;
    private String content;
    private String color;
    private String dateAndTime;

    public Note(){

    }
    public Note(String title, String subTitle, String content, String color) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getContent() {
        return content;
    }

    public String getColor() {
        return color;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}
