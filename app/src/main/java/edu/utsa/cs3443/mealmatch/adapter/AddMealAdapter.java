package edu.utsa.cs3443.mealmatch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.R;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.MealPlan;

/**
 * Adapter class for displaying a list of dishes in a RecyclerView with the option
 * to add or remove a dish from a meal plan.
 *
 * @author Felix Nguyen
 */
public class AddMealAdapter extends RecyclerView.Adapter<AddMealAdapter.RecommendDishViewHolder> {

    private MealPlan mealPlan;
    private ArrayList<Dish> dishesList;
    private Context context;
    private OnDishClickListener dishClickListener;

    /**
     * Constructor for AddMealAdapter.
     *
     * @param context           the context of the activity or fragment.
     * @param dishesList        the list of dishes to display.
     * @param dishClickListener listener for handling dish click events.
     * @param mealId            the ID of the meal plan to modify.
     */
    public AddMealAdapter(Context context, ArrayList<Dish> dishesList, OnDishClickListener dishClickListener, int mealId) {
        this.context = context;
        this.dishesList = dishesList;
        this.dishClickListener = dishClickListener;
        this.mealPlan = DataManager.getInstance().getMealPlanById(mealId);
    }

    /**
     * Inflates the layout for each RecyclerView item.
     *
     * @param parent   the parent ViewGroup.
     * @param viewType the view type of the new View.
     * @return a new instance of RecommendDishViewHolder.
     */
    @NonNull
    @Override
    public RecommendDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_add_dish, parent, false);
        return new RecommendDishViewHolder(view);
    }

    /**
     * Binds data to the views for a specific position.
     *
     * @param holder   the ViewHolder to bind data to.
     * @param position the position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull AddMealAdapter.RecommendDishViewHolder holder, int position) {
        Dish dish = dishesList.get(position);

        boolean isInMealPlan = mealPlan.getDishes().contains(dish.getID());

        // Bind data to views
        holder.txtDishName.setText(dish.getName());

        // Load image using Glide
        Glide.with(context)
                .load(dish.getImageUrl()) // Load the image from the URL in the Dish object
                .placeholder(R.drawable.background_login)
                .error(R.drawable.background_login)
                .into(holder.imgDish);

        holder.imgCheckIcon.setImageResource(isInMealPlan ? R.drawable.ic_checkmark : R.drawable.ic_add_white);

        holder.imgCheckIcon.setOnClickListener(v -> {
            boolean isInMealPlanNow = mealPlan.getDishes().contains(dish.getID());

            if (isInMealPlanNow) {
                DataManager.getInstance().getMealPlanById(mealPlan.getID()).getDishes().remove(Integer.valueOf(dish.getID()));
                DataManager.getInstance().updateMealPlan(context);
            } else {
                DataManager.getInstance().getMealPlanById(mealPlan.getID()).getDishes().add(dish.getID());
                DataManager.getInstance().updateMealPlan(context);
            }
            // Update UI after toggle
            notifyItemChanged(position);
        });

        // Click listener for the entire card
        holder.itemView.setOnClickListener(v -> {
            if (dishClickListener != null) {
                dishClickListener.onDishClick(dish); // Pass clicked dish to the listener
            }
        });
    }

    /**
     * Returns the total number of items in the dataset.
     *
     * @return the size of the dish list.
     */
    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    /**
     * ViewHolder class for the dish item view.
     */
    static class RecommendDishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish, imgCheckIcon;
        TextView txtDishName;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView the item view to be held.
         */
        public RecommendDishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            imgCheckIcon = itemView.findViewById(R.id.ic_add_meal);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
        }
    }

    /**
     * Interface for handling dish click events.
     */
    public interface OnDishClickListener {
        /**
         * Callback method when a dish is clicked.
         *
         * @param dish the clicked dish.
         */
        void onDishClick(Dish dish);
    }

    /**
     * Updates the dish list and associated meal plan data.
     *
     * @param newDishes the new list of dishes to display.
     * @param mealPlan  the updated meal plan.
     */
    public void updateDishes(ArrayList<Dish> newDishes, MealPlan mealPlan) {
        this.mealPlan = mealPlan;
        this.dishesList.clear(); // Clear the old data
        this.dishesList.addAll(newDishes); // Add the new data
        notifyDataSetChanged(); // Notify the adapter of the data change
    }
}
