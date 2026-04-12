package controllerPackage;

import businessPackage.RestaurantFacade;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;
import modelPackage.entity.OrderLine;
import modelPackage.entity.Product;
import modelPackage.entity.RestaurantTable;
import modelPackage.input.TakeOrderLine;
import modelPackage.search.LotStorageProductSearchResult;
import modelPackage.search.ProductCategoryConstraintSearchResult;
import modelPackage.search.TableOrderLineSearchResult;

import java.util.List;

public class RestaurantController {

    private final RestaurantFacade facade;

    public RestaurantController() {
        this.facade = new RestaurantFacade();
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines)
            throws BusinessException, ValidationException {
        facade.takeOrder(tableId, lines);
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        return facade.getAvailableProducts();
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        return facade.getAllTables();
    }

    public RestaurantTable getTableById(int id) throws BusinessException {
        return facade.getTableById(id);
    }

    public List<Order> getAllOrders() throws BusinessException {
        return facade.getAllOrders();
    }

    public Order getOrderById(int id) throws BusinessException {
        return facade.getOrderById(id);
    }

    public List<Order> getOrdersByTableId(int tableId) throws BusinessException {
        return facade.getOrdersByTableId(tableId);
    }

    public List<OrderLine> getOrderLinesByOrderId(int orderId) throws BusinessException {
        return facade.getOrderLinesByOrderId(orderId);
    }

    public List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws BusinessException {
        return facade.searchOrdersByTableId(tableId);
    }

    public List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(
            Integer productId, String productName) throws BusinessException {
        return facade.searchProductCategoryConstraint(productId, productName);
    }

    public List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws BusinessException {
        return facade.searchLotStorageProduct(lotId);
    }
}