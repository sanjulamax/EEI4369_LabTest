package com.example.s23010275;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameEditText, fullNameEditText, passwordEditText;
    dataBase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize the database helper
        databaseHelper = new dataBase(this);

        // Initialize the input fields
        usernameEditText = findViewById(R.id.textInputLayout).findViewById(R.id.uname);
        fullNameEditText = findViewById(R.id.textInputLayout2).findViewById(R.id.fname);
        passwordEditText = findViewById(R.id.editTextTextPassword);


        // Register button click listener
        Button registerButton = findViewById(R.id.button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        // Get user input
        String username = usernameEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data to the database
        long result = databaseHelper.insertUser(username, fullName, password);
        if (result != -1) {
            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
            Log.d("RegisterActivity", "User registration process completed");

            // Redirect to another activity (MainActivity)
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
        }
    }
}