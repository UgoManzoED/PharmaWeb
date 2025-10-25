package it.unisa.pharmaweb.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnectionPool {
	
	// --- Dati di configurazione del Database ---
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pharmaweb_db?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ugo9999)";

    // --- Caricamento driver JDBC di MySQL. (Statico perchè lo eseguo 1 volta al caricamento) ---
    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Errore nel caricamento del driver JDBC: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    // --- Connessione al Database ---
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD); // Ogni volta che viene chiamato, crea una nuova connessione
    }
}
