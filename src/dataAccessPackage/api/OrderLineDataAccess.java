package dataAccessPackage.api;

import exceptionPackage.DataAccessException;
import modelPackage.entity.OrderLine;

import java.util.List;

public interface OrderLineDataAccess extends GenericDAO<OrderLine, OrderLine.OrderLineId> {

    List<OrderLine> getLinesByOrderId(int orderId) throws DataAccessException;
}