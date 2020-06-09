package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ConDocumentos extends Fragment {

    private EditText ejemplo;
    private  Button btnEjem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_con_documentos, container, false);

       btnEjem = (Button) v.findViewById(R.id.btnEjemplo);

       v.findViewById(R.id.btnEjemplo).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });

        return v;
    }





}
