package edu.utsa.cs3443.mealmatch;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

        if (dishId != -1) {
            // Fetch dish details using the dishId (e.g., from DataManager or database)
            Dish dish = DataManager.getInstance().getDishById(dishId);

            if (dish != null) {
                displayDishDetails(dish);
            } else {
                Toast.makeText(this, "Dish not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid dish ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDishDetails(Dish dish) {
        // Update the UI with dish details
        TextView dishNameTextView = findViewById(R.id.txt_dish_name);
        ImageView dishImageView = findViewById(R.id.topImage);
//        TextView dishDescriptionTextView = findViewById(R.id.txt_dish_description);
//
        dishNameTextView.setText(dish.getName());
//        dishDescriptionTextView.setText(dish.getDescription());

        // Load dish image using Glide
        Glide.with(this)
                .load(dish.getImageUrl())
                .into(dishImageView);
    }
}