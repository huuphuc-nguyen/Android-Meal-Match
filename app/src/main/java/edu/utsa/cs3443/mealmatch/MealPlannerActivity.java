package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MealPlannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_planner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        tempNavigationHandle();
    }

    private void tempNavigationHandle(){
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MealPlannerActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MealPlannerActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MealPlannerActivity.this, GroceryListActivity.class));
            }
        });
    }
}