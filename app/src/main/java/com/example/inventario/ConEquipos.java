package com.example.inventario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventario.dialog.DatePickerFragment;
import com.example.inventario.ui.equipos.EquiposFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ConEquipos extends Fragment implements AdapterView.OnItemSelectedListener {

    public ConEquipos() {
        // Required empty public constructor
    }
    private EditText edmodelo, edserie, edinventario,edprecio, eddescripcion;
    private int id_mar, id_cat;

    HttpClient cliente;
    HttpPost post;
    List<NameValuePair> lista;
    Button btnRegresar, btnEliminar;
    private EditText etPlannedDate, etCompraDate;
    private Spinner spinnerCat, spinnerMar;
    private ArrayList<Categorias> categoriesList;
    private ArrayList<Marcas> marcasList;
    ProgressBar progressBar;
    ProgressDialog pDialog;
    /**
     * webservices
     * */
    private String URL_EDITAR ="https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_editarEquipo.php";
    private String URL_CATEGORIES = "https://invetariopdm115.000webhostapp.com/ws_ca06025/getcategoriasEquipos.php";
    private String URL_MARCAS = "https://invetariopdm115.000webhostapp.com/ws_ca06025/getMarcas.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mod_equipos, container, false);

        /**
         * Variables
         * */
        progressBar = view.findViewById(R.id.progressBar);
        //Initializing Spinner
        spinnerCat = (Spinner) view.findViewById(R.id.sp_categorias_equipos);
        spinnerMar = (Spinner) view.findViewById(R.id.sp_marca);
        //get other data form
        edmodelo = (EditText) view.findViewById(R.id.edmodelo);
        edserie = (EditText) view.findViewById(R.id.edserie);
        edinventario = (EditText) view.findViewById(R.id.edinventario);
        edprecio = (EditText) view.findViewById(R.id.edprecio);
        edserie = (EditText) view.findViewById(R.id.edserie);
        eddescripcion = (EditText) view.findViewById(R.id.eddescripcion);
        etCompraDate = (EditText) view.findViewById(R.id.etDateCompra);
        etPlannedDate = (EditText) view.findViewById(R.id.etDate);
        btnRegresar = (Button) view.findViewById(R.id.btnMenu);
        btnEliminar = (Button) view.findViewById(R.id.btnEliminar);

        //Initializing the ArrayList
        categoriesList = new ArrayList<Categorias>();
        marcasList = new ArrayList<Marcas>();

        //Call Actions
        spinnerMar.setOnItemSelectedListener(this);
        new ConEquipos.GetMarcas().execute();
        spinnerCat.setOnItemSelectedListener( this);
        new ConEquipos.GetCategories().execute();
        /**
         * Button Obteniendo Fecha Ingreso
         * */
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etDate:
                        showDatePickerDialog();
                        break;
                }
            }
        });

        etCompraDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etDateCompra:
                        showDatePickerDialogCompra();
                        break;
                }
            }
        });


        /**
         * Button Guardado de Datos
         * */
        view.findViewById(R.id.btnEquipo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String modelo = edmodelo.getText().toString().trim();
                final String serie =  edserie.getText().toString().trim();
                final String inventario =  edinventario.getText().toString().trim();
                final String precio =  edprecio.getText().toString().trim();
                final String descripcion =  eddescripcion.getText().toString().trim();
                final String fecha =  etPlannedDate.getText().toString().trim();
                final String fechaCompra =  etCompraDate.getText().toString().trim();
                /**
                 * Validaciones
                 * */
                if (TextUtils.isEmpty(fecha)) {
                    etPlannedDate.setError("Favor Ingresar Fecha Ingreso");
                    etPlannedDate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(modelo)) {
                    edmodelo.setError("Favor Ingresar Modelo");
                    edmodelo.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(serie)) {
                    edserie.setError("Favor Ingresar Serie");
                    edserie.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(inventario)) {
                    edinventario.setError("Favor Ingresar Nº de Inventario");
                    edinventario.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(precio)) {
                    edprecio.setError("Favor Ingresar Precio");
                    edprecio.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(fechaCompra)) {
                    etCompraDate.setError("Favor Ingresar Fecha de Compra");
                    etCompraDate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(descripcion)) {
                    eddescripcion.setError("Favor Ingresar Descripcion");
                    eddescripcion.requestFocus();
                    return;
                }
                /**
                 * Metodo Guardado de Datos
                 * */
                EnviarForm();

            }
        });

        //Método que recibe la acción OnClick luego se llama al método de confirmación
        view.findViewById(R.id.btnEliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmarEliminarEquipo();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegresarBusqueda();
            }
        });
        cargarEquipoConsultado();
        return view;
    }

    /**
     *Regresar al Menu
     * */

    //Metodo para regresar a pantalla de Documentos
    public  void RegresarBusqueda(){
        EquiposFragment equiposFragment = new EquiposFragment();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment, new EquiposFragment());
        fr.commit();
    }

    //Método que notifica al usuario si esta seguro de confirmar eliminar
    public void ConfirmarEliminarEquipo(){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
        myBuild.setTitle("Mensaje");
        myBuild.setMessage("¿Esta Seguro que desea Eliminar el Equipo?");
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EliminarEquipo();
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


    //Método que consume el servicio para eliminar
    public void EliminarEquipo(){
        final String URL = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_eliminarEquipo.php ";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(), "Equipo Eliminado Satisfactoriamente", Toast.LENGTH_LONG).show();
                //LimpiarElementos();
                RegresarBusqueda();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido un error, no se ha podido eliminar el Equipo", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("inventario", edinventario.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }


    /**
     *Alerta para guardado de datos
     * */

    public void EnviarForm(){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
        myBuild.setTitle("Mensaje");
        myBuild.setMessage("¿Está Seguro que desea actualizar el Equipo?");
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ConEquipos.EnviarDatos(getActivity()).execute();
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

    /**
     * Guardado de Datos
     * */
    class EnviarDatos   extends AsyncTask<String, Integer, String > {

        private Activity contexto;
        EnviarDatos(Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            final String setmodelo = edmodelo.getText().toString().trim();
            final String setserie =  edserie.getText().toString().trim();
            final String setinventario =  edinventario.getText().toString().trim();
            final String setprecio =  edprecio.getText().toString().trim();
            final String setcompra =  etCompraDate.getText().toString().trim();
            final String setdescripcion =  eddescripcion.getText().toString().trim();
            final String setfecha =  etPlannedDate.getText().toString().trim();
            final String id_cat =  spinnerCat.getSelectedItem().toString().trim();
            final String id_mar =  spinnerMar.getSelectedItem().toString().trim();

            cliente = new DefaultHttpClient();
            post = new HttpPost(URL_EDITAR);
            lista = new  ArrayList<NameValuePair>(11);
            lista.add(new BasicNameValuePair("id_categoria", id_cat));
            lista.add(new BasicNameValuePair("num_inventario", setinventario));
            lista.add(new BasicNameValuePair("id_marca", id_mar));
            lista.add(new BasicNameValuePair("modelo", setmodelo));
            lista.add(new BasicNameValuePair("serie", setserie));
            lista.add(new BasicNameValuePair("precio", setprecio));
            lista.add(new BasicNameValuePair("fecha_compra", setcompra));
            lista.add(new BasicNameValuePair("descripcion", setdescripcion));
            lista.add(new BasicNameValuePair("fecha_ingreso", setfecha));

            String responseStr =" ";
            try{
                post.setEntity(new UrlEncodedFormEntity(lista, "utf-8"));
                HttpResponse response = cliente.execute(post);
                responseStr = EntityUtils.toString(response.getEntity());
                return responseStr;

            }catch (UnsupportedEncodingException e){
                responseStr ="Error";

            }catch (ClientProtocolException e){
                responseStr ="Error";
                return responseStr;
            }catch (IOException e){
                responseStr ="Error";
                return responseStr;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String responseStr) {
            if(responseStr.equals("{\"error\":true,\"message\":\"Error en Guardar Equipo\"}")){
                Toast.makeText(contexto,"Error en Editar Documento", Toast.LENGTH_SHORT).show();
            }
            else if(responseStr.equals("{\"error\":true,\"message\":\"Equipo ya Existe\"}")){
                Toast.makeText(contexto,"Nº Inventario y/o Nº de Serie ya estan registrados", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(contexto,"Datos de Equipo actualizados exitosamente", Toast.LENGTH_SHORT).show();
                edmodelo.setText("");
                edserie.setText("");
                edinventario.setText("");
                edprecio.setText("");
                etPlannedDate.setText("");
                etCompraDate.setText("");
                eddescripcion.setText("");
            }

        }


    }

    /*En el siguiente método nos conectamos al ws que realiza la consulta de los equipos
   y los obtenemos recorriendo el json que trae todos los datos y luego los alojamos en un ArrayList del tipo
   de la Clase Documentos que hará uso de todos sus atributos, para traer el solicitado hacemos uso de la variable contenida
   en la Clase Documentos numIven*/
    public void cargarEquipoConsultado(){
        final String URLB = "https://invetariopdm115.000webhostapp.com/ws_vc17009/ws_CargarDatosEquBuscado.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        JSONArray equi = new JSONArray(response);
                        Log.i("sizejson", "" + equi.length());

                        ArrayList<Equipos> listB = new ArrayList<Equipos>();
                        for (int i = 0; i < equi.length(); i += 12) {
                            try {
                                listB.add(new Equipos(
                                        equi.getInt(i ),
                                        equi.getInt(i+1),
                                        equi.getInt(i+2),
                                        equi.getInt(i+3),
                                        equi.getDouble(i+4),
                                        equi.getString(i+5),
                                        equi.getString(i+6),
                                        equi.getString(i+7),
                                        equi.getString(i+8),
                                        equi.getString(i+9),
                                        equi.getString(i+10)));
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), "Campo de busqueda vacio" + e, Toast.LENGTH_LONG).show();
                            }

                        }
                        cargarCampos(listB);
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
                String busqueda = Equipos.getNum_inv();
                parametros.put("busqueda", busqueda);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    /*Seteamos los valores obtenidos del ArrayList lleno obtenido del método obtenerDatosConsulta, lo recorremos y lo asignamos a la propiedad Text de cada EditText*/
    public void cargarCampos(ArrayList list){

        for (int i=0; i<list.size();i++){
            ArrayList<Equipos> Equi = new ArrayList<Equipos>();
            Equi = list;
            edmodelo.setText(Equi.get(i).getModelo().toString());
            edserie.setText(Equi.get(i).getSerie().toString());
            edinventario.setText(Equi.get(i).getNum_inventario().toString());
            edprecio.setText(Equi.get(i).getPrecio().toString());
            etCompraDate.setText(Equi.get(i).getFecha_compra().toString());
            eddescripcion.setText(Equi.get(i).getDescripcion().toString());
            etPlannedDate.setText(Equi.get(i).getFecha_ingreso().toString());
            List<String> lables = new ArrayList<String>();
            for (int j = 0; j < categoriesList.size(); j++) {
                lables.add(categoriesList.get(j).getCategoria());
            }
            // Creating adapter for spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, lables);
            // attaching data adapter to spinner
            spinnerCat.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
         //   spinnerCat.setSelection(spinnerAdapter.getPosition(Equi.get(i).getId_categoria().toString()));
           // spinnerIdio.setSelection(obtenerPosicionItem(spinnerIdio, docu.get(i).getId_idioma().toString()));

        }
    }


    /**
     * Seleccionar Fecha
     * */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" +twoDigits(day)  ;
                etPlannedDate.setText(selectedDate);

            }

            private String twoDigits(int n) {
                return (n<=9) ? ("0"+n) : String.valueOf(n);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showDatePickerDialogCompra() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" +twoDigits(day)  ;
                etCompraDate.setText(selectedDate);
            }

            private String twoDigits(int n) {
                return (n<=9) ? ("0"+n) : String.valueOf(n);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    /**
     * Añadiendo datos a spinner categorias
     * */
    private void populateSpinnerCategoria() {
        List<String> lables = new ArrayList<String>();



        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getCategoria());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCat.setAdapter(spinnerAdapter);
    }

    /**
     * Añadiendo datos a spinner Idioma
     * */
    private void populateSpinnerMarca() {
        List<String> marcas = new ArrayList<String>();



        for (int i = 0; i < marcasList.size(); i++) {
            marcas.add(marcasList.get(i).getMarca());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, marcas);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerMar.setAdapter(spinnerAdapter);
    }

    /**
     * Async task to get all categories
     * */
    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Cargando Datos");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("categories");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            Categorias cat = new Categorias(catObj.getInt("id"),
                                    catObj.getString("name"));
                            categoriesList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "No se recibe datos del servidor!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinnerCategoria();
        }

    }

    /**
     * Async task to get all Marcas
     * */
    private class GetMarcas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_MARCAS, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray language = jsonObj
                                .getJSONArray("marcas");

                        for (int i = 0; i < language.length(); i++) {
                            JSONObject idiObj = (JSONObject) language.get(i);
                            Marcas mar = new Marcas(idiObj.getInt("id"),
                                    idiObj.getString("name"));
                            marcasList.add(mar);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "No se recibe datos del servidor!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateSpinnerMarca();
        }

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    public void onNothingSelected(AdapterView<?> parent) {

    }


}
