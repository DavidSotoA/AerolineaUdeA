package com.example.skorpionx.aerolineaudea_movil.com.udea.aerolineaUdeA.Servicios;

import com.example.skorpionx.aerolineaudea_movil.com.udea.model.Resultado;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;

public interface SesionSocio {

    @GET("/socio/datosSocio")
    void datosSocio(
            @Header("token") String token,
            Callback<Resultado> cb
    );

    @POST("/socio/registrarSocio")
    void registrarUsuario(
            @Header("username") String username,
            @Header("nombre") String nombre,
            @Header("apellidos") String apellidos,
            @Header("fecha_nacimiento") String fecha_nacimiento,
            @Header("movil") String movil,
            @Header("correo_electronico") String correo_electronico,
            @Header("tarjeta_de_credito") String tarjeta_de_credito,
            @Header("contrasena") String contrasena,
            Callback<Resultado> cb);

    @PUT("/socio/login")
    void iniciarSesion(
            @Header("username") String username,
            @Header("contrasena") String contrasena,
            Callback<Resultado> cb
    );

    @PUT ("/socio/logout")
    void cerrarSesion(
            @Header("token") String token,
            Callback<Resultado> cb
    );

    @PUT("/socio/actualizarSocio")
    void actualizarSocio(
            @Header("username") String username,
            @Header("nombre") String nombre,
            @Header("apellidos") String apellidos,
            @Header("fecha_nacimiento") String fecha_nacimiento,
            @Header("movil") String movil,
            @Header("correo_electronico") String correo_electronico,
            @Header("tarjeta_de_credito") String tarjeta_de_credito,
            @Header("contrasena") String contrasena,
            @Header("username_vigente") String username_vigente,
            @Header("contrasena_vigente") String contraseña_vigente,
            Callback<Resultado> cb
    );
}
