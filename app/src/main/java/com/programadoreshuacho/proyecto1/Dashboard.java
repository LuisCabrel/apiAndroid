package com.programadoreshuacho.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    public static String txtUsuarioBienvenida="datosLogUsuario";
    String tokenizador;
    TextView txtBienvenida;
    SharedPreferences preferencias;
    /*
    https://www.youtube.com/watch?v=FnG95Jy3I1c&ab_channel=AlexNarv%C3%A1ezProgramming
     */

    private static final String URL_listaclientes="http://192.168.0.2:8080/public/api/clientes";
    /*LISTA PARA ALMACENAR TODOS LOS CLIENTES*/
    List<Clientes> clientesList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtBienvenida = (TextView)findViewById(R.id.txtUsuarioLog);
        /*RECUPERAMOS LOS DATOS DE SESSION*/
        System.out.println(txtUsuarioBienvenida);
        User token =new User();
        tokenizador = token.getUsuario();
        System.out.println("Token: "+token.getToken());
        String usuario = getIntent().getStringExtra("datosLogUsuario");
        System.out.println(usuario);
        txtBienvenida.setText("Bienvenido "+usuario);

        /*BUSCAMOS LOS DATOS E SESSION*/
        preferencias = getSharedPreferences("datosSession", Context.MODE_PRIVATE);
        String valorToken = preferencias.getString("sessionToken","");
        tokenizador =valorToken;
        System.out.println(valorToken);

        /**/
        recyclerView =(RecyclerView)findViewById(R.id.rvListaClientes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        clientesList = new ArrayList<>();
        loadclientes();


    }

    private void loadclientes() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_listaclientes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //CONVERTIMOS EL STRING A JSON ARRAY OBJECT
                            JSONArray array = new JSONArray(response);

                            //NAVEGAMOS A TRAVEZ DE LOS OBJETOS
                            for (int i=0;i<array.length();i++){
                                JSONObject clients = array.getJSONObject(i);
                                clientesList.add(new Clientes(
                                        clients.getInt("id"),
                                        clients.getString("nombre"),
                                        clients.getString("apellido"),
                                        clients.getString("telefono"),
                                        clients.getString("correo"),
                                        clients.getString("imagen")
                                ));
                            }

                            AdapterCliente adapterCliente = new AdapterCliente(Dashboard.this,clientesList);
                            recyclerView.setAdapter(adapterCliente);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast =Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        ){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer "+tokenizador);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}