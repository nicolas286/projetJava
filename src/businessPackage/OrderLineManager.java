package businessPackage;

import dataAccessPackage.OrderLineDBAccess;
import dataAccessPackage.OrderLineDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.util.List;

public class OrderLineManager {

    private final OrderLineDataAccess orderLineDataAccess;

    public OrderLineManager() {
        this.orderLineDataAccess = new OrderLineDBAccess();
    }

    public List<OrderLine> getLinesByOrderId(int orderId) throws BusinessException {
        if (orderId <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            return orderLineDataAccess.getLinesByOrderId(orderId);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve order lines.", e);
        }
    }
}