package com.example.asus.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationListener locationListener;
    LocationManager locationManager;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startListening();

        }
    }


    public void startListening(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        }



    }

    public void updateLocation(Location location){
        Log.i("",location.toString());
        TextView t1=(TextView)findViewById(R.id.lat);
        TextView t2=(TextView)findViewById(R.id.lon);
        TextView t3=(TextView)findViewById(R.id.alt);
        TextView t4=(TextView)findViewById(R.id.ad);

        t1.setText("Lattitude : "+(Math.round(location.getLatitude())));
        t2.setText("Longitude : "+(Math.round(location.getLongitude())));
        t3.setText("Altitude  : "+(location.getAltitude()));


      //  Toast.makeText(this,"Accuracy is : " +location.getAccuracy()+"", Toast.LENGTH_SHORT).show();

        Geocoder geocoder=new Geocoder(getApplication(),Locale.getDefault());

       try {
           String address="";
           List<Address> add = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

           if(add.size()>0 && add!=null)
           {
               if(add.get(0).getFeatureName()!=null)
               {
                   address=address+add.get(0).getFeatureName()+" , ";
               }

               if(add.get(0).getLocality()!=null)
               {
                   address=address+add.get(0).getLocality()+" , ";
               }

               if(add.get(0).getPostalCode()!=null)
               {
                   address=address+add.get(0).getPostalCode()+" ";
               }

               if(add.get(0).getCountryName()!=null)
               {
                   address=address+add.get(0).getCountryName();
               }
               Log.i("address",address);
               t4.setText(address);
            //   Log.i("",address);
//               Toast.makeText(this,address, Toast.LENGTH_SHORT).show();
           }


       }
       catch (Exception e){}




    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {

            startListening();
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location location=locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if(location!=null)
                updateLocation(location);
            }



        }










    }




}
