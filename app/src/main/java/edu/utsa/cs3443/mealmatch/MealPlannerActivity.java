package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class MealPlannerActivity extends AppCompatActivity {

    private Date currentDate = new Date();
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


        setSelectedDate();
        addMealBtn();
        traverseDates();
        setGreeting();
        tempNavigationHandle();
    }

    private void addMealBtn() {
        AppCompatButton addMeal_Btn = findViewById(R.id.btn_add_meal);
        addMeal_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealPlannerActivity.this, AddMealActivity.class);
                intent.putExtra("selected_date", currentDate);
                startActivity(intent);
            }
        });
    }

    private void traverseDates() {
        ImageButton btn_nextDate = findViewById(R.id.btn_next_date);
        ImageButton btn_priorDate = findViewById(R.id.btn_prior_date);

        btn_priorDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                currentDate = calendar.getTime();
                updateDateDisplay();
            }
        });

        btn_nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                currentDate = calendar.getTime();
                updateDateDisplay();
            }
        });
    }

    private void updateDateDisplay() {
        TextView dateTextView = findViewById(R.id.txt_date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        dateTextView.setText(formatter.format(currentDate));
    }

    private void setGreeting(){
        TextView txtGreeting = findViewById(R.id.txt_greeting);

        txtGreeting.setText("Hello " + UserManager.getInstance().getUser().getFirstname() + ", here's your meal plan for");
    }

    private void setSelectedDate() {
        TextView selectedDate = findViewById(R.id.txt_date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault());
        String date = formatter.format(currentDate);
        selectedDate.setText(date);
    }
    private void tempNavigationHandle(){
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MealPlannerActivity.this, MainActivity.class));
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