package controllerPackage;

import businessPackage.OrderManager;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;
import modelPackage.entity.OrderLine;

import java.util.List;

public class OrderController {

    private final OrderManager orderManager;

    public OrderController() {
        this.orderManager = new OrderManager();
    }

    public List<Order> getAllOrders() throws BusinessException {
        return orderManager.getAllOrders();
    }

    public Order getOrderById(int id) throws BusinessException {
        return orderManager.getOrderById(id);
    }

    public void addOrder(Order order) throws BusinessException, ValidationException {
        orderManager.addOrder(order);
    }

    public void updateOrder(Order order) throws BusinessException, ValidationException {
        orderManager.updateOrder(order);
    }

    public void deleteOrder(int id) throws BusinessException {
        orderManager.deleteOrder(id);
    }

    public List<Order> getOrdersByTableId(int tableId) throws BusinessException {
        return orderManager.getOrdersByTableId(tableId);
    }

    public List<OrderLine> getOrderLinesByOrderId(int orderId) throws BusinessException {
        return orderManager.getOrderLinesByOrderId(orderId);
    }

    public OrderLine getOrderLineByNumber(int orderId, int lineNumber) throws BusinessException {
        return orderManager.getOrderLineByNumber(orderId, lineNumber);
    }

    public void addOrderLineToOrder(int orderId, OrderLine orderLine)
            throws BusinessException, ValidationException {
        orderManager.addOrderLineToOrder(orderId, orderLine);
    }

    public void removeOrderLineFromOrder(int orderId, int lineNumber)
            throws BusinessException, ValidationException {
        orderManager.removeOrderLineFromOrder(orderId, lineNumber);
    }
}