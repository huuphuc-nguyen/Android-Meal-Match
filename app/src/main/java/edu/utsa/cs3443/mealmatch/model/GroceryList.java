package edu.utsa.cs3443.mealmatch.model;

import java.util.ArrayList;

public class GroceryList {
    private int ID;
    private ArrayList<Integer> tasks;
    private static int IDCounter;

    public GroceryList(int ID, ArrayList<Integer> tasks) {
        this.ID = ID;
        this.tasks = tasks;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ArrayList<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Integer> tasks) {
        this.tasks = tasks;
    }

    public static int getIDCounter() {
        return IDCounter;
    }

    public static void setIDCounter(int IDCounter) {
        GroceryList.IDCounter = IDCounter;
    }
}
