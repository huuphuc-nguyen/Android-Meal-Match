package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.adapter.IngredientAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;

public class DishDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Retrieve the dish_id passed from MainActivity
        int dishId = getIntent().getIntExtra("dish_id", -1);
        Dish dish = DataManager.getInstance().getDishById(dishId);

        displayDishNameAndDescription(dish);
        displayIngredientList(dish);

        setUpButtons(dish);
    }

    private void addIngredientToGroceryList(String ingredient){
        // TODO
    }
    private void displayDishNameAndDescription(Dish dish) {
        // Update the UI with dish details
        TextView dishNameTextView = findViewById(R.id.txt_dish_name);
        ImageView dishImageView = findViewById(R.id.topImage);
        TextView dishDescriptionTextView = findViewById(R.id.txt_dish_description);

        dishNameTextView.setText(dish.getName());
        dishDescriptionTextView.setText(dish.getDescription());

        // Load dish image using Glide
        Glide.with(this)
                .load(dish.getImageUrl())
                .into(dishImageView);
    }
    private void displayIngredientList(Dish dish){
        ArrayList<String> ingredients = dish.getIngredients();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        IngredientAdapter adapter = new IngredientAdapter(this, ingredients, ingredient -> {
            addIngredientToGroceryList(ingredient);
            Toast.makeText(this, ingredient + " added to grocery list.", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void setUpButtons(Dish dish){
        ImageView btn_back = findViewById(R.id.btn_back);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);
        LinearLayout btn_add_all = findViewById(R.id.layout_add_all_ingredient);

        btn_add_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String ingredient : dish.getIngredients()){
                    addIngredientToGroceryList(ingredient);
                }
                Toast.makeText(DishDetailActivity.this, "All ingredients added to grocery list.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, MealPlannerActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, GroceryListActivity.class));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}