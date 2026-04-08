package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public interface OrderDataAccess extends GenericDAO<Order, Integer> {

    List<Order> getAllOrders() throws DataAccessException;

    Order getOrderById(int id) throws DataAccessException;
}