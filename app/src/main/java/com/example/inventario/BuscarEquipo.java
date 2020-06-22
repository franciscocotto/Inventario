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
import com.example.inventario.ui.documentos.DocumentosFragment;
import com.example.inventario.ui.equipos.EquiposFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BuscarEquipo extends Fragment {

    ProgressBar progressBar;
    ProgressDialog pDialog;
    EditText etBuscar;
    Button btnBuscar, btnRegresar;
    ListView lista;
    ArrayAdapter adapter = null;
    String[] modelo = new String[0];
    String[] numero_inv = new String[0];
    String num_inv = null;

    public BuscarEquipo() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buscar_equipos, container, false);

        etBuscar = (EditText)view.findViewById(R.id.edtBuscar);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        lista = (ListView)view.findViewById(R.id.lvEquipos);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        btnRegresar = (Button) view.findViewById(R.id.btnRegresarBusqueda);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Cargando Datos");
        pDialog.setCancelable(false);
        pDialog.show();
        buscarEquipo(" ", 1);

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
                    buscarEquipo(busqueda,2);
                    if(lista.getCount()== 0){
                        Toast.makeText(getActivity().getApplicationContext(), "Sin resultados", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int i = Equipos.getFragmento();

                if(i==1){
                    Equipos.setNum_inv(numero_inv[position]);

                    ConEquipos conEquipos = new ConEquipos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new ConEquipos());
                    fr.commit();
                }
                if(i==2){
                    Equipos.setNum_inv(numero_inv[position]);

                    PreEquipos preEquipos = new PreEquipos();

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new PreEquipos());
                    fr.commit();
                }

            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegresarBusqueda();
            }
        });
        return view;
    }

    public void buscarEquipo(final String busqueda, int accion){

        String URL = null;

        switch (accion){
            case 1: //Consulta de docuemntos
                if(Equipos.getFragmento()==1){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_cargarEquipos.php";
                }
                else if(Equipos.getFragmento()==2){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_consulta_equipos.php";
                }
                break;

            case 2: //Buscar documentos
                if(Equipos.getFragmento()==1){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_CargarDatosEquBuscado.php";
                }
                else if(Equipos.getFragmento()==2){
                    URL = "https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_buscar_equipo.php";
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
                        JSONArray equi = new JSONArray(response);
                        Log.i("sizejson", "" + equi.length());

                        ArrayList<Equipos> list = new ArrayList<Equipos>();
                        for (int i = 0; i < equi.length(); i += 5) {
                            try {
                                list.add(new Equipos(
                                        equi.getInt(i),
                                        equi.getString(i+1),
                                        equi.getString(i+2),
                                        equi.getString(i+3),
                                        equi.getString(i+4)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        cargarTabla(list);

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

        modelo = new String[list.size()];
        numero_inv = new String[list.size()];
        ArrayList<Equipos> equipos = new ArrayList<Equipos>();
        equipos = list;
        for (int i=0; i<list.size();i++){
            modelo[i] = equipos.get(i).getModelo().toString();
            numero_inv[i] = equipos.get(i).getNum_inventario().toString();
        }

        adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, modelo);
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
                buscarEquipo(" ",1);
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
    public  void RegresarBusqueda(){
        EquiposFragment equiposFragment = new EquiposFragment();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment, new EquiposFragment());
        fr.commit();

    }
}
