package edu.utsa.cs3443.mealmatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.R;

/**
 * Adapter class for displaying a list of ingredients in a RecyclerView.
 * Each item includes an ingredient name and a button to add it to the grocery list.
 *
 * @author Felix Nguyen
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final ArrayList<String> ingredientList;
    private final Context context;
    private final OnAddButtonClickListener listener;

    /**
     * Interface for handling "Add to Grocery List" button clicks.
     */
    public interface OnAddButtonClickListener {
        /**
         * Callback method triggered when the "Add" button is clicked for an ingredient.
         *
         * @param ingredient the ingredient to be added to the grocery list.
         */
        void onAddButtonClick(String ingredient);
    }

    /**
     * Constructor for IngredientAdapter.
     *
     * @param context       the context of the activity or fragment.
     * @param ingredientList the list of ingredients to display.
     * @param listener      the listener to handle "Add to Grocery List" button clicks.
     */
    public IngredientAdapter(Context context, ArrayList<String> ingredientList, OnAddButtonClickListener listener) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    /**
     * Inflates the layout for each RecyclerView item.
     *
     * @param parent   the parent ViewGroup.
     * @param viewType the view type of the new View.
     * @return a new instance of IngredientViewHolder.
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    /**
     * Binds data to the views for a specific position.
     *
     * @param holder   the ViewHolder to bind data to.
     * @param position the position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        String ingredient = ingredientList.get(position);
        holder.txtIngredient.setText(ingredient);

        // Handle "Add to Grocery List" button click
        holder.btnAddToGroceryList.setOnClickListener(v -> listener.onAddButtonClick(ingredient));
    }

    /**
     * Returns the total number of items in the dataset.
     *
     * @return the size of the ingredient list.
     */
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    /**
     * ViewHolder class for the ingredient item view.
     */
    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView txtIngredient;
        Button btnAddToGroceryList;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView the item view to be held.
         */
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIngredient = itemView.findViewById(R.id.txt_ingredient);
            btnAddToGroceryList = itemView.findViewById(R.id.btn_add_to_grocery_list);
        }
    }
}
