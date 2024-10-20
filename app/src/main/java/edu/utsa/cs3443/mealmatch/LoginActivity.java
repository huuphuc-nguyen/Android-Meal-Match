package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
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

import edu.utsa.cs3443.mealmatch.utils.UserManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        TextView txt_signup = findViewById(R.id.txt_signin);

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSingupActivity();
            }
        });
    }

    private void login(){
        EditText txt_email = findViewById(R.id.txt_email);
        EditText txt_password = findViewById(R.id.txt_password);

        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();

        boolean isLoginSuccess = UserManager.getInstance().login(email,password);

        if(isLoginSuccess){
            launchMainActivity();
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchSingupActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}