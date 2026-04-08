package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public interface OrderDataAccess {

    List<Order> getAllOrders() throws DataAccessException;

    Order getOrderById(int id) throws DataAccessException;
}