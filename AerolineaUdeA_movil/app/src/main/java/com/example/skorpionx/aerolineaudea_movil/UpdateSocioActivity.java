package com.example.skorpionx.aerolineaudea_movil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.Servicios.ServiociosAerolinea;
import com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.util.Constantes;
import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Resultado;
import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Socio;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateSocioActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private TextInputLayout input_nombre;
    private TextInputLayout input_apellidos;
    private TextInputLayout input_contraseña_antigua;
    private TextInputLayout input_contraseña_nueva;
    private TextInputLayout input_movil;
    private TextInputLayout input_correo_electronico;
    private TextInputLayout input_tarjeta_de_credito;
    private TextInputLayout input_username;

    private Button actualizar;
    private  Button fecha_botton;
    private EditText fecha_text;

    String nombre;
    String username;
    String apellidos;
    String contraseña_vigente;
    String contraseña_antigua;
    String contraseña_nueva;
    String username_vigente;
    String movil;
    String correo;
    String tarjeta;
    String fecha_nacimiento;

    int año;
    int mes;
    int dia;
    ProgressDialog progress;
    Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_socio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        socio= (Socio) intent.getSerializableExtra("Socio");
        username_vigente=socio.getUsername();
        contraseña_vigente=socio.getContraseña();

        input_nombre = (TextInputLayout) findViewById(R.id.input_nombre_update);
        input_username = (TextInputLayout) findViewById(R.id.input_username_update);
        input_apellidos = (TextInputLayout) findViewById(R.id.input_apellidos_update);
        input_contraseña_antigua = (TextInputLayout) findViewById(R.id.input_contraseña_update);
        input_contraseña_nueva = (TextInputLayout) findViewById(R.id.input_contraseña2_update);
        input_movil = (TextInputLayout) findViewById(R.id.input_movil_update);
        input_correo_electronico = (TextInputLayout) findViewById(R.id.input_correo_update);
        input_tarjeta_de_credito = (TextInputLayout) findViewById(R.id.input_tarjeta_credito_update);
        fecha_text= (EditText) findViewById(R.id.input_calendar_text_update);
        actualizar=(Button) findViewById(R.id.input_update);
        fecha_botton=(Button) findViewById(R.id.input_calendar_button_update);


        actualizar.setOnClickListener(this);
        fecha_botton.setOnClickListener(this);

    }

    public void actionCalendar(){

        final Calendar c = Calendar.getInstance();
        año = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fecha_text.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, año, mes, dia);
        datePickerDialog.show();
    }

    public boolean actionRegistrarse(){
        boolean formOk=true;
        username= input_username.getEditText().getText().toString();
        nombre = input_nombre.getEditText().getText().toString();
        apellidos = input_apellidos.getEditText().getText().toString();
        contraseña_nueva = input_contraseña_nueva.getEditText().getText().toString();
        contraseña_antigua=input_contraseña_antigua.getEditText().getText().toString();
        movil = input_movil.getEditText().getText().toString();
        correo = input_correo_electronico.getEditText().getText().toString();
        tarjeta = input_tarjeta_de_credito.getEditText().getText().toString();
        fecha_nacimiento=fecha_text.getText().toString();

        if(username.length()==0) {
            input_username.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else {
            input_username.setErrorEnabled(false);
        }if(nombre.length()==0) {
            input_nombre.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_nombre.setErrorEnabled(false);
        }
        if(apellidos.length()==0){
            input_apellidos.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_apellidos.setErrorEnabled(false);
        }
        if(contraseña_nueva.length()<5){
            input_contraseña_nueva.setError(Constantes.ERROR_LENGTH_PASSWORD);
            formOk=false;
        }else{
            input_contraseña_nueva.setErrorEnabled(false);
        }
        if(movil.length()==0){
            input_movil.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_movil.setErrorEnabled(false);
        }
        if(tarjeta.length()==0){
            input_tarjeta_de_credito.setError(Constantes.ERROR_FORM_EMPTY_FIELD);
            formOk=false;
        }else{
            input_tarjeta_de_credito.setErrorEnabled(false);
        }
        if (!validarCorreo(correo)) {
            input_correo_electronico.setError(Constantes.ERROR_FORM_EMAIL_PATTERN);
            formOk=false;
        }else{
            input_correo_electronico.setErrorEnabled(false);
        }
        if(!contraseña_antigua.equals(contraseña_vigente)){
            input_contraseña_antigua.setError(Constantes.ERROR_PASSWORDS_NOT_EQUAL);
            formOk=false;
        }
        return formOk;
    }

    public boolean validarCorreo(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;

        matcher = pattern.matcher(email);
        return matcher.matches();
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

    @Override
    public void onClick(View view) {
        if(view.getId()==findViewById(R.id.input_update).getId()){
            if(actionRegistrarse()){
                progress = new ProgressDialog(this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setTitle(Constantes.PROGRESS_BAR_REGISTRO);
                progress.setMessage(Constantes.PROGRESS_BAR_REGISTRO_MESSAGE);

                fecha_nacimiento=año+"-"+mes+"-"+dia;
                progress.show();

                ServiociosAerolinea.getAutenticacionService().actualizarSocio(username,nombre,apellidos,fecha_nacimiento,
                        movil,correo,tarjeta,contraseña_nueva,username_vigente,contraseña_vigente,
                        new Callback<Resultado>(){

                            @Override
                            public void success(Resultado resultado, Response response) {
                                if (resultado.getTipo().equals("Success")) {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    DialogoAlerta dialogo = new DialogoAlerta();
                                    dialogo.show(fragmentManager, "tagAlerta");
                                }
                                if(resultado.getTipo().equals("Error")){
                                    Toast toast = Toast.makeText(AerolineaUdeA.getAppContext(), "No a sido posible realizar el registro, intenta mas tarde", Toast.LENGTH_SHORT);
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
        }else if(view.getId()==findViewById(R.id.input_calendar_button_update).getId()){
            actionCalendar();
        }
    }

    public class DialogoAlerta extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Información actualizada satisfactoriamente.")
                    .setTitle("Actualización completada")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            dialog.cancel();
                            startActivity(intent);
                            finish();
                        }
                    });

            return builder.create();
        }
    }
}
