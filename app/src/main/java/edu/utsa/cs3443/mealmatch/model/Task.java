package edu.utsa.cs3443.mealmatch.model;

import androidx.annotation.NonNull;

/**
 * Represents a task with a unique ID, name, type, and completion status.
 * Tasks can be categorized by type and marked as done or not done.
 *
 * @author Felix Nguyen, Ian Rohan
 */
public class Task {
    private int ID; // Unique identifier for the task.
    private String name; // Name of the task.
    private String type; // Type/category of the task.
    private boolean isDone; // Completion status of the task.

    /**
     * Constructs a Task object with the specified parameters.
     *
     * @param ID     the unique identifier for the task.
     * @param name   the name of the task.
     * @param type   the type or category of the task.
     * @param isDone the completion status of the task.
     */
    public Task(int ID, String name, String type, boolean isDone) {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.isDone = isDone;
    }

    /**
     * Retrieves the unique ID of the task.
     *
     * @return the task ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique ID for the task.
     *
     * @param ID the new ID for the task.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Retrieves the name of the task.
     *
     * @return the task name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     *
     * @param name the new name for the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type or category of the task.
     *
     * @return the task type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type or category of the task.
     *
     * @param type the new type for the task.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Checks whether the task is marked as done.
     *
     * @return {@code true} if the task is done; {@code false} otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Updates the completion status of the task.
     *
     * @param done the new completion status of the task.
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Returns a string representation of the task.
     * The string is formatted as a CSV line with the following structure:
     * ID, name, type, isDone (1 for done, 0 for not done)
     *
     * @return a string representation of the task in CSV format.
     */
    @NonNull
    @Override
    public String toString() {
        return ID + ", " + name + ", " + type + ", " + (isDone ? 1 : 0);
    }
}