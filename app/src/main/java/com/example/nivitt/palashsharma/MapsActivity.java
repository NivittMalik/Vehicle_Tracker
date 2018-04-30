package com.example.nivitt.palashsharma;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.nivitt.palashsharma.LoginActivity.randomIndex;
import static com.example.nivitt.palashsharma.LoginActivity.reg;
import static com.example.nivitt.palashsharma.LoginActivity.stp;
import static com.example.nivitt.palashsharma.LoginActivity.trk;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();

    String URL_LOC = "https://palashsharma568.000webhostapp.com/PalashSharma/LOC.php";
    String URL_STOP = "https://palashsharma568.000webhostapp.com/PalashSharma/upd_stop.php";
    String URL_TRACK = "https://palashsharma568.000webhostapp.com/PalashSharma/upd_track.php";

    double lat,lon;
    String timestamp;
    FloatingActionButton f1,f2;
    boolean b1= false,b2=false;

    ArrayList<Markers> arrayList = new ArrayList<>();

    Handler mHandler;
    TextView textView2,textView3,textView4;
    StringRequest stringRequest;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        f1 = findViewById(R.id.floatingActionButton);
        f2 = findViewById(R.id.floatingActionButton2);
        Bundle bundle = getIntent().getExtras();
        //reg = bundle.getString("reg");
        //textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        //textView2.setText(reg+""+randomIndex);
        this.mHandler = new Handler();
        m_Runnable.run();
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Track your Vehicle on Map");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(trk.equals("1")){
            f1.setBackgroundTintList(ColorStateList.valueOf(0xFF64DD17));
            f1.setImageResource(R.drawable.caricon);
            //track_status("1");
            getData();
            b1=true;
            textView3.setText("Stop Tracking");
        }
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                vibe.vibrate(100);
                if(!b1){
                    f1.setBackgroundTintList(ColorStateList.valueOf(0xFF64DD17));
                    f1.setImageResource(R.drawable.caricon);
                    track_status("1");
                    getData();
                    b1=true;
                    textView3.setText("Stop Tracking");
                }
                else{
                    f1.setBackgroundTintList(ColorStateList.valueOf(0xFFD50000));
                    f1.setImageResource(R.drawable.caricon1);
                    track_status("0");
                    b1=false;
                    mMap.clear();
                    textView3.setText("Start Tracking");
                }


            }
        });
        if(stp.equals("1")){
            f2.setBackgroundTintList(ColorStateList.valueOf(0xFF64DD17));
            f2.setImageResource(R.drawable.startcar);
            textView4.setText("start Car");
            //stop_status("1");
            b2=true;
        }
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(!b2){
                    f2.setBackgroundTintList(ColorStateList.valueOf(0xFF64DD17));
                    f2.setImageResource(R.drawable.startcar);
                    textView4.setText("start Car");
                    stop_status("1");
                    b2=true;
                }
                else{
                    f2.setBackgroundTintList(ColorStateList.valueOf(0xFFD50000));
                    f2.setImageResource(R.drawable.stopicon);
                    textView4.setText("Stop Car");
                    stop_status("0");
                    b2=false;
                }
            }
        });
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(MapsActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            MapsActivity.this.mHandler.postDelayed(m_Runnable,10000);
            if(b1)
                getData();
        }
    };
    void getData(){
        stringRequest = new StringRequest(Request.Method.POST, URL_LOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   /* JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    //lat = Integer.parseInt(jsonObject1.getString("lat"));
                    lat = jsonObject1.getDouble("lat");
                    lon = jsonObject1.getDouble("long");
                    timestamp = jsonObject1.getString("timestamp");
                    LatLng sydney = new LatLng(lat, lon);
                    //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(sydney);
                    //mMap.moveCamera(cameraUpdate);
                    CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(sydney,15);
                    mMap.animateCamera(cameraUpdate1);
                    mMap.addMarker(new MarkerOptions().position(sydney).title(timestamp));
                    track_status();*/
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Markers markers = new Markers(
                                object.getDouble("lat"),
                                object.getDouble("long"),
                                object.getString("timestamp")
                        );
                        arrayList.add(markers);
                    }
                    for(Markers m :arrayList){
                        options.position(new LatLng(m.getLat(),m.getLon()));
                        options.title(m.getTimestamp());
                        mMap.addMarker(options);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(m.getLat(),m.getLon()),12);
                        mMap.moveCamera(cameraUpdate);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Slow Network", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("regno",reg);
                params.put("ssn_id", String.valueOf(randomIndex));
                return params;
            }
        };
        Volley.newRequestQueue(MapsActivity.this).add(stringRequest);
    }

    void stop_status(final String xyz){
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_STOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("regno",reg);
                params.put("stop_status",xyz);
                return params;
            }
        };;
        Volley.newRequestQueue(MapsActivity.this).add(stringRequest1);
    }

    void track_status(final String abc){
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL_TRACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("regno",reg);
                params.put("track_status",abc);
                return params;
            }
        };;
        Volley.newRequestQueue(MapsActivity.this).add(stringRequest2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu, menu);
        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.exit:

                Intent i = new Intent(MapsActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
        }
        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
     //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
