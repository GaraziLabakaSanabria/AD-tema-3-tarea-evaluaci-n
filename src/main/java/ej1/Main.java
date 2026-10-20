package ej1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import conexionDB.ProbarConexion;

public class Main {

	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		// 1. Listado de cursos y ocupación
		
		try {
			Connection con = ProbarConexion.getConexion();
			
			PreparedStatement ps = con.prepareStatement("SELECT c.id_curso, c.titulo, c.fecha, c.id_aula FROM cursos c ORDER BY c.fecha ASC;");
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println("id del curso: " + rs.getInt("id_curso")); 
				System.out.println("título curso: " + rs.getString("titulo"));
				System.out.println("título curso: " + rs.getDate("fecha"));
				System.out.println("id del aula: " + rs.getInt("id_aula"));
			}
			
			PreparedStatement ps2 = con.prepareStatement("SELECT c.id_curso, c.titulo, c.fecha, CONCAT(COUNT(m.id_curso), \"/\", a.capacidad) as \"plazas libres\", a.nombre, a.edificio FROM cursos c LEFT JOIN matriculas m ON c.id_curso = m.id_curso JOIN aulas a ON a.id_aula = c.id_aula  GROUP BY c.id_curso, c.titulo, c.fecha, a.capacidad, a.nombre, a.edificio ORDER BY c.id_curso;");
			ResultSet rs2 = ps2.executeQuery();
			
			while (rs2.next()) {
				System.out.println(rs2.getInt("id_curso")); 
				System.out.println(rs2.getString("titulo"));
				System.out.println(rs2.getDate("fecha"));
				
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

}}
