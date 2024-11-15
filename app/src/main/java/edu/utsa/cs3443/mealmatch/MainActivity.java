package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import edu.utsa.cs3443.mealmatch.adapter.RecommendDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecommendDishAdapter dishAdapter;
    private ArrayList<Dish> showDishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        tempNavigationHandle();

        initializeRecommendDishes();

        searchBarHandler();

        setGreeting();

        logoutHandler();
    }

    private void logoutHandler(){
        ImageView btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener((v) -> {
            UserManager.getInstance().logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void tempNavigationHandle(){
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MealPlannerActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GroceryListActivity.class));
            }
        });
    }

    private void initializeRecommendDishes(){
        // Create a clone list, then shuffle, then pick random 5 dishes
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> cloneArray = new ArrayList<>(dishData);
        Collections.shuffle(cloneArray);
        showDishList = new ArrayList<>(cloneArray.subList(0, 5));

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize Player List and Adapter
        dishAdapter = new RecommendDishAdapter(this, showDishList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    private void searchBarHandler(){
        EditText txtSearch = findViewById(R.id.txt_search);

        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_term", searchTerm);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }

    private void setGreeting(){
        TextView txtGreeting = findViewById(R.id.txt_greeting);

        txtGreeting.setText("Welcome back, " + UserManager.getInstance().getUser().getFirstname());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data to reflect updated favorites from search
        dishAdapter.updateData(showDishList);
    }
}