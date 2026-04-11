package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.util.List;

public interface OrderLineDataAccess //extends GenericDao... {

    void insert(OrderLine orderLine) throws DataAccessException;
    // Tu peux retirer l'insert qui est couvert par générique DAO

    List<OrderLine> getLinesByOrderId(int orderId) throws DataAccessException;
}
