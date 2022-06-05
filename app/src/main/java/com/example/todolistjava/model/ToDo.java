package com.example.todolistjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity
public class ToDo {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "title")
    private String toDoTitle;
    @ColumnInfo(name = "content")
    private String toDoContent;
    @ColumnInfo(name = "usermail")
    private String userEmail;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "color")
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

    @Ignore
    public ToDo(String toDoTitle, String toDoContent, String userEmail, String date,String color){
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
