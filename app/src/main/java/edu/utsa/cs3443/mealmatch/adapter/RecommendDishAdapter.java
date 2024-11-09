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

public class RecommendDishAdapter extends RecyclerView.Adapter<RecommendDishAdapter.RecommendDishViewHolder>{
    private ArrayList<Dish> dishesList;
    private Context context;

    public RecommendDishAdapter(Context context, ArrayList<Dish> dishesList) {
        this.context = context;
        this.dishesList = dishesList;
    }

    @NonNull
    @Override
    public RecommendDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_recommend_dish, parent, false);
        return new RecommendDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendDishAdapter.RecommendDishViewHolder holder, int position) {
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

        //holder.imgDish.setImageResource(dish.getImageResourceId());  // Assuming you have image resources
        holder.imgFavoriteIcon.setImageResource(isFavorite ? R.drawable.ic_heart_liked : R.drawable.ic_heart_empty);

        // Optional: Set click listener for favorite icon
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
    }


    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    static class RecommendDishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish, imgFavoriteIcon;
        TextView txtDishName;

        public RecommendDishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            imgFavoriteIcon = itemView.findViewById(R.id.ic_add_favorite);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
        }
    }
}
