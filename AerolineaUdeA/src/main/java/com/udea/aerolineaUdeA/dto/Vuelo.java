
package com.udea.aerolineaUdeA.dto;

public class Vuelo {
    String origen;
    String destino;
    String fecha;
    int duracion;

    public Vuelo(String origen, String destino, String fecha, int duracion) {
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    
    
    
}
