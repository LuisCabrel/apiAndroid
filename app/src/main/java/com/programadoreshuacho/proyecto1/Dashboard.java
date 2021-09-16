package com.programadoreshuacho.proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.programadoreshuacho.proyecto1.Fragments.MainFragment;
import com.programadoreshuacho.proyecto1.Fragments.MainFragmentClientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static String txtUsuarioBienvenida="datosLogUsuario";

    /* https://www.youtube.com/watch?v=0EIU5R_zHUc --> menu_lateral*/

    /*VARIABLE PARA MENU LATERAL*/
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    /*FIN DE VARIABLES MENU LATERAL*/
    /*VARIABLES PARA LEVANTAR FRAGMENT*/
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView usuariotxt,perfiltxt;
    SharedPreferences preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardprincipal);

        /* INICIO TODO PARA EL MENU LATERAL */
        /* CLONAR LOS STYLE DE THEME theme/theme.xml Y SE CAMBIA DE NOMBRE*/
        /* EN MANIFEST AGREGAR EL THEMA MODIFICADO*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        /*ESTABLECER EVENTO CLICK AL NAVIGATIONVIEW*/
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        /* FIN TODO PARA EL MENU LATERAL */
        /* CARGAR FRAGMENT PRINCIPAL POR DEFECTO*/
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contenedorFragment,new MainFragment());
        fragmentTransaction.commit();
        setTitle("Home");
        /* FIN CARGAR FRAGMENT PRINCIPAL*/

        /*BUSCAMOS LOS DATOS E SESSION Y COLOCAMOS EN LA CABECERA DEL DRAWER*/
        preferencias = this.getSharedPreferences("datosSession", Context.MODE_PRIVATE);
        String valorUsuario = preferencias.getString("sessionUsuario","");
        String ValorPerfil = preferencias.getString("sessionNombres","");
        View hView = navigationView.getHeaderView(0);
        usuariotxt = (TextView) hView.findViewById(R.id.tituloHeader);
        perfiltxt = (TextView) hView.findViewById(R.id.usuarioHead);
        usuariotxt.setText(valorUsuario);
        perfiltxt.setText(ValorPerfil);






    }



    /*VALIDAMOS OPCIONES DE MENU PARA ENVIAR EL FRAGMENT CORRECTO*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedorFragment,new MainFragment());
            fragmentTransaction.commit();

        }
        if(menuItem.getItemId() == R.id.clientes){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedorFragment,new MainFragmentClientes());
            fragmentTransaction.commit();
        }
        setTitle(menuItem.getTitle());
        return false;
    }
}