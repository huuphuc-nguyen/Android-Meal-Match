package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.adapter.HorizontalDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class FavoriteDishesActivity extends AppCompatActivity {
    ArrayList<Dish> favDishes;
    private RecyclerView recyclerView;
    private HorizontalDishAdapter dishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_dishes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        tempNavigationHandle();

        displayFavoriteDishes();
    }

    private void tempNavigationHandle(){
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavoriteDishesActivity.this, MainActivity.class));
            }
        });

        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavoriteDishesActivity.this, MealPlannerActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavoriteDishesActivity.this, GroceryListActivity.class));
            }
        });
    }

    private void displayFavoriteDishes(){
        favDishes = new ArrayList<>();

        for (int id : UserManager.getInstance().getUser().getFavoriteDishes()){
            Dish getDish = DataManager.getInstance().getDishById(id);
            favDishes.add(getDish);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add ItemDecoration for vertical spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10; // Top margin
                outRect.bottom = 10; // Bottom margin
            }
        });

        // Initialize Player List and Adapter
        dishAdapter = new HorizontalDishAdapter(this, favDishes, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

}