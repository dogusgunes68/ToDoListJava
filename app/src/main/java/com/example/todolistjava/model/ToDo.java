package com.example.todolistjava.model;

public class ToDo {
    private String toDoTitle;
    private String toDoContent;
    private String backgroundColor;
    private String userEmail;
    private String date;

    public ToDo(String toDoTitle,String toDoContent,String backgroundColor,String userEmail, String date){
        this.toDoTitle = toDoTitle;
        this.toDoContent = toDoContent;
        this.backgroundColor = backgroundColor;
        this.userEmail = userEmail;
        this.date = date;
    }

    public ToDo(){

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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
