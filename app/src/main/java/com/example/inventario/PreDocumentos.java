package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PreDocumentos extends Fragment {

    public PreDocumentos() {

    }


    String isbnEn = null;

    EditText edEstado, edTitulo, edTema, edCategoria, edIsbn, edAutor, edIdioma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_documentos, container, false);

        edEstado = (EditText)view.findViewById(R.id.edEstado);
        edTitulo = (EditText)view.findViewById(R.id.edTitulo);
        edTema = (EditText)view.findViewById(R.id.edTema);
        edCategoria = (EditText)view.findViewById(R.id.edCategoria);
        edIsbn = (EditText)view.findViewById(R.id.edIsbn);
        edAutor = (EditText)view.findViewById(R.id.edAutor);
        edIdioma = (EditText)view.findViewById(R.id.edIdioma);

        cargarDatos();

        return view;
    }

    private void cargarDatos(){
        final String URL = "https://inventario-pdm115.000webhostapp.com/ws_prestamo_documentos.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {


                        JSONArray bdoc = new JSONArray(response);
                        Log.i("sizejson", "" + bdoc.length());

                        ArrayList<InventarioDocumentos> listB = new ArrayList<InventarioDocumentos>();
                        for (int i = 0; i < bdoc.length(); i += 13) {
                            try {

                                listB.add(new InventarioDocumentos(
                                        bdoc.getInt(0),
                                        bdoc.getInt(1),
                                        bdoc.getInt(2),
                                        bdoc.getInt(3),
                                        bdoc.getString(4),
                                        bdoc.getString(5),
                                        bdoc.getString(6),
                                        bdoc.getString(7)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        cargarCampos();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("campo", Documentos.getIsbnS());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cargarCampos(){

        Documentos docu = new Documentos();
        //docu =

/*
        edEstado.setText(documentos.getId_estado());
        edTitulo.setText(documentos.getTitulo());
        edTema.setText(documentos.getTema());
        edCategoria.setText(documentos.getId_categoria());
        edIsbn.setText(documentos.getIsbn());
        edAutor.setText(documentos.getAutor());
        edIdioma.setText(documentos.getId_idioma());

 */
    }

}
