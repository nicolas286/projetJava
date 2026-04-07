package dataAccessPackage;

import exceptionPackage.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO {

    protected Connection getConnection() throws DataAccessException {
        try {
            return DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting database connection.", e);
        }
    }
}