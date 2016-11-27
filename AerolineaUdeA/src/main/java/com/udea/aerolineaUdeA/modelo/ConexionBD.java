package com.udea.aerolineaUdeA.modelo;

import com.google.gson.Gson;
import com.udea.aerolineaUdeA.dto.Socio;
import com.udea.aerolineaUdeA.dto.Vuelo;
import com.udea.aerolineaUeA.util.Constantes;
import com.udea.aerolineaUeA.util.RespuestaService;
import com.udea.aerolineaUeA.util.TokenGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    public RespuestaService insertSocio(Socio socio) {

        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, 
                    Constantes.USER, Constantes.PASS);

            String query = " insert into pasajero (username,contraseña,"
                    + " nombres, apellidos, fecha_nacimiento"
                    + ", movil, email,tarjeta_de_credito,socio)"
                    + " values (?,?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, socio.getUsername());
            preparedStmt.setString(2, socio.getContraseña());
            preparedStmt.setString(3, socio.getNombre());
            preparedStmt.setString(4, socio.getApellidos());
            preparedStmt.setString(5, socio.getFecha_nacimiento());
            preparedStmt.setString(6, socio.getMovil());
            preparedStmt.setString(7, socio.getCorreo_electronico());
            preparedStmt.setString(8, socio.getTarjeta_de_credito());
            preparedStmt.setBoolean(9, true);

            preparedStmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (preparedStmt != null) {
                try {
                    preparedStmt.close();
                } catch (SQLException logOrIgnore) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return new RespuestaService(RespuestaService.SUCCESS, Constantes.USER_REGISTER);
    }

    public RespuestaService cerrarSesion(String token) {
        Connection conn = null;
        boolean resp = false;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, 
                    Constantes.USER, Constantes.PASS);

            Statement stmt = conn.createStatement();
            String query = "SELECT count(*) as cant FROM pasajero WHERE "
                    + "token='" + token + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.first()) {
                if (rs.getString("cant").equals("1")) {
                    resp = true;
                }
            }

            if (resp) {
                query = "UPDATE pasajero SET token=NULL WHERE token=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, token);
                preparedStmt.execute();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        if (resp) {
            return new RespuestaService(RespuestaService.SUCCESS, Constantes.FINISH_SESION);
        } else {
            return new RespuestaService(RespuestaService.ERROR, Constantes.USER_NO_FIND);
        }
    }

    public RespuestaService iniciarSesion(String username, String contrasena) {
        Connection conn = null;
        String token = null;
        boolean resp = false;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, Constantes.USER, 
                    Constantes.PASS);

            Statement stmt = conn.createStatement();
            String query = "SELECT count(*) as cant FROM pasajero WHERE "
                    + "username='" + username + "' and contraseña='" 
                    + contrasena + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.first()) {
                if (rs.getString("cant").equals("1")) {
                    resp = true;
                }
            }

            if (resp) {

                stmt = conn.createStatement();
                query = "SELECT token FROM pasajero ";
                rs = stmt.executeQuery(query);

                do {
                    token = TokenGenerator.generarToken();
                } while (existToken(token, rs));

                query = "UPDATE pasajero SET token=? WHERE username=? and contraseña=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, token);
                preparedStmt.setString(2, username);
                preparedStmt.setString(3, contrasena);
                preparedStmt.execute();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        if (resp) {
            return new RespuestaService(RespuestaService.SUCCESS, token);
        } else {
            return new RespuestaService(RespuestaService.ERROR, Constantes.USER_NO_FIND);
        }
    }

    public RespuestaService datosSocio(String token) {
        Connection conn = null;
        Socio socio;
        String socioJson;
        Gson gson = new Gson();
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, Constantes.USER, Constantes.PASS);
            Statement stmt = conn.createStatement();
            
            String query = "SELECT username,nombres,apellidos,fecha_nacimiento,movil,email,contraseña,tarjeta_de_credito FROM pasajero WHERE token='"+token+"'";
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            socio= new Socio(rs.getString("username"),
                             rs.getString("nombres"),
                             rs.getString("contraseña"),
                             rs.getString("apellidos"),
                             rs.getString("fecha_nacimiento"),
                             rs.getString("movil"),
                             rs.getString("email"),
                             rs.getString("tarjeta_de_credito"));
     
            socioJson=gson.toJson(socio);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return new RespuestaService(RespuestaService.SUCCESS, socioJson);
    }

    public RespuestaService actualizarSocio(Socio socio, String usernameVigente, String contraseñaVigente) {
        Connection conn = null;
        String token = null;
        boolean resp = false;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, Constantes.USER, Constantes.PASS);

            Statement stmt = conn.createStatement();
            String query = "SELECT count(*) as cant FROM pasajero WHERE username='" + usernameVigente + "' and contraseña='" + contraseñaVigente + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.first()) {
                if (rs.getString("cant").equals("1")) {
                    resp = true;
                }
            }

            if (resp) {
                query = "UPDATE pasajero SET username=?,nombres=?,apellidos=?,fecha_nacimiento=?,movil=?,email=?,contraseña=?,tarjeta_de_credito=? WHERE username=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);

                preparedStmt.setString(1, socio.getUsername());
                preparedStmt.setString(2, socio.getNombre());
                preparedStmt.setString(3, socio.getApellidos());
                preparedStmt.setString(4, socio.getFecha_nacimiento());
                preparedStmt.setString(5, socio.getMovil());
                preparedStmt.setString(6, socio.getCorreo_electronico());
                preparedStmt.setString(7, socio.getContraseña());
                preparedStmt.setString(8, socio.getTarjeta_de_credito());
                preparedStmt.setString(9, usernameVigente);
                preparedStmt.execute();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        if (resp) {
            return new RespuestaService(RespuestaService.SUCCESS, Constantes.UPDATE_USER);
        } else {
            return new RespuestaService(RespuestaService.ERROR, Constantes.USER_NO_FIND);
        }

    }

    public boolean existToken(String token, ResultSet tokens) throws SQLException {
        while (!tokens.next()) {
            if (tokens.getString("token").equals(token)) {
                tokens.first();
                return true;
            }
        }
        return false;
    }
    
    public RespuestaService consultaVuelo(String fecha_inicio, String fecha_fin, String tipo_asiento, int origen, int destino) {
        Connection conn = null;
        Vuelo vuelo;
        List <Vuelo> vuelos=new ArrayList<>();
        String vueloJson;
        Gson gson = new Gson();
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, Constantes.USER, Constantes.PASS);
            Statement stmt = conn.createStatement();

            String query = "SELECT origen,destino,fecha,duracion FROM vuelo as v, asiento as a WHERE "
                    + "TIMESTAMP(fecha)>=TIMESTAMP('"+fecha_inicio+"') and "
                    + "TIMESTAMP(fecha)<=TIMESTAMP('"+fecha_fin+"')  "
                    + "and origen="+origen+" and destino="+destino+" "
                    + "and vuelo=v.id "
                    + "and clase='"+tipo_asiento+"'";
            
            System.out.println(query);
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                vuelo= new Vuelo(rs.getString("origen"),
                                 rs.getString("destino"),
                                 rs.getString("fecha"),
                                 rs.getInt("duracion"));
                vuelos.add(vuelo);
            
            }
            
            vueloJson=gson.toJson(vuelos);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return new RespuestaService(RespuestaService.SUCCESS, vueloJson);
    }
    
    public RespuestaService agregarBoleto(int id_asiento, int id_cliente) {
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, 
                    Constantes.USER, Constantes.PASS);
            
            Statement stmt = conn.createStatement();
            
            
            //validar que el asiento este disponible
            String query = "select diponible from asiento where id="+id_asiento;
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.first();
            boolean disponible=rs.getBoolean("diponible");
            if(!disponible){
              return new RespuestaService(RespuestaService.ERROR, Constantes.ASIENTO_NO_DISPONIBLE);
            }
            
            //marcar el asiento como no disponible
            query = "UPDATE asiento SET diponible=false WHERE id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id_asiento);
            preparedStmt.execute();
            
     
            //buscar la fecha para el vuelo del asiento
            query = "select fecha from asiento as a,vuelo as v where vuelo=v.id"
                    + " and a.id="+id_asiento;
            
            
            rs = stmt.executeQuery(query);
            
            rs.first();
            String fecha=rs.getString("fecha");
            
            //crear el boleto
            query = "insert into boleto(check_in,fecha_creacion,fecha_vencimiento,asiento,pasajero) "
                    + "values (false,now(),?,?,?)";

            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, fecha);
            preparedStmt.setInt(2, id_asiento);
            preparedStmt.setInt(3, id_cliente);

            preparedStmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (preparedStmt != null) {
                try {
                    preparedStmt.close();
                } catch (SQLException logOrIgnore) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return new RespuestaService(RespuestaService.SUCCESS, Constantes.ASIENTO_RESERVADO);
    }
    
    public RespuestaService checkin(int id_boleto, int id_cliente) {
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            Class.forName(Constantes.JDBC_DRIVER);
            conn = DriverManager.getConnection(Constantes.DB_URL, Constantes.USER, Constantes.PASS);
            Statement stmt = conn.createStatement();

            String query = "update boleto set check_in=true where id=? and pasajero=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id_boleto);
            preparedStmt.setInt(2, id_cliente);
            preparedStmt.execute();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new RespuestaService(RespuestaService.ERROR, e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return new RespuestaService(RespuestaService.ERROR, Constantes.ERROR_BD_CONNETC);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return new RespuestaService(RespuestaService.SUCCESS, Constantes.VUELO_CHECKEADO);
    }       
    
    
    

    public static void main(String[] arg) {
        /*
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String fecha= dateFormat.format(date);
        
        ConexionBD con=new ConexionBD();
        Socio socio= new Socio("username","Andre","suContraseña", "Soto Ayala",fecha,"3242342","andres@wow.com","1234534");
        RespuestaService resp=con.insertSocio(socio);
        System.out.println(fecha);*/
        
        ConexionBD con=new ConexionBD();
        RespuestaService resp=con.checkin(7,2);
        System.out.println(resp.getTipo());
        System.out.println(resp.getMensaje());

    }
}
