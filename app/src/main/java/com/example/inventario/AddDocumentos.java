package com.example.inventario;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.inventario.dialog.DatePickerFragment;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class AddDocumentos extends Fragment implements AdapterView.OnItemSelectedListener {



    public AddDocumentos() {
        // Required empty public constructor
    }
    private EditText etPlannedDate;
    private Spinner spinnerCat, spinnerIdio;
    // array list for spinner adapter
    private ArrayList<Categorias> categoriesList;
    private ArrayList<Idiomas> idiomaList;
    // Url to get all categories
    ProgressDialog pDialog;
    private String URL_CATEGORIES = "https://inventario-pdm115.000webhostapp.com/getcategorias.php";
    private String URL_IDIOMAS = "https://inventario-pdm115.000webhostapp.com/getIdiomas.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_add_documento, container, false);

         //Initializing Spinner
        spinnerCat = (Spinner) view.findViewById(R.id.sp_categorias);
        spinnerIdio = (Spinner) view.findViewById(R.id.sp_idioma);


        //Initializing the ArrayList
        categoriesList = new ArrayList<Categorias>();
        idiomaList = new ArrayList<Idiomas>();

        spinnerIdio.setOnItemSelectedListener(this);
         new GetIdiomas().execute();
        spinnerCat.setOnItemSelectedListener(this);
        new GetCategories().execute();
        etPlannedDate = (EditText) view.findViewById(R.id.etDate);
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
       return view;

    }

    /**
     * Seleccionar Fecha
     * */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day)  + " - " + twoDigits(month+1) + " - " + year;
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



