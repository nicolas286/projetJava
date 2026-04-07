package controllerPackage;

import businessPackage.TableManager;
import exceptionPackage.BusinessException;
import modelPackage.RestaurantTable;

import java.util.List;

public class TableController {

    private final TableManager tableManager;

    public TableController(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        return tableManager.getAllTables();
    }
}