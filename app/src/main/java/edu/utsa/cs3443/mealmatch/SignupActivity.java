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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        Button btn_signup = findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void signup(){
        // Get user input
        EditText txt_firstname = findViewById(R.id.txt_firstname);
        EditText txt_lastname = findViewById(R.id.txt_lastname);
        EditText txt_email = findViewById(R.id.txt_email);
        EditText txt_password = findViewById(R.id.txt_password);
        EditText txt_confirm_password = findViewById(R.id.txt_confirm_password);

        String firstname = txt_firstname.getText().toString().trim();
        String lastname = txt_lastname.getText().toString().trim();
        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String confirmPassword = txt_confirm_password.getText().toString().trim();

        if (!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(),"ConfirmPassword must match Password", Toast.LENGTH_SHORT).show();
        } else if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Fields can not be empty", Toast.LENGTH_SHORT).show();
        } else{
            UserManager.getInstance().addNewUser(email, password, firstname, lastname);
            Toast.makeText(getApplicationContext(),"Create account successfully.", Toast.LENGTH_SHORT).show();
            launchLoginActivity();
        }
    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}