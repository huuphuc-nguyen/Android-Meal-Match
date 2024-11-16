package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

        // Initialize navigation buttons
        tempNavigationHandle();

        // Initialize search bar functionality
        searchBarHandler();

        // Display favorite dishes after the layout is set up
        displayFavoriteDishes();
    }

    private void searchBarHandler() {
        EditText txtSearch = findViewById(R.id.txt_search);

        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    // Pass the search term to displayFilteredFavoriteDishes method
                    displayFilteredFavoriteDishes(searchTerm);
                }
                return true;
            }
            return false;
        });
    }

    private void displayFavoriteDishes() {
        favDishes = new ArrayList<>();

        // Assuming that the user has a list of favorite dish IDs stored in UserManager
        for (int id : UserManager.getInstance().getUser().getFavoriteDishes()) {
            // Fetch the dish using its ID from DataManager
            Dish getDish = DataManager.getInstance().getDishById(id);
            if (getDish != null) {
                favDishes.add(getDish);
            }
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add ItemDecoration for vertical spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;  // Top margin
                outRect.bottom = 10;  // Bottom margin
            }
        });

        // Initialize Adapter and set it to the RecyclerView
        dishAdapter = new HorizontalDishAdapter(this, favDishes, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    // New method to filter the displayed favorite dishes based on the search term
    private void displayFilteredFavoriteDishes(String searchTerm) {
        favDishes = new ArrayList<>();

        // Fetch the user's favorite dish IDs
        ArrayList<Integer> favoriteDishIds = new ArrayList<>(UserManager.getInstance().getUser().getFavoriteDishes());

        // Filter the dishes that match the search term and are fully hearted (favorited)
        for (int id : favoriteDishIds) {
            Dish dish = DataManager.getInstance().getDishById(id);
            if (dish != null && dish.getName().toLowerCase().contains(searchTerm)) {
                favDishes.add(dish);
            }
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add ItemDecoration for vertical spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;  // Top margin
                outRect.bottom = 10;  // Bottom margin
            }
        });

        // Initialize Adapter and set it to the RecyclerView
        dishAdapter = new HorizontalDishAdapter(this, favDishes, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    private void tempNavigationHandle() {
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_home.setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, MainActivity.class)));

        btn_plan.setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, MealPlannerActivity.class)));

        btn_list.setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, GroceryListActivity.class)));
    }
}