package ej3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import conexionDB.ProbarConexion;

public class main {
	private static Connection con;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		try {
			con = ProbarConexion.getConexion();
			
			// pedir DNI y validar 
			System.out.println("Introduce el dni del alumno:");
			String dni = sc.nextLine().trim();
			
			if (!dni.matches("^[0-9]{8}[A-Za-z]$")) {
				System.out.println("Formato de DNI incorrecto. Debe tener 8 números y una letra.");
				return; 
			}
			
			boolean alumnoNuevo = false;
			String nombreAlumno = "";
			
			// comprobar si el alumno ya existe
			PreparedStatement ps = con.prepareStatement("SELECT nombre FROM alumnos WHERE dni = ?");
			ps.setString(1, dni);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				nombreAlumno = rs.getString("nombre");
				System.out.println("El alumno seleccionado es: " + nombreAlumno);						
			} else {
				System.out.println("Escribe el nombre del alumno para darlo de alta: ");
				nombreAlumno = sc.nextLine();
				alumnoNuevo = true; 
			}
			rs.close();
			ps.close();
			
			// mostrar el listado de cursos 
			System.out.println("\n--- CURSOS DISPONIBLES ---");
			plazasLibres();
			
			// solicitar el curso al usuario
			System.out.println("\nElige un curso (introduce su número): ");
			int cursoElegido = sc.nextInt();
			sc.nextLine(); // limpiar buffer
			
			// comprobar matrícula
			if (comprobarCurso(cursoElegido, dni)) {
				System.out.println("Error: El alumno ya se encuentra matriculado en este curso.");
				return;
			}
			
			// transacción de matrícula
			
			con.setAutoCommit(false);
			
			// alumnos nuevos
			if (alumnoNuevo) {
				PreparedStatement psAlta = con.prepareStatement("INSERT INTO alumnos (dni, nombre) VALUES (?, ?)");
				psAlta.setString(1, dni);
				psAlta.setString(2, nombreAlumno);
				psAlta.executeUpdate();
				psAlta.close();
			}
			
			// insertar matrícula
			PreparedStatement psMatricula = con.prepareStatement("INSERT INTO matriculas (dni, id_curso, fecha_matricula) VALUES (?, ?, NOW());");
			psMatricula.setString(1, dni);
			psMatricula.setInt(2, cursoElegido);
			psMatricula.executeUpdate();
			psMatricula.close();
			
			// guardar en la bd
			con.commit();
			con.setAutoCommit(true); 
			
			System.out.println("¡Alumno matriculado correctamente!");
			
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.rollback(); 
					con.setAutoCommit(true);
				}
			} catch (SQLException ex) {
				System.out.println("Error al hacer rollback: " + ex.getMessage());
			}
			System.out.println("Error en la base de datos: " + e.getMessage());
		}
	}
	
	public static int plazasLibres() throws NumberFormatException, SQLException {
		String plazas = "";
		String[] plazasLibres;
		String total = "";
		String ocupado = "";
		int numPlazasSinAlumnos = 0;
		PreparedStatement ps3 = con.prepareStatement("SELECT c.id_curso, c.titulo, CONCAT(COUNT(m.id_curso), \"/\", a.capacidad) as \"plazas-libres\", a.nombre, a.edificio FROM cursos c LEFT JOIN matriculas m ON c.id_curso = m.id_curso JOIN aulas a ON a.id_aula = c.id_aula  GROUP BY c.id_curso, c.titulo, c.fecha, a.capacidad, a.nombre, a.edificio ORDER BY c.id_curso;");
		ResultSet rs3 = ps3.executeQuery();
		
		while(rs3.next()) {
			plazas = rs3.getString("plazas-libres");
			plazasLibres = plazas.split("/");
			total = plazasLibres[1];
			ocupado = plazasLibres[0];
			numPlazasSinAlumnos = Integer.parseInt(total) - Integer.parseInt(ocupado);
			
			System.out.println(rs3.getInt("id_curso") + ". " + rs3.getString("titulo")+ " Plazas libres: " + numPlazasSinAlumnos);
		}
		rs3.close();
		ps3.close();
		return numPlazasSinAlumnos;
	}
	
	// Método que comprueba si un DNI ya tiene una fila asociada a ese curso
	public static boolean comprobarCurso(int cursoElegido, String dniAlumno) throws SQLException {
		PreparedStatement psComprobar = con.prepareStatement("SELECT * FROM matriculas WHERE id_curso = ? AND dni = ?");
		psComprobar.setInt(1, cursoElegido);
		psComprobar.setString(2, dniAlumno);
		ResultSet rsComp = psComprobar.executeQuery();
		
		boolean existeMatricula = rsComp.next(); // true si encuentra fila, false si no
		rsComp.close();
		psComprobar.close();
		
		return existeMatricula;
	}
}
