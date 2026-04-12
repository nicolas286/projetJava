package controllerPackage;

import businessPackage.TakeOrderManager;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Product;
import modelPackage.input.TakeOrderLine;

import java.util.List;

public class TakeOrderController {

    private final TakeOrderManager takeOrderManager;

    public TakeOrderController() {
        this.takeOrderManager = new TakeOrderManager();
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        return takeOrderManager.getAvailableProducts();
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines) throws BusinessException, ValidationException {
        takeOrderManager.takeOrder(tableId, lines);
    }
}