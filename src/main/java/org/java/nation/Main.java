package org.java.nation;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

	public static void main(String[] args) {
		
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "root";
		
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			
			System.out.println("Connessione stabilita correttamente");
		} catch (Exception e) {
			
			System.out.println("Errore di connessione: " + e.getMessage());
		}
		
		System.out.println("\n----------------------------------\n");
		System.out.println("The end");
	}
}