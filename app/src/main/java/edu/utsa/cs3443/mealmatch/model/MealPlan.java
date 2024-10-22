package edu.utsa.cs3443.mealmatch.model;

import java.util.ArrayList;
import java.util.Date;

public class MealPlan {
    private int ID;
    private Date planDate;
    private ArrayList<Dish> dishes;
    private static int IDCounter;

    public MealPlan(int ID, Date planDate, ArrayList<Dish> dishes) {
        this.ID = ID;
        this.planDate = planDate;
        this.dishes = dishes;
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

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public static int getIDCounter() {
        return IDCounter;
    }

    public static void setIDCounter(int IDCounter) {
        MealPlan.IDCounter = IDCounter;
    }
}
