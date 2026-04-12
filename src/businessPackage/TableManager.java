package businessPackage;

import dataAccessPackage.impl.TableDBAccess;
import dataAccessPackage.api.TableDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.entity.RestaurantTable;

import java.util.List;

public class TableManager {

    private final TableDataAccess tableDataAccess;

    public TableManager() {
        this(new TableDBAccess());
    }

    public TableManager(TableDataAccess tableDataAccess) {
        if (tableDataAccess == null) {
            throw new IllegalArgumentException("TableDataAccess cannot be null.");
        }
        this.tableDataAccess = tableDataAccess;
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        try {
            return tableDataAccess.findAll();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve tables.", e);
        }
    }

    public RestaurantTable getTableById(int id) throws BusinessException {
        if (id <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        try {
            return tableDataAccess.findById(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve table.", e);
        }
    }
}