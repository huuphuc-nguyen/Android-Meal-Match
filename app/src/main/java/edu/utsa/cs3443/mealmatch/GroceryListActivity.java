package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.utsa.cs3443.mealmatch.adapter.IngredientAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.groq.GroqApiClientImpl;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.utils.Constant;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class GroceryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grocery_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });


        displayTasks();

        addTask();
        tempNavigationHandle();


    }

    // Navigation buttons
    private void tempNavigationHandle(){
        ImageButton btn_home = findViewById(R.id.btn_home);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);

        // Navigate to home
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroceryListActivity.this, MainActivity.class));
            }
        });

        // Navigate to meal plan
        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroceryListActivity.this, MealPlannerActivity.class));
            }
        });

        // Navigate to favorite dishes
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroceryListActivity.this, FavoriteDishesActivity.class));
            }
        });
    }

    private void addTask() {
        EditText txt_taskname = findViewById(R.id.task_name);
        EditText txt_tasktype = findViewById(R.id.task_type);
        Button add_item = findViewById(R.id.btn_add_item);

        add_item.setOnClickListener(view -> {
            String taskName = txt_taskname.getText().toString().trim();
            String taskType = txt_tasktype.getText().toString().trim();

            if (!taskName.isEmpty() && !taskType.isEmpty()) {
                boolean isAlreadyInGroceryList = false;
                int existedTaskID = 0;

                for (Task task : DataManager.getInstance().getTasks().values()) {
                    if (task.getName().equals(taskName) && task.getType().equals(taskType)) {
                        isAlreadyInGroceryList = true;
                        existedTaskID = task.getID();
                        break;
                    }
                }

                if (!isAlreadyInGroceryList) {
                    Task newTask = new Task(DataManager.getInstance().getNextTaskID(), taskName, taskType, false);
                    DataManager.getInstance().addTask(newTask, this);

                    GroceryList userGroceryList = DataManager.getInstance().getGroceryListById(UserManager.getInstance().getUser().getGroceryID());
                    userGroceryList.addTask(newTask);

                    DataManager.getInstance().updateGroceryList(this);
                } else {
                    Task checkTask = DataManager.getInstance().getTaskById(existedTaskID);
                    checkTask.setDone(false);
                    DataManager.getInstance().updateTask(this);
                }

                txt_taskname.setText("");
                txt_tasktype.setText("");
                displayTasks();
            } else {
                Toast.makeText(this, "Please enter both task name and type.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTasks() {
        LinearLayout taskList = findViewById(R.id.task_list);
        taskList.removeAllViews(); // Clear any existing views

        GroceryList userGList = DataManager.getInstance().getGroceryListById(UserManager.getInstance().getUser().getGroceryID());
        ArrayList<Integer> taskIDs = userGList.getTasks();

        Map<Integer, Task> tasks = DataManager.getInstance().getTasks();
        Log.d("GroceryListActivity", "Number of tasks: " + tasks.size());

        for (int taskID : taskIDs) {
            Task task = tasks.get(taskID);
            if (task != null) {
                // Create a CardView for each task
                androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(this);
                LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardLayoutParams.setMargins(16, 16, 16, 16);
                cardView.setLayoutParams(cardLayoutParams);
                cardView.setRadius(16);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.background_primary));
                cardView.setContentPadding(16, 16, 16, 16);
                cardView.setMaxCardElevation(8);
                cardView.setCardElevation(4);

                // Create a LinearLayout to hold the task details and delete button
                LinearLayout taskLayout = new LinearLayout(this);
                taskLayout.setOrientation(LinearLayout.HORIZONTAL);
                taskLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Create a CheckBox for the task
                CheckBox taskCheckBox = new CheckBox(this);
                taskCheckBox.setChecked(task.isDone());
                taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    task.setDone(isChecked);
                    DataManager.getInstance().updateTask(this);
                });

                // Create a TextView for the task details
                TextView taskView = new TextView(this);
                taskView.setText(task.getName() + " - " + task.getType());
                taskView.setTextSize(18);
                taskView.setTextColor(getResources().getColor(R.color.emphasize_dark));
                taskView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                // Create an ImageButton for deleting the task
                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setImageResource(R.drawable.ic_trash);
                deleteButton.setBackgroundColor(Color.TRANSPARENT);
                deleteButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(100, 100); // Set width and height
                deleteButton.setLayoutParams(buttonLayoutParams);
                deleteButton.setOnClickListener(view -> {
                    userGList.getTasks().remove(Integer.valueOf(taskID));
                    DataManager.getInstance().removeTask(taskID, this);
                    displayTasks();
                });

                // Add the CheckBox, TextView, and ImageButton to the LinearLayout
                taskLayout.addView(taskCheckBox);
                taskLayout.addView(taskView);
                taskLayout.addView(deleteButton);

                // Add the LinearLayout to the CardView
                cardView.addView(taskLayout);

                // Add the CardView to the task list
                taskList.addView(cardView);
            }
        }
    }




}