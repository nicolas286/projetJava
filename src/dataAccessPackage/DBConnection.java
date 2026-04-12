package dataAccessPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* 
Pattern Singleton :
Une seule instance de DBConnection dans toute l'application
*/

public class DBConnection {

    private static DBConnection instance;
    private final Connection connection;

    /* 
    Récupération des informations de connexion à la db
    System.getProperty permet de passer des arguments au lancement du programme, sinon fallback 
    */

    private static final String URL =
            System.getProperty("db.url", "jdbc:mysql://localhost:3306/projet_java");
    private static final String USER =
            System.getProperty("db.user", "root");
    private static final String PASSWORD =
            System.getProperty("db.password", "willylechat");

    /* Méthode centrale de connexion à la DB avec les infos */

    private DBConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Database connection failed.", e);
        }
    }

    /* 
    Méthode publique :
    - crée l'instance si elle n'existe pas
    - synchronized évite la création de plusieurs instances en cas d'accès concurrent (multi-thread)
    */

    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /* 
    Retourne la connexion JDBC gérée par le Singleton
    */

    public Connection getConnection() {
        return connection;
    }
}
