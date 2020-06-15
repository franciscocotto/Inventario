package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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

    ArrayList<Documentos> documentos = new ArrayList<Documentos>();
    ArrayList<Docentes> docentes = new ArrayList<Docentes>();

    String[] docentesV;

    String isbnEn = null;

    EditText edEstado, edTitulo, edTema, edCategoria, edIsbn, edAutor, edIdioma;
    AutoCompleteTextView acDocentes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_documentos, container, false);

        edTitulo = (EditText)view.findViewById(R.id.edTitulo);
        edIsbn = (EditText)view.findViewById(R.id.edIsbn);
        acDocentes = (AutoCompleteTextView)view.findViewById(R.id.acDocentes);

        isbnEn = Documentos.getIsbnS();
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_prestamo_documentos.php", 1, isbnEn);
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_cargar_docentes.php", 2, " ");

        return view;
    }

    private void cargarDatos(String URL, final int accion, final String id){


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {

                        JSONArray bdoc = new JSONArray(response);
                        Log.i("sizejson", "" + bdoc.length());

                        switch (accion){

                            case 1: //Carga campos del documento
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
                                cargarCampos(listB);
                                break;

                            case 2:
                                ArrayList<Docentes> docentes = new ArrayList<Docentes>();
                                for (int i = 0;i<bdoc.length();i+=5){
                                    try {
                                        docentes.add(new Docentes(
                                                bdoc.getInt(i),
                                                bdoc.getString(i+1),
                                                bdoc.getString(i+2),
                                                bdoc.getString(i+3),
                                                bdoc.getString(i+4)));
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }

                                cargarDocentes(docentes);
                                break;

                        }

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
                parametros.put("campo", id);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cargarCampos(ArrayList list) {

        this.documentos = list;

        edTitulo.setText(documentos.get(0).getTitulo());
        edIsbn.setText(documentos.get(0).getIsbn());

    }

    public void cargarDocentes(ArrayList list){
        this.docentes = list;
        docentesV = new String[docentes.size()];

        for (int i = 0; i<docentes.size();i++){
            docentesV[i] = docentes.get(i).getPrimerNombre()+" "+docentes.get(i).getSegundoNombre()+" "+
                docentes.get(i).getPrimerApellido()+" "+docentes.get(i).getSegundoApellido();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, docentesV);
        acDocentes.setAdapter(adapter);

    }

}
