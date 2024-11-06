package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {

        String tasksString = tasks.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        // Format as CSV
        return String.format("%d, %s",
                ID, tasksString);
    }
}
