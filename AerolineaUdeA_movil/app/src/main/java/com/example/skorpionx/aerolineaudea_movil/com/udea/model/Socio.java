package com.example.skorpionx.aerolineaudea_movil.com.udea.model;

import java.io.Serializable;

public class Socio implements Serializable{
    private int id;
    private String username;
    private String nombre;
    private String contraseña;
    private String apellidos;
    private String fecha_nacimiento;
    private String movil;
    private String correo_electronico;
    private String tarjeta_credito;
    private int numero_de_millas;

    public Socio(String username, String nombre, String contraseña, String apellidos,
                 String fecha_nacimiento, String movil, String tarjeta_credito,
                 String correo_electronico, int numero_de_millas) {
        this.username = username;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.movil = movil;
        this.tarjeta_credito = tarjeta_credito;
        this.correo_electronico = correo_electronico;
        this.numero_de_millas = numero_de_millas;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getTarjeta_credito() {
        return tarjeta_credito;
    }

    public void setTarjeta_credito(String tarjeta_credito) {
        this.tarjeta_credito = tarjeta_credito;
    }

    public int getNumero_de_millas() {
        return numero_de_millas;
    }

    public void setNumero_de_millas(int numero_de_millas) {
        this.numero_de_millas = numero_de_millas;
    }
}
