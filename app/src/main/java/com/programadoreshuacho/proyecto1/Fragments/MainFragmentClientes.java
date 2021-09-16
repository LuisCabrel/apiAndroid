package com.programadoreshuacho.proyecto1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.programadoreshuacho.proyecto1.AdapterCliente;
import com.programadoreshuacho.proyecto1.Clientes;
import com.programadoreshuacho.proyecto1.Dashboard;
import com.programadoreshuacho.proyecto1.R;
import com.programadoreshuacho.proyecto1.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragmentClientes extends Fragment {

    public static String txtUsuarioBienvenida="datosLogUsuario";
    String tokenizador;
    TextView txtBienvenida;
    SharedPreferences preferencias;
    /*   https://www.youtube.com/watch?v=FnG95Jy3I1c&ab_channel=AlexNarv%C3%A1ezProgramming --> listCard */
    private static final String URL_listaclientes="http://192.168.0.8:8080/public/api/clientes";
    /*LISTA PARA ALMACENAR TODOS LOS CLIENTES*/
    ArrayList<Clientes> clientesList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_dashboard,container,false);

        User token =new User();
        tokenizador = token.getUsuario();

        /*BUSCAMOS LOS DATOS E SESSION*/
        preferencias = this.getContext().getSharedPreferences("datosSession", Context.MODE_PRIVATE);
        String valorToken = preferencias.getString("sessionToken","");
        String valorUsuario = preferencias.getString("sessionUsuario","");
        tokenizador =valorToken;
        System.out.println(valorToken);
        txtBienvenida = (TextView) view.findViewById(R.id.txtUsuarioLog);
        txtBienvenida.setText("Bienvenido "+valorUsuario);

        /**/
        recyclerView =(RecyclerView) view.findViewById(R.id.rvListaClientes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        clientesList = new ArrayList<>();
        loadclientes();

        return  view;
    }

    private void loadclientes() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_listaclientes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //CONVERTIMOS EL STRING A JSON ARRAY OBJECT
                            JSONArray array = new JSONArray(response);
                            System.out.println(array);
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
                            System.out.println(clientesList);
                            AdapterCliente adapterCliente = new AdapterCliente(clientesList);
                            recyclerView.setAdapter(adapterCliente);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast =Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT);
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
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
