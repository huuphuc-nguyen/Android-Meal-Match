package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import edu.utsa.cs3443.mealmatch.adapter.HorizontalDishAdapter;
import edu.utsa.cs3443.mealmatch.adapter.RecommendDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HorizontalDishAdapter dishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("search_term");

        setSearchDishes(searchTerm);
        setTitle(searchTerm);
    }
    private void setTitle(String searchTerm){
        TextView txtTitle = findViewById(R.id.txt_search_result_for);
        String title = "Search results for \"" + searchTerm + "\"";
        txtTitle.setText(title);
    }

    private void setSearchDishes(String searchTerm){
        // Create a clone list, then shuffle, then pick random 5 dihes
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>();

        for (Dish dish : dishData){
            if (dish.getName().toLowerCase().contains(searchTerm)){
                setList.add(dish);
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
                outRect.top = 10; // Top margin
                outRect.bottom = 10; // Bottom margin
            }
        });

        // Initialize Player List and Adapter
        dishAdapter = new HorizontalDishAdapter(this, setList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }
}