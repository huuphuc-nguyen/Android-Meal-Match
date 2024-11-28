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

import edu.utsa.cs3443.mealmatch.R;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * Adapter class for displaying recommended dishes in a RecyclerView.
 * Each card contains a dish image, name, and a favorite button to toggle the dish as a favorite.
 *
 * @author Felix Nguyen
 */
public class RecommendDishAdapter extends RecyclerView.Adapter<RecommendDishAdapter.RecommendDishViewHolder> {

    private ArrayList<Dish> dishesList;
    private final Context context;
    private final OnDishClickListener dishClickListener;

    /**
     * Interface for handling dish click events.
     */
    public interface OnDishClickListener {
        /**
         * Callback triggered when a dish is clicked.
         *
         * @param dish the clicked dish.
         */
        void onDishClick(Dish dish);
    }

    /**
     * Constructor for RecommendDishAdapter.
     *
     * @param context           the context of the activity or fragment.
     * @param dishesList        the list of dishes to display.
     * @param dishClickListener listener for handling dish click events.
     */
    public RecommendDishAdapter(Context context, ArrayList<Dish> dishesList, OnDishClickListener dishClickListener) {
        this.context = context;
        this.dishesList = dishesList;
        this.dishClickListener = dishClickListener;
    }

    /**
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent   the parent ViewGroup.
     * @param viewType the view type of the new View.
     * @return a new instance of RecommendDishViewHolder.
     */
    @NonNull
    @Override
    public RecommendDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_recommend_dish, parent, false);
        return new RecommendDishViewHolder(view);
    }

    /**
     * Binds data to the views for a specific position.
     *
     * @param holder   the ViewHolder to bind data to.
     * @param position the position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull RecommendDishViewHolder holder, int position) {
        Dish dish = dishesList.get(position);

        boolean isFavorite = UserManager.getInstance().getUser().getFavoriteDishes().contains(dish.getID());

        // Set dish name
        holder.txtDishName.setText(dish.getName());

        // Load dish image using Glide
        Glide.with(context)
                .load(dish.getImageUrl())
                .placeholder(R.drawable.background_login) // Placeholder image
                .error(R.drawable.background_login) // Error image
                .into(holder.imgDish);

        // Set favorite icon based on favorite status
        holder.imgFavoriteIcon.setImageResource(isFavorite ? R.drawable.ic_heart_liked : R.drawable.ic_heart_empty);

        // Click listener for the favorite icon
        holder.imgFavoriteIcon.setOnClickListener(v -> {
            boolean isFavoriteNow = UserManager.getInstance().getUser().getFavoriteDishes().contains(dish.getID());
            if (isFavoriteNow) {
                UserManager.getInstance().removeFavoriteDish(dish.getID(), context);
            } else {
                UserManager.getInstance().addFavoriteDish(dish.getID(), context);
            }
            notifyItemChanged(position); // Update UI
        });

        // Click listener for the entire card
        holder.itemView.setOnClickListener(v -> {
            if (dishClickListener != null) {
                dishClickListener.onDishClick(dish); // Notify listener of dish click
            }
        });
    }

    /**
     * Returns the total number of items in the dataset.
     *
     * @return the size of the dishes list.
     */
    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    /**
     * Updates the dataset with new dishes and refreshes the RecyclerView.
     *
     * @param newDishes the new list of dishes.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<Dish> newDishes) {
        this.dishesList = newDishes;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for the recommended dish item view.
     */
    static class RecommendDishViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDish, imgFavoriteIcon;
        TextView txtDishName;

        /**
         * Constructor for RecommendDishViewHolder.
         *
         * @param itemView the item view to hold.
         */
        public RecommendDishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            imgFavoriteIcon = itemView.findViewById(R.id.ic_add_favorite);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
        }
    }
}
