package com.example.to_do_v3.TaskBackend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {

    private int id;
    private String name;
    private String description;
    private String dueDate;
    private String type;
    private int status = 0;

    public Task() {

    }
    public Task(String name, String description, String dueDate, String type,int status) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.type = type;
        this.status = status;
    }

    public Task(int id, String name, String description, String dueDate, String type,int status) {
        this(name, description, dueDate, type, status);
        this.id = id;
    }

    public static String formatDateString(Date date ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dueDateString = sdf.format(date);
        return dueDateString;
    }
    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {

        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

//    public void setDueDate (String s) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        dueDate = sdf.parse(s);
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
