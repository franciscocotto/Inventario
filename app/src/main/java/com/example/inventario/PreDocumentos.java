package com.example.inventario;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventario.dialog.DatePickerFragment;

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
    ArrayList<Escuelas> escuelas = new ArrayList<Escuelas>();
    ArrayList<Areas> areas = new ArrayList<Areas>();
    ArrayList<Motivos> motivos = new ArrayList<Motivos>();

    String[] docentesV;
    String[] escuelasV;
    String[] areasV;
    String[] motivosV;

    String isbnEn = null;
    String estado = null;
    EditText etFechaPrestamo, etFechaDevolucion;
    ProgressBar progressBar;
    ProgressDialog pDialog;
    EditText edEstado, edTitulo, edTema, edIsbn;
    AutoCompleteTextView acDocentes;
    Button btnAccion;
    TextView lblAccion;
    Spinner spEscuelas, spAreas, spMotivos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_documentos, container, false);

        edTitulo = (EditText)view.findViewById(R.id.edTitulo);
        edIsbn = (EditText)view.findViewById(R.id.edIsbn);
        edEstado = (EditText)view.findViewById(R.id.edEstado);
        acDocentes = (AutoCompleteTextView)view.findViewById(R.id.acDocentes);
        etFechaPrestamo = (EditText) view.findViewById(R.id.etFechaPrestamo);
        etFechaDevolucion = (EditText)view.findViewById(R.id.etFechaDevolucion);
        lblAccion = (TextView)view.findViewById(R.id.lblAccion);
        btnAccion = (Button)view.findViewById(R.id.btnAccion);

        spEscuelas = (Spinner)view.findViewById(R.id.spEscuela);
        spAreas = (Spinner)view.findViewById(R.id.spArea);
        spMotivos = (Spinner)view.findViewById(R.id.spMotivo);


        isbnEn = Documentos.getIsbnS();

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Cargando Datos");
        pDialog.setCancelable(false);
        pDialog.show();
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_prestamo_documentos.php", 1, isbnEn);
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_cargar_docentes.php", 2, " ");
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 4, "escuelas");
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 5, "areas");
        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 6, "motivos");

        etFechaPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etFechaPrestamo:
                        showDatePickerDialog(1);
                        break;
                }
            }
        });

        etFechaDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.etFechaDevolucion:
                        showDatePickerDialog(2);
                        break;
                }
            }
        });

        btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }


        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity().getApplicationContext(), "Hola", Toast.LENGTH_SHORT).show();
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
                                for (int i = 0; i < bdoc.length(); i+=4) {
                                    try {

                                        listB.add(new InventarioDocumentos(
                                                bdoc.getInt(0),
                                                bdoc.getInt(1),
                                                bdoc.getString(2),
                                                bdoc.getString(3)));
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

                            case 3:
                                String estado = bdoc.getString(0);
                                generarAccion(estado);
                                break;

                            case 4:
                                ArrayList<Escuelas> escuelas = new ArrayList<Escuelas>();
                                for (int i = 0;i<bdoc.length();i+=2){
                                    try {
                                        escuelas.add(new Escuelas(bdoc.getInt(i), bdoc.getString(i+1)));

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                cargarSpinner(escuelas, accion);
                                break;

                            case 5:
                                ArrayList<Areas> areas = new ArrayList<Areas>();
                                for (int i = 0;i<bdoc.length();i+=2){
                                    try {
                                        areas.add(new Areas(bdoc.getInt(i), bdoc.getString(i+1)));

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                cargarSpinner(areas, accion);
                                break;

                            case 6:
                                ArrayList<Motivos> motivos = new ArrayList<Motivos>();
                                for (int i = 0;i<bdoc.length();i+=2){
                                    try {
                                        motivos.add(new Motivos(bdoc.getInt(i), bdoc.getString(i+1)));

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                cargarSpinner(motivos, accion);
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

        estado = String.valueOf(documentos.get(0).getId_estado());

        cargarDatos("https://inventario-pdm115.000webhostapp.com/ws_bg17016/ws_estado_documento.php", 3, estado);

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //Carga los nombres de los docentes obtenidos
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

    //Metodo que carga los datos de los Spinner
    public void cargarSpinner(ArrayList list, int accion){

        if(accion==4){
            this.escuelas = list;
            escuelasV = new String[escuelas.size()+1];
            escuelasV[0]=" ";
            for (int i=0;i<escuelas.size();i++){
                escuelasV[i+1] = escuelas.get(i).getEscuela();
            }
            ArrayAdapter adapterEscuelas = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, escuelasV);
            spEscuelas.setAdapter(adapterEscuelas);
        }
        else if(accion==5){
            this.areas = list;
            areasV = new String[areas.size()+1];
            areasV[0]=" ";
            for (int i=0;i<areas.size();i++){
                areasV[i+1] = areas.get(i).getArea();
            }
            ArrayAdapter adapterAreas = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, areasV);
            spAreas.setAdapter(adapterAreas);

        }
        else if(accion==6){
            this.motivos = list;
            motivosV = new String[motivos.size()+1];
            motivosV[0]=" ";
            for (int i=0;i<motivos.size();i++){
                motivosV[i+1] = motivos.get(i).getMotivos();
            }
            ArrayAdapter adapterMotivos = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, motivosV);
            spMotivos.setAdapter(adapterMotivos);

        }

    }
    //Determina la accion del formulario
    public void generarAccion(String estado){
        edEstado.setText(estado);
        if(estado.equals("DISPONIBLE")){
            lblAccion.setText("ACCION: REALIZAR PRESTAMO");
        }
        else if(estado.equals("PRESTADO")){
            lblAccion.setText("ACCION: DEVOLUCION DE DOCUMENTO");
        }
        else if(estado.equals("EXTRAVIADO")){
            lblAccion.setText("ACCIONES NO DISPONIBLES");
            acDocentes.setEnabled(false);
            spEscuelas.setEnabled(false);
            spMotivos.setEnabled(false);
            spAreas.setEnabled(false);
            btnAccion.setEnabled(false);

        }
        else if(estado.equals("ASIGNADO")){
            lblAccion.setText("ACCIONES NO DISPONIBLES");
            acDocentes.setEnabled(false);
            spEscuelas.setEnabled(false);
            spMotivos.setEnabled(false);
            spAreas.setEnabled(false);
            btnAccion.setEnabled(false);
        }
    }

    private void showDatePickerDialog(final int id) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" +twoDigits(day)  ;
                switch (id){
                    case 1:
                        etFechaPrestamo.setText(selectedDate);
                        break;
                    case 2:
                        etFechaDevolucion.setText(selectedDate);
                        break;
                }

            }

            private String twoDigits(int n) {
                return (n<=9) ? ("0"+n) : String.valueOf(n);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}
