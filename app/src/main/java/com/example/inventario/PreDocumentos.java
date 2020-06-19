package com.example.inventario;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*  Autor:   Luis Salvador Barrera Garcia

    Carnet:  BG17016

    Descripcion:
    Este fragment se encarga de solicitar los datos al usuario para realizar un prestamo o asignacion de
    documento a un docente previamente registrado en la base de datos.
*/

public class PreDocumentos extends Fragment {

    public PreDocumentos() {

    }
    InventarioDocumentos invDocumentos = new InventarioDocumentos();

    //Definicion de componentes a utilizar
    ArrayList<InventarioDocumentos> documentos = new ArrayList<InventarioDocumentos>();
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
    EditText edEstado, edTitulo, edIsbn, edObservacions;
    AutoCompleteTextView acDocentes;
    Button btnAccion, btnRegresar;
    TextView lblAccion;
    Spinner spEscuelas, spAreas, spMotivos;
    CheckBox cbAsignado, cbCiclo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_documentos, container, false);



        //Inicializacion de los componentes a utilizar

        //Edit text y TextView
        edTitulo = (EditText)view.findViewById(R.id.edTitulo);
        edIsbn = (EditText)view.findViewById(R.id.edIsbn);
        edEstado = (EditText)view.findViewById(R.id.edEstado);
        acDocentes = (AutoCompleteTextView)view.findViewById(R.id.acDocentes);
        etFechaPrestamo = (EditText) view.findViewById(R.id.etFechaPrestamo);
        etFechaDevolucion = (EditText)view.findViewById(R.id.etFechaDevolucion);
        edObservacions = (EditText)view.findViewById(R.id.edObservaciones);
        lblAccion = (TextView)view.findViewById(R.id.lblAccion);
        btnRegresar = (Button)view.findViewById(R.id.btnBack);
        //Botones
        btnAccion = (Button)view.findViewById(R.id.btnAccion);

        //Spinners
        spEscuelas = (Spinner)view.findViewById(R.id.spEscuela);
        spAreas = (Spinner)view.findViewById(R.id.spArea);
        spMotivos = (Spinner)view.findViewById(R.id.spMotivo);

        //CheckBox
        cbAsignado = (CheckBox)view.findViewById(R.id.cbDefinitiva);
        cbCiclo = (CheckBox)view.findViewById(R.id.cbCiclo);

        //Obtenemos el isbn del libro seleccionado en el fragment BuscarDocumentos
        isbnEn = Documentos.getIsbnS();

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Cargando Datos");
        pDialog.setCancelable(false);
        pDialog.show();

        //Consumo de los Web Serces mediante Volley
        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_prestamo_documentos.php", 1, isbnEn);
        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_cargar_docentes.php", 2, " ");
        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 4, "escuelas");
        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 5, "areas");
        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_spinners_prestamo.php", 6, "motivos");

        //Calentdario para fecha prestamo
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

        //Calendario para fecha devolucion
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

        cbAsignado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbAsignado.isChecked() == true){
                    cbCiclo.setChecked(false);
                    cbCiclo.setEnabled(false);
                    etFechaDevolucion.setEnabled(false);
                    etFechaDevolucion.setError(null);
                }
                else{
                    cbCiclo.setEnabled(true);
                    etFechaDevolucion.setEnabled(true);
                }
            }
        });

        cbCiclo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbCiclo.isChecked()==true){
                    cbAsignado.setChecked(false);
                    cbAsignado.setEnabled(false);
                    etFechaDevolucion.setEnabled(false);
                    etFechaDevolucion.setError(null);
                }
                else{
                    cbAsignado.setEnabled(true);
                    etFechaDevolucion.setEnabled(true);
                }
            }
        });

        //Boton de accion
        btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String mensaje;

                if(estado.equals("DISPONIBLE")){
                    mensaje = "¿Esta seguro que desea realizar el prestamo?";
                    EnviarForm(mensaje, 1);
                }

            }


        });


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Documentos.setFragmento(2);
                BuscarDocumento conDocumentos = new BuscarDocumento();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new BuscarDocumento());
                fr.commit();
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
        myBuild.setMessage("¿Está Seguro que desea Asignar el Documento?");
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accionPrestar();
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



    public void EnviarForm(String mensaje, final int accion){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());
        myBuild.setTitle("Mensaje");
        myBuild.setMessage(mensaje);
        myBuild.setIcon(R.drawable.ic_error_outline_black_24dp);
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(accion==1){
                    accionPrestar();
                }
                else if(accion==2){
                    accionDevolver();
                }
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
                                ArrayList<InventarioDocumentos> documentos = new ArrayList<InventarioDocumentos>();
                                for (int i = 0; i < bdoc.length(); i+=4) {
                                    try {

                                        documentos.add(new InventarioDocumentos(
                                                bdoc.getInt(0),
                                                bdoc.getInt(1),
                                                bdoc.getInt(2),
                                                bdoc.getString(3),
                                                bdoc.getString(4)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                cargarCampos(documentos);
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

                            case 7:
                                ArrayList<InventarioDocumentos> prestamo = new ArrayList<InventarioDocumentos>();
                                for (int i = 0;i<bdoc.length();i+=10){
                                    try {
                                        prestamo.add(new InventarioDocumentos(
                                                bdoc.getInt(i),
                                                bdoc.getInt(i+1),
                                                bdoc.getInt(i+2),
                                                bdoc.getInt(i+3),
                                                bdoc.getInt(i+4),
                                                bdoc.getInt(i+5),
                                                bdoc.getInt(i+6),
                                                bdoc.getString(i+7),
                                                bdoc.getString(i+8),
                                                bdoc.getString(i+9)));

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                cargarCamposDevolucion(prestamo);
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

        cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_estado_documento.php", 3, estado);

        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void cargarCamposDevolucion(ArrayList list){
        ArrayList<InventarioDocumentos> inv = new ArrayList<InventarioDocumentos>();

        inv = list;

        for (int i = 0;i<docentes.size();i++){
            if(inv.get(0).getId_docente()==docentes.get(i).getId_docente()){
                acDocentes.setText(docentes.get(i).getNombreCompleto());
            }
        }

        if(inv.get(0).getTodo_ciclo()==1){
            cbCiclo.setChecked(true);
            cbAsignado.setChecked(false);
        }
        else if(inv.get(0).getEs_definitivo()==1){
            cbCiclo.setChecked(false);
            cbAsignado.setChecked(true);
        }
        else {
            cbCiclo.setChecked(false);
            cbAsignado.setChecked(false);
        }
        etFechaPrestamo.setText(inv.get(0).getFecha_desde());
        etFechaDevolucion.setText(inv.get(0).getFecha_hasta());
        edObservacions.setText(inv.get(0).getObservacion());

        String[] escuela = new String[1];
        String[] area = new String[1];
        String[] motivo = new String[1];

        for(int i=0; i<escuelas.size();i++){
            if(inv.get(0).getId_escuela()==escuelas.get(i).getId_escuela()){
                escuela[0]=escuelas.get(i).getEscuela();
            }
        }
        for(int i=0; i<areas.size();i++){
            if(inv.get(0).getId_area()==areas.get(i).getId_area()){
                area[0]=areas.get(i).getArea();
            }
        }
        for(int i=0; i<motivos.size();i++){
            if(inv.get(0).getId_motivo()==motivos.get(i).getId_motivos()){
                motivo[0]=motivos.get(i).getMotivos();
            }
        }

        ArrayAdapter escu = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, escuela);
        ArrayAdapter are = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, area);
        ArrayAdapter moti = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, motivo);

        spEscuelas.setAdapter(escu);
        spAreas.setAdapter(are);
        spMotivos.setAdapter(moti);

    }

    //Carga los nombres de los docentes obtenidos
    public void cargarDocentes(ArrayList list){
        this.docentes = list;
        docentesV = new String[docentes.size()];

        for (int i = 0; i<docentes.size();i++){
            docentesV[i] = docentes.get(i).getNombreCompleto();
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

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        this.estado = estado;
        edEstado.setText(estado);
        switch (estado){
            case "DISPONIBLE":
                lblAccion.setText("ACCION: REALIZAR PRESTAMO");
                btnAccion.setText("PRESTAR DOCUMENTO");

                etFechaPrestamo.setText(date);

                break;
            case "PRESTADO":
                lblAccion.setText("ACCION: DEVOLUCION DE DOCUMENTO");
                componentesDesabilitados();
                btnAccion.setText("DEVOLVER DOCUMENTO");
                cargarDatos("https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_devolver_documento.php", 7, String.valueOf(documentos.get(0).getId_bien()));
                etFechaDevolucion.setText(date);
                break;

            case "ASIGNADO":
                lblAccion.setText("ACCION: DEVOLUCION DE DOCUMENTO");
                componentesDesabilitados();
                btnAccion.setText("ASIGNADO PERMANENTEMENTE");
                btnAccion.setEnabled(false);
                break;

            case "EXTRAVIADO":
                lblAccion.setText("DOCUMENTO EXTRAVIADO");
                btnAccion.setText("ACCION NO DISPONIBLE");
                btnAccion.setEnabled(false);
        }

    }

    public void componentesDesabilitados(){
        acDocentes.setEnabled(false);
        cbCiclo.setEnabled(false);
        cbAsignado.setEnabled(false);
        etFechaPrestamo.setEnabled(false);
        etFechaDevolucion.setEnabled(false);
        edObservacions.setEnabled(false);
        spEscuelas.setEnabled(false);
        spMotivos.setEnabled(false);
        spAreas.setEnabled(false);
    }

    public void accionPrestar(){
        //Validaciones de los campos
        if(acDocentes.getText().toString().isEmpty() ||
                spEscuelas.getSelectedItemPosition()==0 ||
                spAreas.getSelectedItemPosition()==0 ||
                spMotivos.getSelectedItemPosition()==0){

            if(acDocentes.getText().toString().isEmpty()){
                acDocentes.setError("No ingreso ningun docente");
            }
            if(spEscuelas.getSelectedItemPosition()==0){
                Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar una escuela", Toast.LENGTH_SHORT).show();
            }
            if(spAreas.getSelectedItemPosition()==0){
                Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar una area", Toast.LENGTH_SHORT).show();
            }
            if(spMotivos.getSelectedItemPosition()==0){
                Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar un motivo", Toast.LENGTH_SHORT).show();
            }
        }
        if(cbCiclo.isChecked()==false && cbAsignado.isChecked()==false && etFechaDevolucion.getText().toString().isEmpty()){
            etFechaDevolucion.setError("Debe seleccionar una fecha de devolucion");
        }
        else {
            if(!acDocentes.getText().toString().isEmpty()){
                boolean docente = false;
                for(int i=0;i<docentesV.length;i++){
                    if(docentesV[i].equals(acDocentes.getText().toString())){
                        docente = true;
                        invDocumentos.setId_docente(docentes.get(i).getId_docente());
                    }
                }
                if(docente==false){
                    acDocentes.setError("Docente no registrado");
                }
                else {
                    etFechaDevolucion.setError(null);

                    invDocumentos.setId_documento(documentos.get(0).getId_documento());
                    invDocumentos.setId_bien(documentos.get(0).getId_bien());

                    invDocumentos.setId_area(areas.get(spAreas.getSelectedItemPosition()-1).getId_area());
                    invDocumentos.setId_motivo(motivos.get(spMotivos.getSelectedItemPosition()-1).getId_motivos());

                    if(cbAsignado.isChecked()==true){
                        invDocumentos.setEs_definitivo(1);
                        invDocumentos.setTodo_ciclo(0);
                        invDocumentos.setFecha_hasta("0000-00-00");
                    }
                    else if(cbCiclo.isChecked()==true){
                        invDocumentos.setEs_definitivo(0);
                        invDocumentos.setTodo_ciclo(1);
                        invDocumentos.setFecha_hasta("0000-00-00");
                    }
                    else {
                        invDocumentos.setEs_definitivo(0);
                        invDocumentos.setTodo_ciclo(0);
                    }

                    invDocumentos.setId_escuela(escuelas.get(spEscuelas.getSelectedItemPosition()-1).getId_escuela());
                    invDocumentos.setFecha_desde(etFechaPrestamo.getText().toString());
                    invDocumentos.setFecha_hasta(etFechaDevolucion.getText().toString());
                    invDocumentos.setObservacion(edObservacions.getText().toString());
                    invDocumentos.setId_estado(2);
                    Context contexto = getActivity().getApplicationContext();
                    invDocumentos.prestar(contexto);

                    if(InventarioDocumentos.resultado==1){
                        BuscarDocumento buscarDocumento = new BuscarDocumento();

                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.nav_host_fragment, new BuscarDocumento());
                        fr.commit();
                    }
                }
            }
        }
    }

    public void accionDevolver(){
        invDocumentos.setId_documento(documentos.get(0).getId_documento());
        invDocumentos.setId_bien(documentos.get(0).getId_bien());

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
