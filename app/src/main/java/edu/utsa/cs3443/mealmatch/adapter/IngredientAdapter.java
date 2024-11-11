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

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final ArrayList<String> ingredientList;
    private final Context context;
    private final OnAddButtonClickListener listener;

    public interface OnAddButtonClickListener {
        void onAddButtonClick(String ingredient);
    }

    public IngredientAdapter(Context context, ArrayList<String> ingredientList, OnAddButtonClickListener listener) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        String ingredient = ingredientList.get(position);
        holder.txtIngredient.setText(ingredient);

        // Handle add button click
        holder.btnAddToGroceryList.setOnClickListener(v -> listener.onAddButtonClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView txtIngredient;
        Button btnAddToGroceryList;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIngredient = itemView.findViewById(R.id.txt_ingredient);
            btnAddToGroceryList = itemView.findViewById(R.id.btn_add_to_grocery_list);
        }
    }
}
