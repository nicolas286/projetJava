package dataAccessPackage;

import exceptionPackage.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/projet_java";
    private final String USER = "root";
    private final String PASSWORD = "willylechat";

    private DBConnection() throws DataAccessException {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException e) {
            throw new DataAccessException("DB Connexion error", e);
        }
    }

    public static DBConnection getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
