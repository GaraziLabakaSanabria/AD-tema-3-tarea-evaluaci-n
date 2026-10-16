package ej2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import conexionDB.ProbarConexion;

public class main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 2. Modificar nombre alumno
		
				
				try {
					Connection con = ProbarConexion.getConexion();
					
					System.out.println("Introduce el dni del alumno:");
					String dni = sc.nextLine();
					PreparedStatement ps = con.prepareStatement("SELECT nombre FROM alumnos WHERE dni = ?;");
					ps.setString(1, dni);
					ResultSet rs = ps.executeQuery();
					
					while (rs.next()) {
						System.out.println("Nombre actual: " + rs.getString("nombre"));
						System.out.println("Escribe el nuevo nombre del alumno (deja en blanco para cancelar): ");
						String nombreNuevo = sc.nextLine();
						PreparedStatement ps2 = con.prepareStatement("UPDATE alumnos SET nombre = ? WHERE dni = ?;");
						ps2.setString(1, nombreNuevo);
						ps2.setString(2, dni);
						System.out.println("Nombre actual: " + rs.getString(1));
						
						ps2.executeUpdate();
						System.out.println("Nombre actualizado correctamente a " + nombreNuevo);
					}
					
					
				} catch(SQLException e) {
					System.out.println("Alumno no encontrado");
				}

	}

}
