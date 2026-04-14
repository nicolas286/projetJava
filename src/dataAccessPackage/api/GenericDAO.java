package dataAccessPackage.api;

import exceptionPackage.DataAccessException;

import java.util.List;

public interface GenericDAO<T, ID> {

    void insert(T entity) throws DataAccessException;

    void update(T entity) throws DataAccessException;

    void delete(ID id) throws DataAccessException;

    T findById(ID id) throws DataAccessException;

    List<T> findAll() throws DataAccessException;
}
