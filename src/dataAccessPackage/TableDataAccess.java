package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.RestaurantTable;

import java.util.List;

public interface TableDataAccess extends GenericDAO<RestaurantTable, Integer> {

    List<RestaurantTable> getAllTables() throws DataAccessException;

    RestaurantTable getTableById(int id) throws DataAccessException;
}