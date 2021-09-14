package com.programadoreshuacho.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    EditText txtPassword,txtUsuario;
    ImageView show_pass_btn;
    CheckBox txtCheck;
    Button btnLogin;
    private Object JSONObject;
    User usuario;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); /*OCULTAR TIULO*/
        getSupportActionBar().hide(); /*OCULTAR BARRA SUPERIOR*/
        setContentView(R.layout.activity_main);
        show_pass_btn = findViewById(R.id.show_pass_btn);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnLogin = findViewById(R.id.btnLogin);
        txtCheck = findViewById(R.id.estadoCheck);

        cargarPreferencias();

        /*FUNCION PARA OCULTAR Y VISUALIZAR PASSWORD*/
        show_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.show_pass_btn){
                    if(txtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(v)).setImageResource(R.drawable.invisible);
                        //Show Password
                        txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(v)).setImageResource(R.drawable.visibility);
                        //Hide Password
                        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            }
        });

        /* FUNCION PARA LOGEO*/
        RequestQueue queue = Volley.newRequestQueue(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(txtCheck.isChecked());
                iniciarSesion();
            }
        });

    }



    private void iniciarSesion(){
        /* EN EL MANIFEST SE AGREGO LA SGTE LINEA android:usesCleartextTraffic="true" ESTO SOLO SE HABILITA PARA HTTP*/
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();
        String url="http://192.168.0.2:8080/public/auth/login";
        //http://192.168.0.2/Proyectos/apiandroid/public/auth/login";
        //https://www.youtube.com/watch?v=EzRZVsw70Rk
        //https://www.youtube.com/watch?v=s8l5yJ9dlfQ&list=PLCTD_CpMeEKSiUQ_svD3ovD1qXdKQFMvO&index=10
        /* PARAMETROS A ENVIAR */
        Map<String,String> parametros = new HashMap<String, String>();
        parametros.put("usuario",txtUsuario.getText().toString());
        parametros.put("clave",txtPassword.getText().toString());
        JSONObject jsonParametros = new JSONObject(parametros);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        usuario =new User();
                        //System.out.println(response);
                        JSONArray jsonArray = response.optJSONArray("datos");
                        System.out.println(jsonArray);
                        JSONObject jsonObject = null;
                        try {
                            //System.out.println(response);
                            jsonObject = jsonArray.getJSONObject(0);
                            Boolean resp = jsonObject.optBoolean("resp");
                            //Integer respuesta = Integer.parseInt(resp);
                            System.out.println(resp);
                            //if (respuesta == 1){
                            if (resp){
                                Toast toast =Toast.makeText(getApplicationContext(),"Usuario logeado correctamente",Toast.LENGTH_SHORT);
                                toast.show();
                                /* SETEA LOS DATOS EN LA CLASE User.java*/
                                usuario.setUsuario(jsonObject.optString("usuario"));
                                usuario.setNombres(jsonObject.optString("nombres"));
                                usuario.setToken(jsonObject.optString("token"));
                                /*GUARDAMOS VARIABLES EN SESSION*/
                                guardarPreferencias(usuario.getUsuario(),usuario.getNombres(),usuario.getToken(),jsonObject.optBoolean("resp"));
                                /*ABRIMOS DASHBOARD ENVIANDO DATOS*/
                                Intent intencion = new Intent(getApplicationContext(),Dashboard.class);
                                intencion.putExtra(Dashboard.txtUsuarioBienvenida, usuario.getNombres());
                                startActivity(intencion);
                            }else{
                                /*GUARDAMOS VARIABLES EN SESSION*/
                                guardarPreferencias("","","",false);

                                Toast toast =Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectas",Toast.LENGTH_SHORT);
                                toast.show();

                            }


                        } catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                        Toast toast =Toast.makeText(getApplicationContext(),"error: "+error,Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        );
        queue.add(getRequest);

    }

    private void cargarPreferencias() {
        preferencias = getSharedPreferences("datosSession", Context.MODE_PRIVATE);
        String valorUsuario = preferencias.getString("sessionUsuario","");
        String valorClave = preferencias.getString("sessionClave","");
        Boolean valorCheck = preferencias.getBoolean("sessionCheck",false);
        Boolean valorLogin = preferencias.getBoolean("sessionLogin",false);
        if(valorLogin){
            /*ABRIMOS DASHBOARD ENVIANDO DATOS*/
            Intent intencion = new Intent(getApplicationContext(),Dashboard.class);
            intencion.putExtra(Dashboard.txtUsuarioBienvenida, valorUsuario);
            startActivity(intencion);
        }else{
            if(valorCheck){
                txtUsuario.setText(valorUsuario);
                txtPassword.setText(valorClave);
            }

        }

    }

    private void guardarPreferencias(String jsonUsuario,String jsonNombres,String jsonToken,Boolean jsonResp){
        SharedPreferences preferencias = getSharedPreferences("datosSession", Context.MODE_PRIVATE);
        String valorUsuario = txtUsuario.getText().toString();
        String valClave = txtUsuario.getText().toString();
        Boolean valCheck = txtCheck.isChecked();

        editor = preferencias.edit();
        editor.putString("sessionUsuario",valorUsuario);
        editor.putString("sessionClave",valClave);
        editor.putBoolean("sessionCheck",valCheck);
        editor.putString("sessionNombres",jsonNombres);
        editor.putString("sessionToken",jsonToken);
        editor.putBoolean("sessionLogin",jsonResp);

        editor.commit();

    }

}