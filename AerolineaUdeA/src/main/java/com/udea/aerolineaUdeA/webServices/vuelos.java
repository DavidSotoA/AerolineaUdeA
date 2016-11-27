
package com.udea.aerolineaUdeA.webServices;

import com.udea.aerolineaUdeA.modelo.ConexionBD;
import com.udea.aerolineaUeA.util.RespuestaService;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("vuelo")
public class vuelos {
      
    @GET
    @Path("consultarVuelo")
    @Produces({"application/json"})
    public RespuestaService datosSocio(@HeaderParam("fecha_inicio") String fecha_inicio,
                                       @HeaderParam("fecha_fin") String fecha_fin,
                                       @HeaderParam("tipo_asiento") String tipo_asiento,
                                       @HeaderParam("origen") int origen,
                                       @HeaderParam("destino") int destino){
        ConexionBD sesion = new ConexionBD();
        return sesion.consultaVuelo(fecha_inicio,fecha_fin,tipo_asiento,origen,destino);
        
    }
    
    
    
}
