package businessPackage;

import dataAccessPackage.TableDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.RestaurantTable;

import java.util.List;

public class TableManager {

    private final TableDataAccess tableDataAccess;

    public TableManager(TableDataAccess tableDataAccess) {
        this.tableDataAccess = tableDataAccess;
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        try {
            return tableDataAccess.getAllTables();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve tables.", e);
        }
    }

    public RestaurantTable getTableById(int id) throws BusinessException {
        if (id <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        try {
            return tableDataAccess.getTableById(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve table.", e);
        }
    }
}