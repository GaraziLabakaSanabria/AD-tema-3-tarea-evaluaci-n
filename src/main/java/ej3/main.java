package ej3;

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
					if(dni.matches("[0-9]{8}-?[A-Za-z]")) {
						PreparedStatement ps = con.prepareStatement("SELECT nombre FROM alumnos WHERE dni = ?;");
						ps.setString(1, dni);
						ResultSet rs = ps.executeQuery();
						
						if(rs.next()) {
							System.out.println("El alumno seleccionado es " + rs.getString("nombre"));
							
						} else {
						
							System.out.println("Escribe el nombre del alumno para darlo de alta: " );
							String nombreAlumnoNuevo = sc.nextLine();
							PreparedStatement ps2 = con.prepareStatement("INSERT INTO alumnos (dni, nombre) VALUES (?, ?);");
							ps2.setString(1, dni);
							ps2.setString(2, nombreAlumnoNuevo);
							ps2.executeUpdate();
						
						}
					} else {
						System.out.println("DNI no encontrado, prueba a escribirlo correctamente");
					}
					
					PreparedStatement ps3 = con.prepareStatement("SELECT nombre FROM cursoss WHERE dni = ?;");

	} catch (SQLException e) {
		
	}

}}
