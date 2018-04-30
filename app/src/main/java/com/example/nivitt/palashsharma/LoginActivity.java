package com.example.nivitt.palashsharma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editText1,editText2;
    Button btn_lgn;
    String regno,pass;
    static String reg,stp,trk;
    TextView t;
    ProgressDialog progress;
    String URL_LOGIN = "https://palashsharma568.000webhostapp.com/PalashSharma/Login.php";
    String URL_SSN_UPDATE = "https://palashsharma568.000webhostapp.com/PalashSharma/upd_ssn.php";
    AlertDialog.Builder builder;

    static int randomIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this,R.style.myAlert);

        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        btn_lgn = findViewById(R.id.btn_lgn);
        t = findViewById(R.id.textView);
        builder = new AlertDialog.Builder(this,R.style.myAlert);

        btn_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                login();

            }
        });
    }
    void login(){
        regno = editText1.getText().toString();
        pass = editText2.getText().toString();

        if(regno.equals("")||pass.equals("")){
            builder.setTitle("Reg_No or Pass can't be empty");
            displayAlert("Enter a valid Reg_No and Pass...");
        }
        else {
            progress.setMessage("Logging In!");
            progress.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        //t.setText(code);
                        progress.dismiss();
                        if(code.equals("login_failed")){
                            builder.setTitle("Login Error");
                            displayAlert(jsonObject.getString("message"));
                        }
                        else{
                            reg = jsonObject.getString("regno");
                            stp = jsonObject.getString("stop_status");
                            trk = jsonObject.getString("track_status");

                            //Bundle bundle = new Bundle();

                            ssn_id();
                            Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                            //bundle.putString("reg",reg);
                            //intent.putExtras(bundle);
                            startActivity(intent);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(getWindow().getDecorView().getRootView() , "Network Error, Please try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("regno",regno);
                    params.put("pass",pass);
                    return params;
                }
            };
            Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
        }
    }

    void ssn_id(){
        int max = 300;
        List<Integer> indices = new ArrayList<Integer>(max);
        for(int c = 0; c < max; ++c)
        {
            indices.add(c);
        }
        int arrIndex = (int)((double)indices.size() * Math.random());
        randomIndex = indices.get(arrIndex);
        indices.remove(arrIndex);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SSN_UPDATE, new Response.Listener<String>() {
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
                params.put("regno",regno);
                params.put("session_id", String.valueOf(randomIndex));
                return params;
            }
        };;
        Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
    }

    public void displayAlert(String message){

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editText1.setText("");
                editText2.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
