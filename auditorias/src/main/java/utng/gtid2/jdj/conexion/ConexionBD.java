package utng.gtid2.jdj.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Punto único de arranque o de acceso a la base de datos JDBC hacia PostgreSQL
 * ConexionBD
 */
public class ConexionBD {

    private static ConexionBD instancia;

    private static final String URL = "jdbc:postgresql://localhost:5432/auditoriasdb";
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "admin";

    private Connection conexion;

    private ConexionBD() throws SQLException{
        conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static synchronized ConexionBD getInstancia() throws SQLException{
        if (instancia == null || instancia.conexion == null ||
            instancia.conexion.isClosed()){
                instancia = new ConexionBD();
            }
            return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public static void cerrar(){
        try {
            if(instancia != null && instancia.conexion != null && !instancia.conexion.isClosed()){
                instancia.conexion.close();
            }

        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: "+ e.getMessage());
            
        }
    }

}
