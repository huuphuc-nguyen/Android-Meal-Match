package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import edu.utsa.cs3443.mealmatch.adapter.AddMealAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class MealPlannerActivity extends AppCompatActivity {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

    private RecyclerView recyclerView;
    private AddMealAdapter dishAdapter;
    private Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_planner);

        setupEdgeToEdgePadding();
        setupRecyclerView();
        setGreeting();
        setSelectedDate();
        setupDateNavigation();
        setupNavigationButtons();
        setupAddMealButton();
    }

    private void setupEdgeToEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    private MealPlan getCurrentMealPlan(Date currentDate) {
        for (MealPlan mealPlan : DataManager.getInstance().getMealPlans().values()) {
            if (dateFormat.format(currentDate).equals(dateFormat.format(mealPlan.getPlanDate()))
                    && UserManager.getInstance().getUser().getMealPlans().contains(mealPlan.getID())) {
                return mealPlan;
            }
        }
        return null;
    }

    private void setupAddMealButton() {
        AppCompatButton addMealBtn = findViewById(R.id.btn_add_meal);

        addMealBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MealPlannerActivity.this, AddMealActivity.class);
            MealPlan mealPlan = getCurrentMealPlan(currentDate);
            int id = 0;
            if (mealPlan == null){
                id = DataManager.getInstance().getNextMealPlanID();
                UserManager.getInstance().getUser().getMealPlans().add(id);
                DataManager.getInstance().updateUser(this);

                MealPlan newMeal = new MealPlan(id, currentDate, new ArrayList<>());
                DataManager.getInstance().getMealPlans().put(id, newMeal);
            }
            else {
                id = mealPlan.getID();
            }
            intent.putExtra("meal_id", id);
            startActivity(intent);
        });
    }

    private void displayMealPlanDishes(MealPlan mealPlan) {
        ArrayList<Dish> mealPlanDishes = new ArrayList<>();
        if (mealPlan != null) {
            for (int id : mealPlan.getDishes()) {
                Dish dish = DataManager.getInstance().getDishById(id);
                if (dish != null) {
                    mealPlanDishes.add(dish);
                }
            }
        }

        updateDisplayDisshes(mealPlanDishes, mealPlan);
    }

    private void setupRecyclerView() {
        MealPlan mealPlan = getCurrentMealPlan(currentDate);
        ArrayList<Dish> dishes = new ArrayList<>();
        if (mealPlan != null) {
            for (int id : mealPlan.getDishes()) {
                Dish dish = DataManager.getInstance().getDishById(id);
                if (dish != null) {
                    dishes.add(dish);
                }
            }
        }

        if (recyclerView == null) {
            // Initialize RecyclerView
            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            // Add ItemDecoration for vertical spacing (only once)
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.top = 10; // Top margin
                    outRect.bottom = 10; // Bottom margin
                }
            });
        }

        // Initialize Adapter and set it to the RecyclerView
        int id = 0;
        if (mealPlan == null){
            id = DataManager.getInstance().getNextMealPlanID();
            UserManager.getInstance().getUser().getMealPlans().add(id);
            DataManager.getInstance().updateUser(this);

            MealPlan newMeal = new MealPlan(id, currentDate, new ArrayList<>());
            DataManager.getInstance().getMealPlans().put(id, newMeal);
        }
        else {
            id = mealPlan.getID();
        }
        dishAdapter = new AddMealAdapter(this, dishes, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        }, id);
        recyclerView.setAdapter(dishAdapter);
    }

    private void updateDisplayDisshes(ArrayList<Dish> dishes, MealPlan mealPlan){
        dishAdapter.updateDishes(dishes, mealPlan);
    }

    private void setupDateNavigation() {
        ImageButton btnNextDate = findViewById(R.id.btn_next_date);
        ImageButton btnPriorDate = findViewById(R.id.btn_prior_date);

        btnPriorDate.setOnClickListener(view -> {
            updateCurrentDate(-1);
            displayMealPlanDishes(getCurrentMealPlan(currentDate));
        });

        btnNextDate.setOnClickListener(view -> {
            updateCurrentDate(1);
            displayMealPlanDishes(getCurrentMealPlan(currentDate));
        });
    }

    private void updateCurrentDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        currentDate = calendar.getTime();
        updateDateDisplay();
    }

    private void updateDateDisplay() {
        TextView dateTextView = findViewById(R.id.txt_date);
        dateTextView.setText(displayDateFormat.format(currentDate));
    }

    private void setGreeting() {
        TextView txtGreeting = findViewById(R.id.txt_greeting);
        txtGreeting.setText("Hello " + UserManager.getInstance().getUser().getFirstname() + ",\n here's your meal plan for:");
    }

    private void setSelectedDate() {
        TextView selectedDate = findViewById(R.id.txt_date);
        selectedDate.setText(displayDateFormat.format(currentDate));
    }

    private void setupNavigationButtons() {
        findViewById(R.id.btn_home).setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.btn_favoriteDish).setOnClickListener(view -> startActivity(new Intent(this, FavoriteDishesActivity.class)));
        findViewById(R.id.btn_groceryList).setOnClickListener(view -> startActivity(new Intent(this, GroceryListActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayMealPlanDishes(getCurrentMealPlan(currentDate));
    }
}
