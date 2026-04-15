package dataAccessPackage.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* 
Pattern Singleton :
Une seule instance de Connection dans toute l'application
*/

public class DBConnection {

    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/projet_java";
    private static final String USER = "root";
    private static final String PASSWORD = "willylechat";

    private DBConnection() {} // Empêche l'instanciation de la classe (classe utilitaire)

    /*
    Méthode publique :
    crée la connexion si elle n'existe pas encore
    */

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /* Fermeture finale */

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
