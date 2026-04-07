package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.RestaurantTable;

import java.util.List;

public interface TableDataAccess {

    List<RestaurantTable> getAllTables() throws DataAccessException;

    RestaurantTable getTableById(int id) throws DataAccessException;
}