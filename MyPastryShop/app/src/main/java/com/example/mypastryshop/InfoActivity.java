package com.example.mypastryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import john.net.DownLoader;

public class InfoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    final static int REQUEST_CODE = 777;

    EditText address;
    double lat;
    double lng;

    /*
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        address = findViewById(R.id.addressToFind);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Log.i("PPPPPPPP1111111111", location.toString());

                        //double lon = location.getLongitude();
                        //double lat = location.getLatitude();
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(we));
                        // mMap.moveCamera(zoom);
//                        setMapMeta(location, 19, R.drawable.start);


                    }
                }
        );
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (
                    (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] != PackageManager.PERMISSION_DENIED)
                            ||
                            (permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[1] != PackageManager.PERMISSION_DENIED)
            ) {
                Toast.makeText(this, "使用者不同意權限!!", Toast.LENGTH_LONG).show();
                return;
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        10,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                Log.i("PPPPPPPP22222222", location.toString());

                                //double lon = location.getLongitude();
                                //double lat = location.getLatitude();

                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(we));
                                // mMap.moveCamera(zoom);
                                //mMap.animateCamera(zoom);
//                                setMapMeta(location, 19, R.drawable.start);
                            }
                        }
                );
            }

        }
    }

    private void  setMapMeta(Location location, int zoom, int icon_id){

        MarkerOptions option = new MarkerOptions();
        option.title("媽!我在這");
        BitmapDescriptor vector = BitmapDescriptorFactory.fromResource(icon_id);
        option.icon(vector);
        option.position(new LatLng(location.getLatitude(), location.getLongitude()));
        final Marker start_point = mMap.addMarker(option);




        new Thread(){
            float r = 0;
            @Override
            public void run() {
                super.run();
                for(int i=0;i<100;i++){
                    InfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("kk","123");
                            start_point.setRotation(r);
                        }
                    });
                    r = r + 10;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }.start();


        CameraUpdate go = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 19);
        mMap.animateCamera(go);

    }

    private void  setMapMeta(double latitude,double longitude, int zoom, int icon_id){

        MarkerOptions option = new MarkerOptions();
        option.title("媽!我在這");
        BitmapDescriptor vector = BitmapDescriptorFactory.fromResource(icon_id);
        option.icon(vector);
        option.position(new LatLng(latitude, longitude));
        final Marker start_point = mMap.addMarker(option);




        new Thread(){
            float r = 0;
            @Override
            public void run() {
                super.run();
                for(int i=0;i<100;i++){
                    InfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("kk","123");
                            start_point.setRotation(r);
                        }
                    });
                    r = r + 10;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }.start();


        CameraUpdate go = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 19);
        mMap.animateCamera(go);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override//一開始繼承實作的類別
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);//哪種類型的地圖
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng we = new LatLng(24.930726, 121.171998);//設定定位地點
        mMap.addMarker(new MarkerOptions().position(we).title("Marker in Sydney"));//游標一過去會看到的字
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(we, 19);//從多高看下來
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(we));
        mMap.moveCamera(zoom);

    }

    public void GoMap(View v) throws UnsupportedEncodingException {

        if(v.getId() == R.id.buttonGo){

            Log.i("OKOKOKOKOK", "111111");
            String xxyy = address.getText().toString();
            xxyy = URLEncoder.encode(xxyy, "UTF-8");
            DownLoader download = new DownLoader(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=" + xxyy + "&key=AIzaSyBvCygD7NttQRRc1xWzJ4Af20mhIi_y-s4",
                    "/data/data/idv.john.mymap/geo_data.txt"
            );
            Log.i("OKOKOKOKOK", "22222");
            download.noter = new DownLoader.OKListener() {
                @Override
                public void complete() {
                    Log.i("OKOKOKOKOK", "COMPLETE");
                }

                @Override
                public void alreadyHave() {
                    Log.i("OKOKOKOKOK", "GO　CHECK SANDBOX");
                }
            };
            Log.i("OKOKOKOKOK", "33333");
            download.start();
        }else if(v.getId() == R.id.buttonStreetView){


            Log.i("OKOKOKOKOK", "111111");
            String xxyy = address.getText().toString();
            xxyy = URLEncoder.encode(xxyy, "UTF-8");
            final DownLoader download = new DownLoader(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=" + xxyy + "&key=AIzaSyBvCygD7NttQRRc1xWzJ4Af20mhIi_y-s4"
            );
            Log.i("OKOKOKOKOK", "22222");
            download.noter = new DownLoader.OKListener() {
                @Override
                public void complete() {
                    Log.i("OKOKOKOKOK", "COMPLETE");
                    Log.i("OKOKOKOKOK", download.result);
                    try {
                        JSONObject jsonObject = new JSONObject(download.result);
                        String ok_or_not = (String)jsonObject.get("status");
                        Log.i("OKOKOKOKOK", ok_or_not);
                        if(ok_or_not.equals("OK")){

                            JSONArray arr = (JSONArray)jsonObject.get("results");
                            JSONObject jsonObject2 = (JSONObject)arr.get(0);
                            JSONObject geo = (JSONObject)jsonObject2.get("geometry");
                            JSONObject lo = (JSONObject)geo.get("location");
                            lat = (double)lo.get("lat");
                            lng = (double)lo.get("lng");
                            Log.i("OKOKOKOKOK", "經度是:" + lng + " 緯度是:" + lat);
                            new  Thread( new  Runnable() {

                                @Override
                                public  void  run() {

                                    //延迟两秒
                                    try  {
                                        Thread.sleep(  2000  );
                                    }  catch  (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    runOnUiThread( new  Runnable() {
                                        @Override
                                        public  void  run() {
                                            setMapMeta(lat,lng, 19, R.drawable.start);
                                        }
                                    });

                                }
                            }).start();
                        }else{

                        }

                    }catch (JSONException err){
                        Log.d("Error", err.toString());
                    }

                }

                @Override
                public void alreadyHave() {
                    Log.i("OKOKOKOKOK", "GO　CHECK SANDBOX");
                }
            };
            Log.i("OKOKOKOKOK", "33333");
            download.start();




        }else if(v.getId() == R.id.buttonOriginal){
            Intent go = new Intent(InfoActivity.this, WebViewActivity.class);
            InfoActivity.this.startActivity(go);
        }

    }
}