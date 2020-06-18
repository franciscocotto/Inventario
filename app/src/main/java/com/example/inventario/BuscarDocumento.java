package com.example.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarDocumento extends Fragment {

    public BuscarDocumento() {
        // Required empty public constructor
    }
    ProgressBar progressBar;
    ProgressDialog pDialog;
    EditText etBuscar;
    Button btnBuscar;
    //TableLayout tlLista;
    ListView lista;
    ArrayAdapter adapter = null;
    String[] titulos = new String[0];
    String[] isbn = new String[0];
    String isbnParam = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_documento, container, false);

        etBuscar = (EditText)view.findViewById(R.id.edtBuscar);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        lista = (ListView)view.findViewById(R.id.lvLibros);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Cargando Datos");
        pDialog.setCancelable(false);
        pDialog.show();
        buscarLibro(" ", 1);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etBuscar.getText().toString().isEmpty()){
                    EnviarForm();
                }
                else{
                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Buscando...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    String busqueda = etBuscar.getText().toString();
                    buscarLibro(busqueda,2);
                    if(lista.getCount()== 0){
                        Toast.makeText(getActivity().getApplicationContext(), "Sin resultados", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int i = Documentos.getFragmento();

                if(i==1){
                    Documentos.setIsbnS(isbn[position]);

                    ConDocumentos conDocumentos = new ConDocumentos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new ConDocumentos());
                    fr.commit();
                }
                if(i==2){
                    Documentos.setIsbnS(isbn[position]);

                    PreDocumentos preDocumentos = new PreDocumentos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new PreDocumentos());
                    fr.commit();
                }

            }
        });

        return view;

    }

    /*public void obtenerLibros(){

        String URL = null;
        if(Documentos.getFragmento()==1){
            URL = "Direccion de Adiel";
        }
        else if(Documentos.getFragmento()==2){
            URL = "http://www.ingenieriadesistemasinformaticos.com/ws_bg17016/ws_consulta_documentos.php";
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                    if(response.length()>0){
                        try{
                            JSONArray doc = new JSONArray(response);
                            Log.i("sizejson",""+doc.length());

                            ArrayList<Documentos> documentos = new ArrayList<Documentos>();

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
                            cargarTabla(documentos);

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

    }*/

    public void buscarLibro(final String busqueda, int accion){

        String URL = null;

        switch (accion){
            case 1: //Consulta de docuemntos
                if(Documentos.getFragmento()==1){
                    URL = "Direccion de Adiel";
                }
                else if(Documentos.getFragmento()==2){
                    URL = "http://www.ingenieriadesistemasinformaticos.com/ws_bg17016/ws_consulta_documentos.php";
                }
                break;

            case 2:
                if(Documentos.getFragmento()==1){
                    URL = "Direccion de Adiel";
                }
                else if(Documentos.getFragmento()==2){
                    URL = "http://www.ingenieriadesistemasinformaticos.com/ws_bg17016/ws_buscar_documentos.php";
                }
                break;
        }




            lista.setAdapter(null);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            JSONArray bdoc = new JSONArray(response);
                            Log.i("sizejson", "" + bdoc.length());

                            ArrayList<Documentos> listB = new ArrayList<Documentos>();
                            for (int i = 0; i < bdoc.length(); i += 13) {
                                try {

                                    listB.add(new Documentos(
                                            bdoc.getInt(i + 1),
                                            bdoc.getInt(i + 7),
                                            bdoc.getInt(i + 12),
                                            bdoc.getString(i + 2),
                                            bdoc.getString(i + 3),
                                            bdoc.getString(i + 4),
                                            bdoc.getString(i + 5),
                                            bdoc.getString(i + 6),
                                            bdoc.getString(i + 8),
                                            bdoc.getString(i + 10),
                                            bdoc.getString(i + 11),
                                            bdoc.getString(i + 9)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            cargarTabla(listB);

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
                    parametros.put("campo", busqueda);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);

    }

    public void cargarTabla(ArrayList list){

        titulos = new String[list.size()];
        isbn = new String[list.size()];
        ArrayList<Documentos> docu = new ArrayList<Documentos>();
        docu = list;
        for (int i=0; i<list.size();i++){
            titulos[i] = docu.get(i).getTitulo().toString();
            isbn[i] = docu.get(i).getIsbn().toString();
        }

        adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
        lista.setAdapter(adapter);
        updateListViewHeight(lista);
        if (pDialog.isShowing())
            pDialog.dismiss();

    }

    public static void updateListViewHeight(ListView lista) {
        ListAdapter myListAdapter = lista.getAdapter();
        if (myListAdapter == null) {
            return;
        }
        // get listview height
        int totalHeight = 0;
        int adapterCount = myListAdapter.getCount();
        for (int size = 0; size < adapterCount; size++) {
            View listItem = myListAdapter.getView(size, null, lista);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // Change Height of ListView
        ViewGroup.LayoutParams params = lista.getLayoutParams();
        params.height = (totalHeight
                + (lista.getDividerHeight() * (adapterCount)));
        lista.setLayoutParams(params);
    }

    /**
     * Alerta
     * */
    public void EnviarForm(){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
        myBuild.setTitle("Mensaje");
        myBuild.setMessage("No ha Seleccionado un Documento Especifico, ¿Desea mostrar todos los Documentos en el Inventario" +
                " de la EISI Disponibles?");
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity().getApplicationContext(), "Campo de busqueda vacio", Toast.LENGTH_LONG).show();
                buscarLibro(" ",1);
            }
        });
        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = myBuild.create();
        dialog.show();
    }

}
