package controllerPackage;

import businessPackage.TakeOrderManager;
import exceptionPackage.BusinessException;
import modelPackage.Product;
import modelPackage.TakeOrderLine;

import java.util.List;

public class TakeOrderController {

    private final TakeOrderManager takeOrderManager;

    public TakeOrderController() {
        this.takeOrderManager = new TakeOrderManager();
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        return takeOrderManager.getAvailableProducts();
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines) throws BusinessException {
        takeOrderManager.takeOrder(tableId, lines);
    }
}