package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import edu.utsa.cs3443.mealmatch.adapter.HorizontalDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class AddMealActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HorizontalDishAdapter dishAdapter;
    private ArrayList<Dish> showDishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_meal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });


        Intent intent = getIntent();
        setGreeting();
        tempNavigationHandle();
        recommendDishes();
        searchBarHandler();
    }

    private void searchBarHandler(){
        EditText txtSearch = findViewById(R.id.txt_search);

        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    Intent intent = new Intent(AddMealActivity.this, AddMealSearchActivity.class);
                    intent.putExtra("search_term", searchTerm);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }

    private void recommendDishes(){
        // Create a clone list, then shuffle, then pick random 5 dihes
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>(dishData);
        Collections.shuffle(setList);
        showDishList = new ArrayList<>(setList.subList(0, 5));

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
        dishAdapter = new HorizontalDishAdapter(this, showDishList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    private void setGreeting(){
        TextView txtGreeting = findViewById(R.id.txt_greeting);
        //" + UserManager.getInstance().getUser().getFirstname() + "
        txtGreeting.setText("Hello ,\n here are meals to add");
    }



    private void tempNavigationHandle(){
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMealActivity.this, MainActivity.class));
            }
        });

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMealActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMealActivity.this, GroceryListActivity.class));
            }
        });
    }
}



