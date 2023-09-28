package org.java.nation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "root";
		
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			
			System.out.println("Connessione stabilita correttamente");
			
			Statement statement = conn.createStatement();
			
			String query = "SELECT c.NAME AS Nazione, c.COUNTRY_ID AS ID, r.NAME AS Regione, ct.NAME AS Continente " +
                    "FROM COUNTRIES c " +
                    "JOIN REGIONS R ON c.REGION_ID = r.REGION_ID " +
                    "JOIN CONTINENTS ct ON r.CONTINENT_ID = ct.CONTINENT_ID " +
                    "ORDER BY c.NAME";
			
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
                String nazione = resultSet.getString("Nazione");
                int id = resultSet.getInt("ID");
                String regione = resultSet.getString("Regione");
                String continente = resultSet.getString("Continente");

                System.out.println("Nazione: " + nazione);
                System.out.println("ID: " + id);
                System.out.println("Regione: " + regione);
                System.out.println("Continente: " + continente);
                System.out.println("----------------------------------");
            }
		} catch (Exception e) {
			
			System.out.println("Errore di connessione: " + e.getMessage());
		}
		
		System.out.println("\n----------------------------------\n");
		System.out.println("The end");
	}
}
