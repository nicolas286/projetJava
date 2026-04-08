package controllerPackage;

import businessPackage.OrderLineManager;
import dataAccessPackage.OrderLineDBAccess;
import exceptionPackage.BusinessException;
import modelPackage.OrderLine;

import java.util.List;

public class OrderLineController {

    private final OrderLineManager orderLineManager;

    public OrderLineController() {
        this.orderLineManager = new OrderLineManager(new OrderLineDBAccess());
    }

    public List<OrderLine> getLinesByOrderId(int orderId) throws BusinessException {
        return orderLineManager.getLinesByOrderId(orderId);
    }
}