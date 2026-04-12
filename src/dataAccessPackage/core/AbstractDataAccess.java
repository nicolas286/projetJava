package dataAccessPackage.core;

import exceptionPackage.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDataAccess {

    protected Connection getConnection() throws DataAccessException {
        try {
            return DBConnection.getInstance();
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting database connection.", e);
        }
    }
}
