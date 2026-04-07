package dataAccessPackage;
import exceptionPackage.DataAccessException;

import java.sql.Connection;

public abstract class AbstractDAO {

    protected Connection getConnection() throws DataAccessException {
        return DBConnection.getInstance().getConnection();
    }
}
