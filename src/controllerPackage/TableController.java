package controllerPackage;

import businessPackage.TableManager;
import dataAccessPackage.TableDBAccess;
import exceptionPackage.BusinessException;
import modelPackage.RestaurantTable;

import java.util.List;

public class TableController {

    private final TableManager tableManager;

    public TableController() {
        this.tableManager = new TableManager(new TableDBAccess());
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        return tableManager.getAllTables();
    }

    public RestaurantTable getTableById(int id) throws BusinessException {
        return tableManager.getTableById(id);
    }
}