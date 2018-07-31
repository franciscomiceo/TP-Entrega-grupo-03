package frsf.isi.died.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

     
    public static Connection estableceConexion(){
        Connection conexion = null;
      
       
        try
        {
           Class.forName("org.postgresql.Driver");
          
           conexion = DriverManager.getConnection(Unifica.url,Unifica.user,Unifica.password);
           
        } catch (ClassNotFoundException | SQLException e) {
             JOptionPane.showMessageDialog(null,"Problema al establecer la Conexión a la base de datos."+e.getMessage());
         }
        return conexion;
    }
    
  
}