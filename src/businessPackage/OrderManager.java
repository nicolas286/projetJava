package businessPackage;

import dataAccessPackage.OrderDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.util.List;

public class OrderManager {

    private final OrderDataAccess orderDataAccess;

    public OrderManager(OrderDataAccess orderDataAccess) {
        this.orderDataAccess = orderDataAccess;
    }

    public List<Order> getAllOrders() throws BusinessException {
        try {
            return orderDataAccess.getAllOrders();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve orders.", e);
        }
    }

    public Order getOrderById(int id) throws BusinessException {
        if (id <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            return orderDataAccess.getOrderById(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve order.", e);
        }
    }
}