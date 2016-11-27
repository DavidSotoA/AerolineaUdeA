package com.udea.aerolineaUdeA.webServices;

import com.udea.aerolineaUdeA.dto.Socio;
import com.udea.aerolineaUdeA.modelo.ConexionBD;
import com.udea.aerolineaUeA.util.RespuestaService;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("socio")
public class Login {

    @GET
    @Path("datosSocio")
    @Produces({"application/json"})
    public RespuestaService datosSocio(@HeaderParam("token") String token){
        ConexionBD sesion = new ConexionBD();
        return sesion.datosSocio(token);
    }

    @POST
    @Path("registrarSocio")
    @Produces({"application/json"})
    public RespuestaService registrarUsuario(@HeaderParam("contrasena") String contraseña,
            @HeaderParam("username") String username,
            @HeaderParam("nombre") String nombre,
            @HeaderParam("apellidos") String apellidos,
            @HeaderParam("fecha_nacimiento") String fecha_nacimiento,
            @HeaderParam("movil") String movil,
            @HeaderParam("correo_electronico") String correo_electronico,
            @HeaderParam("tarjeta_de_credito") String tarjeta_de_credito
    ) {
        ConexionBD sesion = new ConexionBD();
        System.out.println("username: " + username);
        Socio socio = new Socio(username, nombre, contraseña, apellidos, fecha_nacimiento,
                movil, correo_electronico, tarjeta_de_credito);
        return sesion.insertSocio(socio);
    }

    @PUT
    @Path("login")
    @Produces({"application/json"})
    public RespuestaService iniciarSesion(@HeaderParam("username") String username,
            @HeaderParam("contrasena") String contraseña) {
        ConexionBD sesion = new ConexionBD();
        return sesion.iniciarSesion(username, contraseña);
    }

    @PUT
    @Path("logout")
    @Produces({"application/json"})
    public RespuestaService cerrarSesion(@HeaderParam("token") String token) {
        ConexionBD sesion = new ConexionBD();
        return sesion.cerrarSesion(token);
    }

    @PUT
    @Path("actualizarSocio")
    @Produces({"application/json"})
    public RespuestaService actualizarSocio(@HeaderParam("contrasena") String contraseña,
            @HeaderParam("contrasena_vigente") String contraseñaVigente,
            @HeaderParam("username") String username,
            @HeaderParam("nombre") String nombre,
            @HeaderParam("apellidos") String apellidos,
            @HeaderParam("fecha_nacimiento") String fecha_nacimiento,
            @HeaderParam("movil") String movil,
            @HeaderParam("correo_electronico") String correo_electronico,
            @HeaderParam("username_vigente") String usernameVigente,
            @HeaderParam("tarjeta_de_credito") String tarjeta_de_credito
    ) {
        ConexionBD sesion = new ConexionBD();
        Socio socio = new Socio(username, nombre, contraseña, apellidos, fecha_nacimiento,
                movil, correo_electronico, tarjeta_de_credito);
        return sesion.actualizarSocio(socio, usernameVigente, contraseñaVigente);
    }
}
