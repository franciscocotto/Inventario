package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class PreDocumentos extends Fragment {

    public PreDocumentos() {

    }

    String isbnEn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pre_documentos, container, false);

        //Tu contenido

        isbnEn = Documentos.getIsbnS();
        Toast.makeText(getActivity().getApplicationContext(), isbnEn, Toast.LENGTH_LONG).show();



        return view;
    }

}
