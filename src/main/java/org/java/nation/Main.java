package org.java.nation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        final String url = "jdbc:mysql://localhost:3306/db-nations";
        final String user = "root";
        final String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            System.out.println("Connessione stabilita correttamente");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ricerca una nazione: ");
            String searchString = scanner.nextLine();

            String query = "SELECT c.NAME AS Nazione, c.COUNTRY_ID AS ID, r.NAME AS Regione, ct.NAME AS Continente " +
                    "FROM COUNTRIES c " +
                    "JOIN REGIONS R ON c.REGION_ID = r.REGION_ID " +
                    "JOIN CONTINENTS ct ON r.CONTINENT_ID = ct.CONTINENT_ID " +
                    "WHERE c.NAME LIKE ? " +
                    "ORDER BY c.NAME";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchString + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("ID");
                String nazione = resultSet.getString("Nazione");
                String regione = resultSet.getString("Regione");
                String continente = resultSet.getString("Continente");

                System.out.println("Nazione: " + nazione);
                System.out.println("ID: " + id);
                System.out.println("Regione: " + regione);
                System.out.println("Continente: " + continente);
                System.out.println("----------------------------------");
            }

            System.out.print("Inserisci un ID: ");
            int selectedCountryId = scanner.nextInt();

            String statisticsQuery = "SELECT CS.*, C.NAME AS Nazione " +
            	    "FROM COUNTRY_STATS CS " +
            	    "JOIN ( " +
            	    "    SELECT COUNTRY_ID, MAX(year) AS last_year " +
            	    "    FROM COUNTRY_STATS " +
            	    "    GROUP BY COUNTRY_ID " +
            	    ") AS max_years " +
            	    "ON CS.COUNTRY_ID = max_years.COUNTRY_ID AND CS.year = max_years.last_year " +
            	    "JOIN COUNTRIES C ON CS.COUNTRY_ID = C.COUNTRY_ID " +
            	    "WHERE CS.COUNTRY_ID = ?";

            
            PreparedStatement statisticsStatement = conn.prepareStatement(statisticsQuery);
            statisticsStatement.setInt(1, selectedCountryId);
            ResultSet statisticsResultSet = statisticsStatement.executeQuery();

            System.out.println("\nStatistiche più recenti:");
            while (statisticsResultSet.next()) {
            	String nazione = statisticsResultSet.getString("Nazione");
                int year = statisticsResultSet.getInt("Year");
                int population = statisticsResultSet.getInt("Population");
                double gdp = statisticsResultSet.getDouble("GDP");

                System.out.println("\nDettagli nazione: " + nazione);
                System.out.println("Anno: " + year);
                System.out.println("Popolazione: " + population);
                System.out.println("GDP: " + gdp);
                
            }

            String languagesQuery = "SELECT L.`language` " +
                    "FROM LANGUAGES L " +
                    "JOIN COUNTRY_LANGUAGES CL ON L.LANGUAGE_ID = CL.LANGUAGE_ID " +
                    "WHERE CL.COUNTRY_ID = ?";
            
            PreparedStatement languagesStatement = conn.prepareStatement(languagesQuery);
            languagesStatement.setInt(1, selectedCountryId);
            ResultSet languagesResultSet = languagesStatement.executeQuery();

            System.out.println("\nLingue parlate:");
            while (languagesResultSet.next()) {
                String language = languagesResultSet.getString("language");
                System.out.println(language);
            }

        } catch (Exception e) {

            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }
}

