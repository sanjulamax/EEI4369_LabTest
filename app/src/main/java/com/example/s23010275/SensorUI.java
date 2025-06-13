package com.example.s23010275;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorUI extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView temperatureTextView;

    private boolean isTemperatureSensorAvailable;
    private float currentTemperature = 0.0f; // Store the current temperature
    private final float TEMPERATURE_THRESHOLD = 75.0f; //  threshold in Celsius

    private MediaPlayer mediaPlayer; // Declare MediaPlayer instanceSAVINDU
    private boolean isSoundPlaying = false; // Flag to prevent sound from playing repeatedly



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor_ui);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mediaPlayer = MediaPlayer.create(this, R.raw.warning);
        // Set up a completion listener to reset the flag when sound finishes
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isSoundPlaying = false;

                }
            });
        } else {
            Toast.makeText(this, "Error initializing media player.", Toast.LENGTH_SHORT).show();
        }


        // Try to get the ambient temperature sensor
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
            Toast.makeText(this, "Ambient Temperature Sensor found!", Toast.LENGTH_SHORT).show();
        } else {
            // If the sensor is not available, set a default message
            temperatureTextView.setText("Ambient Temperature Sensor: Not Available");
            isTemperatureSensorAvailable = false;
            Toast.makeText(this, "Ambient Temperature Sensor not available.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listener only if the sensor is available
        if (isTemperatureSensorAvailable && temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listener to save battery when the app is not in the foreground
        if (isTemperatureSensorAvailable && temperatureSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This method is called when sensor values change.
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            currentTemperature = event.values[0];
            temperatureTextView.setText("Temperature: " + currentTemperature + " °C");

            // Check if temperature exceeds the threshold
            if (currentTemperature > TEMPERATURE_THRESHOLD) {
                playSoundAlert(); // Play sound alert
                showTemperatureAlertPopup(currentTemperature); // Show popup alert
            }
            else {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    mediaPlayer.seekTo(0); // Reset to the beginning
                    isSoundPlaying = false;
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showTemperatureAlertPopup(float temp) {
        // Create and show an alert dialog with the temperature information

        new AlertDialog.Builder(this)
                .setTitle("Temperature Alert!")
                .setMessage("The device temperature (" + temp + " °C) has exceeded the threshold of " + TEMPERATURE_THRESHOLD + " °C.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Simple alert icon
                .show();
    }

    private void playSoundAlert() {
        // Play sound only if it's not already playing and MediaPlayer is available
        if (mediaPlayer != null && !isSoundPlaying && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Start playing the sound
            isSoundPlaying = true; // Set flag to true
            Toast.makeText(this, "Temperature high! Playing sound.", Toast.LENGTH_SHORT).show();
        } else if (mediaPlayer == null) {
            Toast.makeText(this, "MediaPlayer not initialized.", Toast.LENGTH_SHORT).show();
        }
    }


}