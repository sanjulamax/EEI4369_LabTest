package com.example.s23010275;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText usernameEditText;
    EditText passwordEditText;
    dataBase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        databaseHelper = new dataBase(this);

        // Initialize input fields
        usernameEditText = findViewById(R.id.textInputLayout).findViewById(R.id.uname);
        passwordEditText = findViewById(R.id.editTextTextPassword);



        // Login button click listener
        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Redirect to RegisterActivity
        TextView goToRegister = findViewById(R.id.go_to_reg);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        // Get user input
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials
        boolean isValid = databaseHelper.verifyUser(username, password);
        if (isValid) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "User logged in successfully");

            // Redirect to another activity (MapViewer)
            Intent intent = new Intent(MainActivity.this, MapViewer.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "Login failed. Incorrect username or password.");
        }
    }
}