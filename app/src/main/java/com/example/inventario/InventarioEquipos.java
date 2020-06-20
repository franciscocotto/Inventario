package com.example.inventario;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InventarioEquipos extends Equipos {
    private static boolean resultado=true;
    private int id_prestamo, id_bien, id_area, id_docente, id_motivo, todo_ciclo, es_definitivo, id_escuela;
    private String fecha_desde, fecha_hasta, observacion;

    public InventarioEquipos(){}

    public InventarioEquipos(int id_equipo, int id_bien, int id_estado, String modelo, String num_inventario){
        this.id_equipo = id_equipo;
        this.id_bien = id_bien;
        this.id_estado = id_estado;
        this.modelo = modelo;
        this.num_inventario = num_inventario;
    }

    public int getId_bien() {
        return id_bien;
    }

    public int getId_prestamo() {
        return id_prestamo;
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


    public void setId_bien(int id_bien) {
        this.id_bien = id_bien;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
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

    public static boolean isResultado() {
        return resultado;
    }

    public static void setResultado(boolean resultado) {
        InventarioEquipos.resultado = resultado;
    }


    public void prestar(final Context context){
        String URL = "https://invetariopdm115.000webhostapp.com/ws_bg17016/ws_prestar_equipo.php";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                InventarioDocumentos.resultado=false;
                            } else {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_equipo", String.valueOf(id_equipo));
                params.put("id_bien",String.valueOf(id_bien));
                params.put("id_area",String.valueOf(id_area));
                params.put("id_docente",String.valueOf(id_docente));
                params.put("id_motivo",String.valueOf(id_motivo));
                params.put("todo_ciclo",String.valueOf(todo_ciclo));
                params.put("es_definitivo",String.valueOf(es_definitivo));
                params.put("id_escuela",String.valueOf(id_escuela));
                params.put("fecha_desde",fecha_desde);
                params.put("fecha_hasta",fecha_hasta);
                params.put("observacion",observacion);
                params.put("id_estado",String.valueOf(id_estado));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
