package controllerPackage;

import businessPackage.OrderManager;
import dataAccessPackage.OrderDBAccess;
import exceptionPackage.BusinessException;
import modelPackage.Order;

import java.util.List;

public class OrderController {

    private final OrderManager orderManager;

    public OrderController() {
        this.orderManager = new OrderManager(new OrderDBAccess());
    }

    public List<Order> getAllOrders() throws BusinessException {
        return orderManager.getAllOrders();
    }

    public Order getOrderById(int id) throws BusinessException {
        return orderManager.getOrderById(id);
    }
}