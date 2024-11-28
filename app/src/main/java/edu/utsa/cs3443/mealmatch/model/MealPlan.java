package edu.utsa.cs3443.mealmatch.model;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class MealPlan {
    private int ID;
    private Date planDate;
    private ArrayList<Integer> dishesID;

    public MealPlan(int ID, Date planDate, ArrayList<Integer> dishesID) {
        this.ID = ID;
        this.planDate = planDate;
        this.dishesID = dishesID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public ArrayList<Integer> getDishes() {
        return dishesID;
    }

    public void setDishes(ArrayList<Integer> dishesID) {
        this.dishesID = dishesID;
    }

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
