package edu.utsa.cs3443.mealmatch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import edu.utsa.cs3443.mealmatch.R;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.utils.UserManager;
// display dish img, name, and btn to add meal to mealplan
public class AddMealAdapter extends RecyclerView.Adapter<AddMealAdapter.RecommendDishViewHolder>{

    MealPlan mealPlan = DataManager.getInstance().getMealPlanById(1);
    ArrayList<Integer> mealIDs = mealPlan.getDishes();
    Map<Integer, MealPlan> meals = DataManager.getInstance().getMealPlans();

    private ArrayList<Dish> dishesList;
    private Context context;
    private OnDishClickListener dishClickListener;

    public AddMealAdapter(Context context, ArrayList<Dish> dishesList, OnDishClickListener dishClickListener) {
        this.context = context;
        this.dishesList = dishesList;
        this.dishClickListener = dishClickListener;
    }

    @NonNull
    @Override
    public RecommendDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_add_dish, parent, false);
        return new RecommendDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMealAdapter.RecommendDishViewHolder holder, int position) {
        Dish dish = dishesList.get(position);

        boolean isInMealPlan = mealIDs.contains(dish.getID());

        // Bind data to views
        holder.txtDishName.setText(dish.getName());

        // Load image using Glide
        Glide.with(context)
                .load(dish.getImageUrl()) // Load the image from the URL in the Dish object
                .placeholder(R.drawable.background_login)
                .error(R.drawable.background_login)
                .into(holder.imgDish);

        //holder.imgDish.setImageResource(dish.getImageResourceId());  // Assuming you have image resources
        holder.imgCheckIcon.setImageResource(isInMealPlan ? R.drawable.ic_checkmark : R.drawable.ic_add_white);

        // Optional: Set click listener for check icon
        holder.imgCheckIcon.setOnClickListener(v -> {
            boolean isInMealPlanNow = mealIDs.contains(dish.getID());
            if (isInMealPlanNow) {
                // Remove from meal plan; need to make dynamic
                UserManager.getInstance().removeDishFromMealPlan(dish.getID(),1, context);

            } else {
                // Add to mealplan 1; need to make dynamic
                UserManager.getInstance().addMealPlan(dish.getID(),1, context);
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

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    static class RecommendDishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish, imgCheckIcon;
        TextView txtDishName;

        public RecommendDishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            imgCheckIcon = itemView.findViewById(R.id.ic_add_meal);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
        }
    }

    public interface OnDishClickListener {
        void onDishClick(Dish dish);
    }

}

