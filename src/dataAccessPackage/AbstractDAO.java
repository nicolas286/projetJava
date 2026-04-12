package dataAccessPackage;

import exceptionPackage.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

/* 
Classe abstraite :
- centralise l'accès à la connexion
- évite la duplication de code dans les DAO
- gère les exceptions
*/

public abstract class AbstractDAO<T, ID> implements GenericDAO<T, ID> {

    protected Connection getConnection() throws DataAccessException {
        try {
            return DBConnection.getInstance();
        } catch (SQLException e) {
            throw new DataAccessException("Error while getting database connection.", e);
        }
    }
}
