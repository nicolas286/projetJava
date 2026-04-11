package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public interface OrderDataAccess extends GenericDAO<Order, Integer> {

    List<Order> getAllOrders() throws DataAccessException;

    Order getOrderById(int id) throws DataAccessException;

    List<Order> getOrdersByTableId(int tableId) throws DataAccessException;
}

// Ce fichier ne me paraît pas nécessaire tel quel dans le sens où il reprend essentiellement la même chose que notre genericDAO (getAllOrders = findAll, getOrderById = findById).
// ok pour getOrdersByTableId, donc on ne garderait ce fichier que pour cet élément spécifique aux commandes
