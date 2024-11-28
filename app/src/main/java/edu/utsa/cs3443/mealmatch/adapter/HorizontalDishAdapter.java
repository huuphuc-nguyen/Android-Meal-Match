package edu.utsa.cs3443.mealmatch.adapter;

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
 * Adapter class for displaying a horizontal list of dishes in a RecyclerView.
 * Each dish card includes an image, name, and a favorite toggle button.
 *
 * @author Felix Nguyen
 */
public class HorizontalDishAdapter extends RecyclerView.Adapter<HorizontalDishAdapter.RecommendDishViewHolder> {

    private ArrayList<Dish> dishesList;
    private Context context;
    private OnDishClickListener dishClickListener;

    /**
     * Constructor for HorizontalDishAdapter.
     *
     * @param context           the context of the activity or fragment.
     * @param dishesList        the list of dishes to display.
     * @param dishClickListener listener for handling dish click events.
     */
    public HorizontalDishAdapter(Context context, ArrayList<Dish> dishesList, OnDishClickListener dishClickListener) {
        this.context = context;
        this.dishesList = dishesList;
        this.dishClickListener = dishClickListener;
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
        View view = LayoutInflater.from(context).inflate(R.layout.card_horizontal_fit_dish, parent, false);
        return new RecommendDishViewHolder(view);
    }

    /**
     * Binds data to the views for a specific position.
     *
     * @param holder   the ViewHolder to bind data to.
     * @param position the position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull HorizontalDishAdapter.RecommendDishViewHolder holder, int position) {
        Dish dish = dishesList.get(position);

        boolean isFavorite = UserManager.getInstance().getUser().getFavoriteDishes().contains(dish.getID());

        // Bind data to views
        holder.txtDishName.setText(dish.getName());

        // Load image using Glide
        Glide.with(context)
                .load(dish.getImageUrl()) // Load the image from the URL in the Dish object
                .placeholder(R.drawable.background_login)
                .error(R.drawable.background_login)
                .into(holder.imgDish);

        holder.imgFavoriteIcon.setImageResource(isFavorite ? R.drawable.ic_heart_liked : R.drawable.ic_heart_empty);

        // Set click listener for favorite icon
        holder.imgFavoriteIcon.setOnClickListener(v -> {
            boolean isFavoriteNow = UserManager.getInstance().getUser().getFavoriteDishes().contains(dish.getID());
            if (isFavoriteNow) {
                // Remove from favorites
                UserManager.getInstance().removeFavoriteDish(dish.getID(), context);
            } else {
                // Add to favorites
                UserManager.getInstance().addFavoriteDish(dish.getID(), context);
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
        ImageView imgDish, imgFavoriteIcon;
        TextView txtDishName;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView the item view to be held.
         */
        public RecommendDishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            imgFavoriteIcon = itemView.findViewById(R.id.ic_add_favorite);
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
}