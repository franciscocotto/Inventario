package com.example.inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText Usuario;
    private EditText Password;
    private TextView  Info;
    private Button  Login;
    private int counter = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuario = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // validate(Usuario.getText().toString(), Password.getText().toString());
                login();
            }
        });


    }
    private void login(){

        String url = "https://invetariopdm115.000webhostapp.com//login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){

                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("UserName", Usuario.getText().toString().trim());
                params.put("Password", Password.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void validate(String userName, String userPassword){
        if(userName.equals("Admin")  && (userPassword.equals("1234") )){
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }else {
            counter--;
            Info.setText("Intentos Faltantes: " + String.valueOf(counter));
                if(counter==0){
                    Login.setEnabled(false);
                }
        }
    }
}
