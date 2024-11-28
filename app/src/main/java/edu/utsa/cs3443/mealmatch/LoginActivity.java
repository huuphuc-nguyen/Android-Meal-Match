package edu.utsa.cs3443.mealmatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.groq.GroqApiClientImpl;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * LoginActivity handles the login screen for users to authenticate into the app.
 * It validates the user's credentials and allows navigation to either the main app or signup screen.
 *
 * @author Felix Nguyen
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. It sets up the layout, system bar insets,
     * loads data from DataManager, and configures the login and signup buttons.
     *
     * @param savedInstanceState If the activity is being reinitialized after being previously shut down, this contains the data it needs.
     */
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Apply system bar insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Load all data from DataManager (e.g., users, tasks, etc.)
        DataManager.getInstance().loadAllData(this);

        // Set up the login button click listener
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        // Set up the signup button click listener
        TextView txt_signup = findViewById(R.id.txt_signin);
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSingupActivity();
            }
        });
    }

    /**
     * Validates the user's email and password, attempting to log the user in.
     * If login is successful, the user is directed to the main activity.
     * If login fails, an error message is shown.
     */
    private void login() {
        EditText txt_email = findViewById(R.id.txt_email);
        EditText txt_password = findViewById(R.id.txt_password);

        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();

        // Attempt login using UserManager
        boolean isLoginSuccess = UserManager.getInstance().login(email, password);

        if (isLoginSuccess) {
            // If login successful, launch the main activity
            launchMainActivity();
        } else {
            // Show an error toast if login fails
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts the MainActivity, which serves as the landing page after successful login.
     */
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the SignupActivity, where new users can create an account.
     */
    private void launchSingupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
