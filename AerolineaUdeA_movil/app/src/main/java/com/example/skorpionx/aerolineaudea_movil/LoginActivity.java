package com.example.skorpionx.aerolineaudea_movil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.Servicios.ServiociosAerolinea;
import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.util.Constantes;
import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Resultado;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout input_username;
    private TextInputLayout input_contraseña;
    private Button button_login;
    ProgressDialog progress;

    String username;
    String contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        input_username = (TextInputLayout) findViewById(R.id.input_nombreLogin);
        input_contraseña = (TextInputLayout) findViewById(R.id.input_passwordLogin);
        button_login = (Button) findViewById(R.id.button_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_login.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void callMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==findViewById(R.id.button_login).getId()){
            if(validation()){
                progress = new ProgressDialog(this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setTitle(Constantes.PROGRESS_LOGIN);
                progress.setMessage(Constantes.PROGRESS_BAR_REGISTRO_MESSAGE);
                progress.show();
                ServiociosAerolinea.getAutenticacionService().iniciarSesion(username,contraseña,
                        new Callback<Resultado>() {
                            @Override
                            public void success(Resultado resultado, Response response) {
                                if (resultado.getTipo().equals("Success")) {
                                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("token",resultado.getMensaje());
                                    editor.putString("contraseña",contraseña);
                                    editor.putString("user",username);
                                    editor.commit();
                                    progress.dismiss();
                                    callMain();
                                }

                                if(resultado.getTipo().equals("Error")){
                                    progress.dismiss();
                                    Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), "No a sido posible realizar iniciar sesion, intenta mas tarde", Toast.LENGTH_SHORT);
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
    }

    public boolean validation(){
        boolean formOk=true;
        username= input_username.getEditText().getText().toString();
        contraseña= input_contraseña.getEditText().getText().toString();

        if(username.length()==0) {
            input_username.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_username.setErrorEnabled(false);
        }
        if(contraseña.length()==0){
            input_contraseña.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_contraseña.setErrorEnabled(false);
        }
        return formOk;
    }
}
