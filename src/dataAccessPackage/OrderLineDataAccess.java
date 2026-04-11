package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.util.List;

public interface OrderLineDataAccess extends GenericDAO<OrderLine, OrderLineId> {

    List<OrderLine> getLinesByOrderId(int orderId) throws DataAccessException;
}