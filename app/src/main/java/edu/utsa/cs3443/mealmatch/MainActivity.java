package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
    ArrayList<Dish> dishData = DataManager.getInstance().getDishes();

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

//        for (Dish dish : DataManager.getInstance().getDishes()){
//            if (UserManager.getInstance().getUser().getFavoriteDishes().contains(dish.getID())){
//                //dishesList.add(dish);
//                Log.e("TAG", dish.toString());
//            }
//        }

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
        ArrayList<Dish> cloneArray = new ArrayList<>(dishData);
        Collections.shuffle(cloneArray);
        ArrayList<Dish> recommendDishes = new ArrayList<>(cloneArray.subList(0, 5));

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize Player List and Adapter
        dishAdapter = new RecommendDishAdapter(this, recommendDishes);
        recyclerView.setAdapter(dishAdapter);
    }
}