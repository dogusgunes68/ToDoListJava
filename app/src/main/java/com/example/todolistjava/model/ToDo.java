package com.example.todolistjava.model;

import com.google.firebase.Timestamp;


public class ToDo {


    private long uid;


    private String toDoTitle;

    private String toDoContent;

    private String userEmail;

    private Timestamp date;

    private String color;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Long getUid(){
        return uid;
    }

    public void setUid(Long newUid){
        uid = newUid;
    }


    private String id = null;

    public ToDo(String toDoTitle, String toDoContent, String userEmail, Timestamp date,String color){
        this.toDoTitle = toDoTitle;
        this.toDoContent = toDoContent;
        this.userEmail = userEmail;
        this.date = date;
        this.color = color;
    }

    public ToDo(){

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getToDoTitle() {
        return toDoTitle;
    }

    public void setToDoTitle(String toDoTitle) {
        this.toDoTitle = toDoTitle;
    }

    public String getToDoContent() {
        return toDoContent;
    }

    public void setToDoContent(String toDoContent) {
        this.toDoContent = toDoContent;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
