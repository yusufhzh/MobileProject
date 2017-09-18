package com.example.ka20er.locationproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    double retLong;
    double retLat;
    private static final long MIN_DISTANCE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1;
    protected LocationManager locationManager;
    Location location;
    TextView tvLong;
    TextView tvLat;
    EditText etName;
    Button coordButton;
    Button syncButton;
    String latitude;
    String longitude;
    SimpleDateFormat simpleDateFormat;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.etName);
        tvLat = (TextView) findViewById(R.id.tvLatitude);
        tvLong = (TextView) findViewById(R.id.tvLongitude);
        coordButton = (Button) findViewById(R.id.btnCoord);
        syncButton = (Button) findViewById(R.id.btnSync);

        coordButton.setEnabled(false);
        syncButton.setEnabled(false);

        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etName.getText().toString().isEmpty()) {
                    coordButton.setEnabled(true);
                }
            }
        });

        coordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                latitude = String.format("%.6f", retLat);
                longitude = String.format("%.6f", retLong);

                tvLat.setText(latitude);
                tvLong.setText(longitude);
                Toast.makeText(getApplicationContext(), "Retrieving coordinates", Toast.LENGTH_SHORT).show();

                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy, hh-mm-ss");
                timeStamp = simpleDateFormat.format(new Date());
                Log.i("MainActivity", "Current Timestamp: " + timeStamp);

                syncButton.setEnabled(true);
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent todoIntent = new Intent(MainActivity.this, ToDoActivity.class);
                todoIntent.putExtra("passName", etName.getText().toString());
                todoIntent.putExtra("passLat", latitude);
                todoIntent.putExtra("passLong", longitude);
                todoIntent.putExtra("passCoordTime",timeStamp);
                startActivity(todoIntent);
            }
        });

    }

    public void getLocation() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    retLat = lastKnownLocation.getLatitude();
                    retLong = lastKnownLocation.getLongitude();
                }
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    retLat = lastKnownLocation.getLatitude();
                    retLong = lastKnownLocation.getLongitude();
                }
            };
        }
    }
}


