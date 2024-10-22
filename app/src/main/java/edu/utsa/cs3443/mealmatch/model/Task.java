package edu.utsa.cs3443.mealmatch.model;

public class Task {
    private int ID;
    private String name;
    private String type;
    private boolean isDone;
    private static int IDCounter;

    public Task(int ID, String name, String type, boolean isDone) {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.isDone = isDone;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public static int getIDCounter() {
        return IDCounter;
    }

    public static void setIDCounter(int IDCounter) {
        Task.IDCounter = IDCounter;
    }
}
