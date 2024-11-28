package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * SplashScreen displays a splash screen for a brief duration before navigating to the LoginActivity.
 * This is typically used for branding, loading, or preparing app resources.
 *
 * @author Felix Nguyen
 */
public class SplashScreen extends AppCompatActivity {

    /**
     * Called when the activity is created. It sets up the splash screen and schedules a task
     * to transition to the LoginActivity after a brief delay.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state.
     *                           If the activity is being recreated, this will contain the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable edge-to-edge display

        // Use a Handler to create a delay of 2 seconds before transitioning to the login screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the LoginActivity after the delay
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                // Close the SplashScreen activity to prevent it from being accessed back
                finish();
            }
        }, 2000); // Delay of 2000 milliseconds (2 seconds)
    }
}