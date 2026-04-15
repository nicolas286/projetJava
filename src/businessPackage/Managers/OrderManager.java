package businessPackage.Managers;

import dataAccessPackage.api.OrderDataAccess;
import dataAccessPackage.impl.OrderDBAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;
import modelPackage.entity.OrderLine;

import java.time.LocalDateTime;
import java.util.List;

public class OrderManager {

    private final OrderDataAccess orderDataAccess;

    public OrderManager() {
        this(new OrderDBAccess());
    }

    public OrderManager(OrderDataAccess orderDataAccess) {
        if (orderDataAccess == null) {
            throw new IllegalArgumentException("OrderDataAccess cannot be null.");
        }
        this.orderDataAccess = orderDataAccess;
    }

    public List<Order> getAllOrders() throws BusinessException {
        try {
            return orderDataAccess.findAll();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve orders.", e);
        }
    }

    public Order getOrderById(int id) throws BusinessException {
        if (id <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            Order order = orderDataAccess.findById(id);

            if (order == null) {
                throw new BusinessException("Order not found.");
            }

            return order;
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve order.", e);
        }
    }

    public void updateOrder(Order order) throws BusinessException, ValidationException {
        validateOrder(order);

        if (order.getId() <= 0) {
            throw new BusinessException("Order id must be positive for update.");
        }

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

    public List<Order> getOrdersByTableId(int tableId) throws BusinessException {
        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        try {
            return orderDataAccess.getOrdersByTableId(tableId);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve orders for the selected table.", e);
        }
    }

    public List<OrderLine> getOrderLinesByOrderId(int orderId) throws BusinessException {
        return getOrderById(orderId).getOrderLines();
    }


    private void validateOrder(Order order) throws ValidationException {
        if (order == null) {
            throw new ValidationException("Order cannot be null.");
        }

        LocalDateTime ordered = order.getDateOrdered();
        LocalDateTime completed = order.getDateCompleted();
        LocalDateTime delivered = order.getDateDelivered();

        if (completed != null && completed.isBefore(ordered)) {
            throw new ValidationException("Date completed cannot be before date ordered.");
        }

        if (delivered != null && delivered.isBefore(ordered)) {
            throw new ValidationException("Date delivered cannot be before date ordered.");
        }

        if (completed != null && delivered != null && delivered.isBefore(completed)) {
            throw new ValidationException("Date delivered cannot be before date completed.");
        }
    }
}