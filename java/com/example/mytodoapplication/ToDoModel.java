package com.example.mytodoapplication;

//This class defines the task object and gives the attributes for the model.
public class ToDoModel {
    private int id, status;
    private String task;


    //These are the getters and setters for each of the parameters.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
