package ej5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.vibur.dbcp.ViburDBCPDataSource;

public class conectarAVibur {

    public static void main(String[] args) {
        
        ViburDBCPDataSource ds = new ViburDBCPDataSource();

        ds.setJdbcUrl("jdbc:hsqldb:mem:sakila;shutdown=false");
        ds.setUsername("SA");
        ds.setPassword("");

        ds.setPoolInitialSize(5);
        ds.setPoolMaxSize(20);
        ds.start(); 

       
        try (Connection con = ds.getConnection()) {
            
            
            if (con.isValid(2)) {
                System.out.println("¡Conexión exitosa y válida a HSQLDB utilizando Vibur DBCP!");
            }

            // Crear la tabla 'comprobacion' (limpiando si existía antes)
            try (Statement st = con.createStatement()) {
                st.executeUpdate("DROP TABLE comprobacion IF EXISTS");
                st.executeUpdate("CREATE TABLE comprobacion (id INT IDENTITY PRIMARY KEY, mensaje VARCHAR(255))");
                System.out.println("Tabla 'comprobacion' creada correctamente.");
            }

            // Insertar una fila de prueba
            String sqlInsert = "INSERT INTO comprobacion (mensaje) VALUES (?)";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setString(1, "Hola, esta es una prueba con Vibur y HSQLDB en fichero.");
                psInsert.executeUpdate();
                System.out.println("Fila insertada correctamente.");
            }

            // Recuperar la fila mediante una consulta SELECT
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, mensaje FROM comprobacion")) {
                
                System.out.println("\n--- RESULTADOS DE LA TABLA ---");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + " | Mensaje: " + rs.getString("mensaje"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en la ejecución de la base de datos: " + e.getMessage());
        } finally {
            ds.close();
            System.out.println("\nPool de conexiones finalizado y cerrado correctamente.");
        }
    }
}

