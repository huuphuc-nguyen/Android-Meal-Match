package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a grocery list containing a collection of tasks.
 * Each grocery list is identified by a unique ID and stores task IDs.
 *
 * @author Felix Nguyen, Ian Rohan
 */
public class GroceryList {
    private int ID; // Unique identifier for the grocery list.
    private ArrayList<Integer> tasks; // List of task IDs associated with the grocery list.

    /**
     * Constructs a GroceryList object with the specified ID and tasks.
     *
     * @param ID    the unique identifier for the grocery list.
     * @param tasks the list of task IDs associated with the grocery list.
     */
    public GroceryList(int ID, ArrayList<Integer> tasks) {
        this.ID = ID;
        this.tasks = tasks;
    }

    /**
     * Gets the unique ID of the grocery list.
     *
     * @return the grocery list ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique ID for the grocery list.
     *
     * @param ID the new ID for the grocery list.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets the list of task IDs associated with the grocery list.
     *
     * @return an {@link ArrayList} of task IDs.
     */
    public ArrayList<Integer> getTasks() {
        return tasks;
    }

    /**
     * Sets the tasks for the grocery list.
     *
     * @param tasks an {@link ArrayList} of task IDs to associate with the grocery list.
     */
    public void setTasks(ArrayList<Integer> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the grocery list by its ID.
     *
     * @param task the {@link Task} to be added to the grocery list.
     */
    public void addTask(Task task) {
        this.tasks.add(task.getID());
    }

    /**
     * Returns a string representation of the grocery list.
     * The string is formatted as a CSV, where the first column is the grocery list ID,
     * and the second column is a semicolon-separated list of task IDs.
     *
     * @return a string representation of the grocery list in CSV format.
     */
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