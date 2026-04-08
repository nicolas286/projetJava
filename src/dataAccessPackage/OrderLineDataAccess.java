package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.util.List;

public interface OrderLineDataAccess {

    void insert(OrderLine orderLine) throws DataAccessException;

    List<OrderLine> getLinesByOrderId(int orderId) throws DataAccessException;
}