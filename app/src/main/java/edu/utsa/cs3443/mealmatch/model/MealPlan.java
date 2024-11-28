package edu.utsa.cs3443.mealmatch.model;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Represents a meal plan containing a collection of dishes for a specific date.
 * Each meal plan is identified by a unique ID and associates multiple dish IDs.
 *
 * @author Felix Nguyen
 */
public class MealPlan {
    private int ID; // Unique identifier for the meal plan.
    private Date planDate; // The date for which the meal plan is scheduled.
    private ArrayList<Integer> dishesID; // List of dish IDs included in the meal plan.

    /**
     * Constructs a MealPlan object with the specified ID, date, and dishes.
     *
     * @param ID        the unique identifier for the meal plan.
     * @param planDate  the date for which the meal plan is scheduled.
     * @param dishesID  the list of dish IDs included in the meal plan.
     */
    public MealPlan(int ID, Date planDate, ArrayList<Integer> dishesID) {
        this.ID = ID;
        this.planDate = planDate;
        this.dishesID = dishesID;
    }

    /**
     * Retrieves the unique ID of the meal plan.
     *
     * @return the meal plan ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique ID for the meal plan.
     *
     * @param ID the new ID for the meal plan.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Retrieves the date for which the meal plan is scheduled.
     *
     * @return the meal plan date.
     */
    public Date getPlanDate() {
        return planDate;
    }

    /**
     * Sets the date for which the meal plan is scheduled.
     *
     * @param planDate the new date for the meal plan.
     */
    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    /**
     * Retrieves the list of dish IDs associated with the meal plan.
     *
     * @return an {@link ArrayList} of dish IDs.
     */
    public ArrayList<Integer> getDishes() {
        return dishesID;
    }

    /**
     * Sets the list of dish IDs for the meal plan.
     *
     * @param dishesID an {@link ArrayList} of dish IDs to associate with the meal plan.
     */
    public void setDishes(ArrayList<Integer> dishesID) {
        this.dishesID = dishesID;
    }

    /**
     * Returns a string representation of the meal plan.
     * The string is formatted as a CSV line with the following structure:
     * ID, planDate (MM-dd-yyyy), dishID1;dishID2;...;dishIDn
     *
     * @return a string representation of the meal plan in CSV format.
     */
    @Override
    public String toString() {
        // Format the date as MM-dd-yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = formatter.format(planDate);

        // Convert dishesID list to semicolon-separated string
        String dishesStr = dishesID.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        // Return as CSV line
        return ID + ", " + dateStr + ", " + dishesStr;
    }
}
