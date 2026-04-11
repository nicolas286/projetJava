package dataAccessPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private final Connection connection;

    private static final String URL =
            System.getProperty("db.url", "jdbc:mysql://localhost:3306/projet_java");
    private static final String USER =
            System.getProperty("db.user", "root");
    private static final String PASSWORD =
            System.getProperty("db.password", "willylechat");

    private DBConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Database connection failed.", e);
        }
    }

    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}