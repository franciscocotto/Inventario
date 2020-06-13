package com.example.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BuscarDocumento extends Fragment {

    public BuscarDocumento() {
        // Required empty public constructor
    }

    EditText etBuscar;
    Button btnBuscar;
    //TableLayout tlLista;
    ListView lista;
    ArrayAdapter adapter = null;
    String[] titulos = new String[0];

    private ArrayList<Documentos> documentos = new ArrayList<Documentos>();

    int contador = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_documento, container, false);

        etBuscar = (EditText)view.findViewById(R.id.edtBuscar);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        lista = (ListView)view.findViewById(R.id.lvLibros);
        obtenerLibros("https://inventario-pdm115.000webhostapp.com/ws_consulta_documentos.php");

        //Cargar datos


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cargarTabla();
            }
        });

        return view;

    }

    public void obtenerLibros(final String URL){

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    response = response.replace("][",",");
                    if(response.length()>0){
                        try{
                            JSONArray doc = new JSONArray(response);
                            Log.i("sizejson",""+doc.length());

                            documentos.clear();
                            for(int i = 0;i<doc.length(); i+=13){
                                try{
                                    documentos.add(new Documentos(
                                            doc.getInt(i+1),
                                            doc.getInt(i+7),
                                            doc.getInt(i+12),
                                            doc.getString(i+2),
                                            doc.getString(i+3),
                                            doc.getString(i+4),
                                            doc.getString(i+5),
                                            doc.getString(i+6),
                                            doc.getString(i+8),
                                            doc.getString(i+10),
                                            doc.getString(i+11),
                                            doc.getString(i+9)));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                            cargarTabla();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }

    public void cargarTabla(){

        titulos = new String[documentos.size()];

        for (int i=0; i<documentos.size();i++){
            titulos[i] = documentos.get(i).getTitulo().toString();
        }

        adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
        lista.setAdapter(adapter);

    }

}
