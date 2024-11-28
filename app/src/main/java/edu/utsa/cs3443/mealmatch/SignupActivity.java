package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * SignupActivity handles the user sign-up process. It collects the user's personal information,
 * validates the input, and creates a new user account if the input is valid.
 *
 * @author Felix Nguyen
 */
public class SignupActivity extends AppCompatActivity {

    /**
     * Initializes the activity, sets up the UI components, and handles the sign-up button click event.
     *
     * @param savedInstanceState The saved instance state, if the activity is being recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Apply system bar insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Initialize the sign-up button and set up the click listener
        Button btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(view -> signup());
    }

    /**
     * Handles the sign-up process. It validates the user input and either creates a new account
     * or shows an error message based on the validation results.
     */
    private void signup() {
        // Get user input from the form fields
        EditText txtFirstName = findViewById(R.id.txt_firstname);
        EditText txtLastName = findViewById(R.id.txt_lastname);
        EditText txtEmail = findViewById(R.id.txt_email);
        EditText txtPassword = findViewById(R.id.txt_password);
        EditText txtConfirmPassword = findViewById(R.id.txt_confirm_password);

        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "ConfirmPassword must match Password", Toast.LENGTH_SHORT).show();
        }
        // Check if any fields are empty
        else if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            // Attempt to create a new user using the UserManager class
            if (UserManager.getInstance().addNewUser(email, password, firstName, lastName, this)) {
                Toast.makeText(getApplicationContext(), "Account created successfully.", Toast.LENGTH_SHORT).show();
                launchLoginActivity();
            }
            // Show error if the email already exists
            else {
                Toast.makeText(getApplicationContext(), "Email already exists.", Toast.LENGTH_SHORT).show();
                txtEmail.requestFocus();
            }
        }
    }

    /**
     * Launches the LoginActivity, allowing the user to log in with the newly created account.
     */
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}