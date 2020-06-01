package com.example.inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        Info.setText("Intentos Restantes: 5");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Usuario.getText().toString(), Password.getText().toString());
            }
        });

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
