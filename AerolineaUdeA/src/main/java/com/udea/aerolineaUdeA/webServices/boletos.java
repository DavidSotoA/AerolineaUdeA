
package com.udea.aerolineaUdeA.webServices;

import com.udea.aerolineaUdeA.modelo.ConexionBD;
import com.udea.aerolineaUeA.util.RespuestaService;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("boletos")
public class boletos {
    
    @POST
    @Path("agregarBoleto")
    @Produces({"application/json"})
    public RespuestaService datosSocio(@HeaderParam("id_asiento") int id_asiento,
                                       @HeaderParam("id_cliente") int id_cliente){
        ConexionBD sesion = new ConexionBD();
        return sesion.agregarBoleto(id_asiento,id_cliente);
    }
    
    @PUT
    @Path("checkin")
    @Produces({"application/json"})
    public RespuestaService checkin(@HeaderParam("id_boleto") int id_boleto,
                                       @HeaderParam("id_cliente") int id_cliente){
        ConexionBD sesion = new ConexionBD();
        return sesion.checkin(id_boleto,id_cliente);
    }
}
