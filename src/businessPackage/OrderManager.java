package businessPackage;

import dataAccessPackage.OrderDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.time.LocalDateTime;
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

    public void addOrder(Order order) throws BusinessException {
        validateOrder(order);

        try {
            orderDataAccess.insert(order);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to add order.", e);
        }
    }

    public void updateOrder(Order order) throws BusinessException {
        validateOrder(order);

        try {
            orderDataAccess.update(order);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to update order.", e);
        }
    }

    public void deleteOrder(int id) throws BusinessException {
        if (id <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            orderDataAccess.delete(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to delete order.", e);
        }
    }

    private void validateOrder(Order order) throws BusinessException {
        if (order == null) {
            throw new BusinessException("Order cannot be null.");
        }

        if (order.getId() <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        if (order.getDateOrdered() == null) {
            throw new BusinessException("Date ordered is required.");
        }

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            throw new BusinessException("Status is required.");
        }

        if (order.getTableId() <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        LocalDateTime ordered = order.getDateOrdered();
        LocalDateTime completed = order.getDateCompleted();
        LocalDateTime delivered = order.getDateDelivered();

        if (completed != null && completed.isBefore(ordered)) {
            throw new BusinessException("Date completed cannot be before date ordered.");
        }

        if (delivered != null && delivered.isBefore(ordered)) {
            throw new BusinessException("Date delivered cannot be before date ordered.");
        }

        if (completed != null && delivered != null && delivered.isBefore(completed)) {
            throw new BusinessException("Date delivered cannot be before date completed.");
        }
    }
}