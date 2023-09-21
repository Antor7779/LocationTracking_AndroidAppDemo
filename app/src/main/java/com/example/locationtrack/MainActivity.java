package com.example.locationtrack;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView locationTextView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = findViewById(R.id.locationTextView);

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are granted, start location updates
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Initialize LocationListener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method will be called when the location is updated
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Update the locationTextView with the received location data
                locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
            }

            // Implement other LocationListener methods if needed
            // onProviderEnabled, onProviderDisabled, onStatusChanged
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, // Use GPS for more accurate location
                    0, // Minimum time interval between updates in milliseconds (0 for immediate updates)
                    0, // Minimum distance between updates in meters (0 for immediate updates)
                    locationListener
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, start location updates
                startLocationUpdates();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop location updates when the activity is destroyed
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
