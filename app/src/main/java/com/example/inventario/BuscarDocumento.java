package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class BuscarDocumento extends Fragment {

    public BuscarDocumento() {
        // Required empty public constructor
    }

    EditText etBuscar;
    Button btnBuscar;
    TableLayout tlLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_documento, container, false);

        etBuscar = (EditText)view.findViewById(R.id.etBuscar);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        tlLista = (TableLayout)view.findViewById(R.id.tlLista);
    }
}
