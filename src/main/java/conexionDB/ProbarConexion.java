package conexionDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class ProbarConexion {
    private static final String URL =
            "jdbc:mysql://localhost:3306/dbformacion";
    private static final String USER = "root";
    private static final String PASS = "root";
 
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Conexión válida: " + con.isValid(2));
        } catch (SQLException e) {
            System.err.println("No se pudo conectar: " + e.getMessage());
        }
        
        
    }
    
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}