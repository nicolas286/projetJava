package businessPackage;

import dataAccessPackage.impl.OrderLineDBAccess;
import dataAccessPackage.api.OrderLineDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.entity.OrderLine;

import java.util.List;

public class OrderLineManager {

    private final OrderLineDataAccess orderLineDataAccess;

    public OrderLineManager() {
        this(new OrderLineDBAccess());
    }

    public OrderLineManager(OrderLineDataAccess orderLineDataAccess) {
        if (orderLineDataAccess == null) {
            throw new IllegalArgumentException("OrderLineDataAccess cannot be null.");
        }
        this.orderLineDataAccess = orderLineDataAccess;
    }

    public List<OrderLine> getAllOrderLines() throws BusinessException {
        try {
            return orderLineDataAccess.findAll();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve order lines.", e);
        }
    }

    public OrderLine getOrderLineById(int number, int orderId) throws BusinessException {
        if (number <= 0) {
            throw new BusinessException("Line number must be positive.");
        }

        if (orderId <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            return orderLineDataAccess.findById(new OrderLine.OrderLineId(number, orderId));
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve order line.", e);
        }
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

    public void addOrderLine(OrderLine orderLine) throws BusinessException {
        validateOrderLine(orderLine);

        try {
            orderLineDataAccess.insert(orderLine);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to add order line.", e);
        }
    }

    public void updateOrderLine(OrderLine orderLine) throws BusinessException {
        validateOrderLine(orderLine);

        try {
            orderLineDataAccess.update(orderLine);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to update order line.", e);
        }
    }

    public void deleteOrderLine(int number, int orderId) throws BusinessException {
        if (number <= 0) {
            throw new BusinessException("Line number must be positive.");
        }

        if (orderId <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        try {
            orderLineDataAccess.delete(new OrderLine.OrderLineId(number, orderId));
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to delete order line.", e);
        }
    }

    private void validateOrderLine(OrderLine orderLine) throws BusinessException {
        if (orderLine == null) {
            throw new BusinessException("Order line cannot be null.");
        }

        if (orderLine.getNumber() <= 0) {
            throw new BusinessException("Line number must be positive.");
        }

        if (orderLine.getOrderId() <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        if (orderLine.getNameSnapshot() == null || orderLine.getNameSnapshot().isBlank()) {
            throw new BusinessException("Product name snapshot is required.");
        }

        if (orderLine.getPriceSnapshot() < 0) {
            throw new BusinessException("Price snapshot cannot be negative.");
        }

        if (orderLine.getProductId() <= 0) {
            throw new BusinessException("Product id must be positive.");
        }

        if (orderLine.getQuantity() <= 0) {
            throw new BusinessException("Quantity must be positive.");
        }
    }
}