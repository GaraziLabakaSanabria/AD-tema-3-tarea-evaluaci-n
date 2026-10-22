package ej4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import com.mysql.cj.jdbc.CallableStatement;

import conexionDB.ProbarConexion;

public class main {
	static Connection con;
	static int cursoElegido;
	public static void main(String[] args) {
		try {
			con = ProbarConexion.getConexion();
			
			// Función SQL
			
			/*
			use dbformacion;
			DELIMITER //
			CREATE FUNCTION obtener_numero_matriculados(cursoElegido int) returns int reads sql data
			BEGIN
			declare numero_matriculas int; -- variable contenedora
			select count(*) INTO numero_matriculas FROM matriculas WHERE id_curso = cursoElegido;
			RETURN numero_matriculas;
			END //
			*/
			
			Scanner sc = new Scanner(System.in);
			
			
			String sql = "{? = call obtener_numero_matriculados(?)}";
			
			
			PreparedStatement ps = con.prepareStatement("SELECT id_curso, titulo FROM cursos;");
			ResultSet rs = ps.executeQuery();
			
			
			while(rs.next()) {
				System.out.println(rs.getInt("id_curso"));
				System.out.println(rs.getString("titulo"));
			}
			
			System.out.println("Introduce el curso elegido:");
			int cursoElegido = sc.nextInt();
			
			CallableStatement cs = (CallableStatement) con.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, cursoElegido);
			cs.execute();
			
			System.out.println("Número de alumnos matriculados: " + cs.getInt(1));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
