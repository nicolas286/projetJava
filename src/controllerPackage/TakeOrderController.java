package controllerPackage;

import businessPackage.TakeOrderManager;
import dataAccessPackage.OrderDBAccess;
import dataAccessPackage.OrderLineDBAccess;
import dataAccessPackage.ProductDBAccess;
import exceptionPackage.BusinessException;
import modelPackage.Product;
import modelPackage.TakeOrderLine;

import java.util.List;

public class TakeOrderController {

    private final TakeOrderManager takeOrderManager;

    public TakeOrderController() {
        this.takeOrderManager = new TakeOrderManager(
                new ProductDBAccess(),
                new OrderDBAccess(),
                new OrderLineDBAccess()
        );
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        return takeOrderManager.getAvailableProducts();
    }

    public void takeOrder(int orderId, int tableId, List<TakeOrderLine> lines) throws BusinessException {
        takeOrderManager.takeOrder(orderId, tableId, lines);
    }
}