package com.example.dailyessential.notes.model;

/**
 * It is class which will help you to retrive data from firebase database.
 * */
public class Note {
    private String title;
    private String content;
    private String color;
    private String dateAndTime;
    private String search;

    public Note(){

    }
    public Note(String title, String content, String color, String dateAndTime, String search) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.dateAndTime = color;
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSearch(String search) {
        this.search = search;
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
