package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public interface OrderDataAccess extends GenericDAO<Order, Integer> {

    List<Order> getAllOrders() throws DataAccessException;

    Order getOrderById(int id) throws DataAccessException;

    // Il faut supprimer ces deux là : on a déjà findall et findbyid définies dans la DAO générique

    List<Order> getOrdersByTableId(int tableId) throws DataAccessException;
}