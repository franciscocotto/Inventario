package com.example.inventario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.inventario.dialog.DatePickerFragment;
import com.loopj.android.http.AsyncHttpClient;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class AddDocumentos extends Fragment implements AdapterView.OnItemSelectedListener {

    public AddDocumentos() {

    }

    private EditText edautor, edtema, edtitulo,edsubtitulo, edpalabras_clave, ededitorial, edisbn, eddescripcion;
    private int id_idi, id_cat;

    HttpClient cliente;
    HttpPost post;
    List<NameValuePair> lista;

    private EditText etPlannedDate;
    private Spinner spinnerCat, spinnerIdio;
    private ArrayList<Categorias> categoriesList;
    private ArrayList<Idiomas> idiomaList;
    ProgressBar progressBar;
    ProgressDialog pDialog;
    /**
     * webservices
     * */
    private String URL_GUARDAR ="http://www.ingenieriadesistemasinformaticos.com/ws_ca06025/PostDocument.php";
    private String URL_CATEGORIES = "http://www.ingenieriadesistemasinformaticos.com/ws_ca06025/getcategorias.php";
    private String URL_IDIOMAS = "http://www.ingenieriadesistemasinformaticos.com/ws_ca06025/getIdiomas.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_add_documento, container, false);
        /**
         * Variables
         * */
        progressBar = view.findViewById(R.id.progressBar);
         //Initializing Spinner
        spinnerCat = (Spinner) view.findViewById(R.id.sp_categorias);
        spinnerIdio = (Spinner) view.findViewById(R.id.sp_idioma);
        //get other data form
        edtitulo = (EditText) view.findViewById(R.id.edtitulo);
        edsubtitulo = (EditText) view.findViewById(R.id.edsubtitulo);
        edtema = (EditText) view.findViewById(R.id.edtema);
        edautor = (EditText) view.findViewById(R.id.edautor);
        edisbn = (EditText) view.findViewById(R.id.edisbm);
        edpalabras_clave = (EditText) view.findViewById(R.id.edpalabras_clave);
        eddescripcion = (EditText) view.findViewById(R.id.eddescripcion);
        ededitorial = (EditText) view.findViewById(R.id.ededitorial);
        etPlannedDate = (EditText) view.findViewById(R.id.etDate);

        //Initializing the ArrayList
        categoriesList = new ArrayList<Categorias>();
        idiomaList = new ArrayList<Idiomas>();


        //Call Actions
        spinnerIdio.setOnItemSelectedListener(this);
         new GetIdiomas().execute();
        spinnerCat.setOnItemSelectedListener(this);
        new GetCategories().execute();

        /**
         * Button Obteniendo Fecha
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

        /**
         * Button Guardado de Datos
         * */
        view.findViewById(R.id.btnDocumento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String titulo = edtitulo.getText().toString().trim();
                final String subtitulo =  edsubtitulo.getText().toString().trim();
                final String tema =  edtema.getText().toString().trim();
                final String autor =  edautor.getText().toString().trim();
                final String isbm =  edisbn.getText().toString().trim();
                final String palabras =  edpalabras_clave.getText().toString().trim();
                final String descripcion =  eddescripcion.getText().toString().trim();
                final String fecha =  etPlannedDate.getText().toString().trim();
                final String editorial =  ededitorial.getText().toString().trim();
                /**
                 * Validaciones
                 * */
                if (TextUtils.isEmpty(fecha)) {
                    etPlannedDate.setError("Favor Ingresar Fecha");
                    etPlannedDate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(autor)) {
                    edautor.setError("Favor Ingresar Autor");
                    edautor.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(tema)) {
                    edtema.setError("Favor Ingresar Tema");
                    edtema.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(titulo)) {
                    edtitulo.setError("Favor Ingresar Titulo");
                    edtitulo.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(subtitulo)) {
                    edsubtitulo.setError("Favor Ingresar Subtitulo");
                    edsubtitulo.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(palabras)) {
                    edpalabras_clave.setError("Favor Ingresar Palabras Clave");
                    edpalabras_clave.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(editorial)) {
                    ededitorial.setError("Favor Ingresar Editorial");
                    ededitorial.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(isbm)) {
                    edisbn.setError("Favor Ingresar ISBN");
                    edisbn.requestFocus();
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

       return view;

    }
    /**
     *Alerta para guardado de datos
     * */

    public void EnviarForm(){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
        myBuild.setTitle("Mensaje");
        myBuild.setMessage("¿Esta Seguro que desea Guardar el Documento?");
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new EnviarDatos(getActivity()).execute();
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
    class EnviarDatos   extends AsyncTask<String, Integer, String >{

        private Activity contexto;
        EnviarDatos(Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            final String settitulo = edtitulo.getText().toString().trim();
            final String setsubtitulo =  edsubtitulo.getText().toString().trim();
            final String settema =  edtema.getText().toString().trim();
            final String setautor =  edautor.getText().toString().trim();
            final String setisbm =  edisbn.getText().toString().trim();
            final String setpalabras =  edpalabras_clave.getText().toString().trim();
            final String setdescripcion =  eddescripcion.getText().toString().trim();
            final String setfecha =  etPlannedDate.getText().toString().trim();
            final String seteditorial =  ededitorial.getText().toString().trim();
            final String id_cat =  spinnerCat.getSelectedItem().toString().trim();
            final String id_idi =  spinnerIdio.getSelectedItem().toString().trim();

            cliente = new DefaultHttpClient();
            post = new HttpPost(URL_GUARDAR);
            lista = new  ArrayList<NameValuePair>(11);
            lista.add(new BasicNameValuePair("id_categoria", id_cat));
            lista.add(new BasicNameValuePair("id_idioma", id_idi));
            lista.add(new BasicNameValuePair("titulo", settitulo));
            lista.add(new BasicNameValuePair("subtitulo", setsubtitulo));
            lista.add(new BasicNameValuePair("tema", settema));
            lista.add(new BasicNameValuePair("fecha_ingreso", setfecha));
            lista.add(new BasicNameValuePair("isbm", setisbm));
            lista.add(new BasicNameValuePair("autor", setautor));
            lista.add(new BasicNameValuePair("editorial", seteditorial));
            lista.add(new BasicNameValuePair("palabras", setpalabras));
            lista.add(new BasicNameValuePair("descripcion", setdescripcion));

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
            if(responseStr.equals("{\"error\":true,\"message\":\"Error en Guardar Documento\"}")){
                Toast.makeText(contexto,"Error en Guardar Documento", Toast.LENGTH_SHORT).show();
            }
            else if(responseStr.equals("{\"error\":true,\"message\":\"Documento ya Existe\"}")){
                Toast.makeText(contexto,"Titulo y/o ISBN ya estan registrados", Toast.LENGTH_SHORT).show();
            }
          else{
                Toast.makeText(contexto,"Datos de documento guardados exitosamente", Toast.LENGTH_SHORT).show();
                edtitulo.setText("");
                edautor.setText("");
                edsubtitulo.setText("");
                edtema.setText("");
                etPlannedDate.setText("");
                edpalabras_clave.setText("");
                ededitorial.setText("");
                edisbn.setText("");
                eddescripcion.setText("");
            }

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
    private void populateSpinnerIdioma() {
        List<String> idiomas = new ArrayList<String>();



        for (int i = 0; i < idiomaList.size(); i++) {
            idiomas.add(idiomaList.get(i).getIdioma());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, idiomas);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerIdio.setAdapter(spinnerAdapter);
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
     * Async task to get all idiomas
     * */
    private class GetIdiomas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_IDIOMAS, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray language = jsonObj
                                .getJSONArray("idiomas");

                        for (int i = 0; i < language.length(); i++) {
                            JSONObject idiObj = (JSONObject) language.get(i);
                            Idiomas idi = new Idiomas(idiObj.getInt("id"),
                                    idiObj.getString("name"));
                            idiomaList.add(idi);
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
            populateSpinnerIdioma();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       /* Toast.makeText(
                getActivity().getApplicationContext(),
                "Categoria " + parent.getItemAtPosition(position).toString() ,
                Toast.LENGTH_LONG).show();*/

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    }



