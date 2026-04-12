package controllerPackage;

import businessPackage.Managers.OrderManager;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;

public class OrderController {

    private final OrderManager orderManager;

    public OrderController() {
        this.orderManager = new OrderManager();
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
}