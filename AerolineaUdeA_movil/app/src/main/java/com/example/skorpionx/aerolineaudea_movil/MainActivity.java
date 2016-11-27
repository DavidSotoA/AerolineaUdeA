package com.example.skorpionx.aerolineaudea_movil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolieaUdeA.socio.RegistroActivity;
import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.Servicios.ServiociosAerolinea;
import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.util.Constantes;
import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Resultado;
import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Socio;
import com.google.gson.Gson;

import java.io.Serializable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG=MainActivity.class.getSimpleName();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cargarInformacion();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarInformacion();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void callUpdate(Socio socio){
        Intent intent = new Intent(this, UpdateSocioActivity.class);
        intent.putExtra("Socio",  socio);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_registro) {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_actualizarInfo){
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String token = prefs.getString("token", "no_token");
            progress = new ProgressDialog(this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle(Constantes.PROGRESS_LOGIN);
            progress.setMessage(Constantes.PROGRESS_BAR_ACTUALIZAR_INFORMACION);
            progress.show();

            ServiociosAerolinea.getAutenticacionService().datosSocio(token,
                    new Callback<Resultado>(){

                        @Override
                        public void success(Resultado resultado, Response response) {
                            if (resultado.getTipo().equals("Success")) {
                                Gson gson=new Gson();
                                Socio socio = gson.fromJson(resultado.getMensaje(), Socio.class);
                                progress.dismiss();
                                callUpdate(socio);
                            }
                            if(resultado.getTipo().equals("Error")){
                                progress.dismiss();
                                Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), "Ocurrio un problema intente mas tarde", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progress.dismiss();
                            Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), Constantes.CONEXION_ERROR, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

        }
        else if (id == R.id.nav_sesion) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String token = prefs.getString("token", "no_token");
            System.out.println("token: "+token);
            if(token.equals("no_token")){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }else{
                progress = new ProgressDialog(this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setTitle(Constantes.PROGRESS_LOGIN);
                progress.setMessage(Constantes.PROGRESS_BAR_CERRAR_SESION);
                progress.show();
                ServiociosAerolinea.getAutenticacionService().cerrarSesion(token,
                        new Callback<Resultado>() {

                            @Override
                            public void success(Resultado resultado, Response response) {
                                if (resultado.getTipo().equals("Success")) {
                                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    progress.dismiss();
                                    cargarInformacion();
                                }else{
                                    progress.dismiss();
                                    Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), "Ocurrio un problema intente mas tarde", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progress.dismiss();
                                Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), Constantes.CONEXION_ERROR, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargarInformacion(){
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "no_token");

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);

        MenuItem item = nv.getMenu().getItem(0);
        Menu menu=nv.getMenu();
        TextView t_usuario = (TextView) findViewById(R.id.nav_headertwo);

        if(token.equals("no_token")){
            Log.d(TAG, "No_getAccount");
            item.setTitle(R.string.iniciar_sesion);
            item.setIcon(R.drawable.ic_login);
            //menu.add(90, 1, 4, R.string.nav_registro).setIcon(R.drawable.ic_registro);
        }else{
            Log.d(TAG, "getAccount");
            //t_usuario.setText(usuario);
            item.setTitle(R.string.cerrar_sesion);
            item.setIcon(R.drawable.ic_logout);

        }
    }


}
