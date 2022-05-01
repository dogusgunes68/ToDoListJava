package com.example.todolistjava.model;

public class ToDo {
    private String toDoTitle;
    private String toDoContent;
    private String degreeOfImportance;
    private String userEmail;
    private String date;
    private String color;

    public ToDo(String toDoTitle, String toDoContent, String degreeOfImportance, String userEmail, String date,String color){
        this.toDoTitle = toDoTitle;
        this.toDoContent = toDoContent;
        this.degreeOfImportance = degreeOfImportance;
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

    public String getDegreeOfImportance() {
        return degreeOfImportance;
    }

    public void setDegreeOfImportance(String degreeOfImportance) {
        this.degreeOfImportance = degreeOfImportance;
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
