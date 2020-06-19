package com.example.inventario;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class InventarioDocumentos extends Documentos {

    private int id_prestamo, id_bien, id_area, id_docente, id_motivo, todo_ciclo, es_definitivo, id_escuela, resultado;
    private String fecha_desde, fecha_hasta, observacion;

    public InventarioDocumentos(){}

    public InventarioDocumentos(int id_documento, int id_bien, int id_estado, String titulo, String isbn){
        this.id_documento = id_documento;
        this.id_bien = id_bien;
        this.id_estado = id_estado;
        this.titulo = titulo;
        this.isbn = isbn;
    }

    public InventarioDocumentos(int id_prestamo, int id_area, int id_docente, int id_motivo, int todo_ciclo, int es_definitivo, int id_escuela, String fecha_desde, String fecha_hasta, String observacion){
        this.id_prestamo = id_prestamo;
        this.id_area = id_area;
        this.id_docente = id_docente;
        this.id_motivo = id_motivo;
        this.todo_ciclo = todo_ciclo;
        this.es_definitivo = es_definitivo;
        this.id_escuela = id_escuela;
        this.fecha_desde = fecha_desde;
        this.fecha_hasta = fecha_hasta;
        this.observacion = observacion;
    }

    //Setters


    public void setId_bien(int id_bien) {
        this.id_bien = id_bien;
    }

    public void setId_area(int id_area) {
        this.id_area = id_area;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public void setTodo_ciclo(int todo_ciclo) {
        this.todo_ciclo = todo_ciclo;
    }

    public void setEs_definitivo(int es_definitivo) {
        this.es_definitivo = es_definitivo;
    }

    public void setId_escuela(int id_escuela) {
        this.id_escuela = id_escuela;
    }

    public void setFecha_desde(String fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    //Getters
    public int getId_bien() {
        return id_bien;
    }

    public int getId_area() {
        return id_area;
    }

    public int getId_docente() {
        return id_docente;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public int getTodo_ciclo() {
        return todo_ciclo;
    }

    public int getEs_definitivo() {
        return es_definitivo;
    }

    public int getId_escuela() {
        return id_escuela;
    }

    public String getFecha_desde() {
        return fecha_desde;
    }

    public String getFecha_hasta() {
        return fecha_hasta;
    }

    public String getObservacion() {
        return observacion;
    }

    public int getResultado() {
        return resultado;
    }

    public void prestar(final Context context){
        String URL = "http://www.ingenieriadesistemasinformaticos.com/ws_bg17016/ws_prestar_documento.php";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    resultado = 1;
                    Toast.makeText(context, "Prestamo realizado", Toast.LENGTH_LONG).show();
                }
                else {
                    resultado = 0;
                    Toast.makeText(context, "Error al realizar prestamo", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_documento", String.valueOf(id_documento));
                parametros.put("id_bien",String.valueOf(id_bien));
                parametros.put("id_area",String.valueOf(id_area));
                parametros.put("id_docente",String.valueOf(id_docente));
                parametros.put("id_motivo",String.valueOf(id_motivo));
                parametros.put("todo_ciclo",String.valueOf(todo_ciclo));
                parametros.put("es_definitivo",String.valueOf(es_definitivo));
                parametros.put("id_escuela",String.valueOf(id_escuela));
                parametros.put("fecha_desde",fecha_desde);
                parametros.put("fecha_hasta",fecha_hasta);
                parametros.put("observacion",observacion);
                parametros.put("id_estado",String.valueOf(id_estado));

                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
}
