package com.example.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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

public class ConEquipos extends Fragment {

    public ConEquipos() {
        // Required empty public constructor
    }


    ProgressBar progressBar;
    ProgressDialog pDialog;
    EditText etBuscar;
    Button btnBuscar;
    //TableLayout tlLista;
    ListView lista;
    ArrayAdapter adapter = null;
    String[] descripcion = new String[0];
    String[] num_inventario = new String[0];
    String isbnParam = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_con_equipos, container, false);

        etBuscar = (EditText)view.findViewById(R.id.edtBuscar);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        lista = (ListView)view.findViewById(R.id.lvEquipos);
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
                    Equipos.setNumInventario(num_inventario[position]);

                    ConEquipos conEquipos = new ConEquipos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new ConEquipos());
                    fr.commit();
                }
                if(i==2){
                    Equipos.setNumInventario(num_inventario[position]);

                    PreDocumentos preDocumentos = new PreDocumentos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new PreDocumentos());
                    fr.commit();
                }

            }
        });

        return view;

    }


    public void buscarLibro(final String busqueda, int accion){

        String URL = null;

        switch (accion){
            case 1: //Consulta de docuemntos
                if(Equipos.getFragmento()==1){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_CargarEquiposActivos.php";
                }
                else if(Documentos.getFragmento()==2){
                    URL = "";
                }
                break;

            case 2: //Buscar documentos
                if(Equipos.getFragmento()==1){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_CargarDatosEquBuscado.php";
                }
                else if(Documentos.getFragmento()==2){
                    URL = "";
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

                        ArrayList<Equipos> listB = new ArrayList<Equipos>();
                        for (int i = 0; i < bdoc.length(); i += 12) {
                            try {

                                listB.add(new Equipos(
                                        bdoc.getInt(i + 1),
                                        bdoc.getInt(i + 6),
                                        bdoc.getInt(i + 11),
                                        bdoc.getInt(i + 2),
                                        bdoc.getDouble(i + 3),
                                        bdoc.getString(i + 4),
                                        bdoc.getString(i + 5),
                                        bdoc.getString(i + 7),
                                        bdoc.getString(i + 9),
                                        bdoc.getString(i + 10),
                                        bdoc.getString(i + 8)));
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
                parametros.put("busqueda", busqueda);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void cargarTabla(ArrayList list){

        descripcion = new String[list.size()];
        num_inventario = new String[list.size()];
        ArrayList<Equipos> docu = new ArrayList<Equipos>();
        docu = list;
        for (int i=0; i<list.size();i++){
            descripcion[i] = docu.get(i).getNum_inventario().toString();
            num_inventario[i] = docu.get(i).getFecha_compra().toString();
        }

        adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, descripcion);
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
        myBuild.setMessage("No ha Seleccionado un Equipo Especifico, ¿Desea mostrar todos los Equipos en el Inventario" +
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
