package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public interface OrderDataAccess extends GenericDAO<Order, Integer> {

    List<Order> getOrdersByTableId(int tableId) throws DataAccessException;
}

