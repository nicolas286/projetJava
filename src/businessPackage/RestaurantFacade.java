package businessPackage;

import businessPackage.Managers.OrderManager;
import businessPackage.Managers.SearchManager;
import businessPackage.Managers.TableManager;
import businessPackage.Managers.TakeOrderManager;
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

public class RestaurantFacade {

    private final OrderManager orderManager;
    private final TakeOrderManager takeOrderManager;
    private final TableManager tableManager;
    private final SearchManager searchManager;

    public RestaurantFacade() {
        this.orderManager = new OrderManager();
        this.tableManager = new TableManager();
        this.takeOrderManager = new TakeOrderManager();
        this.searchManager = new SearchManager();
    }

    public List<Order> getAllOrders() throws BusinessException {
        return orderManager.getAllOrders();
    }

    public Order getOrderById(int id) throws BusinessException {
        return orderManager.getOrderById(id);
    }

    public List<Order> getOrdersByTableId(int tableId) throws BusinessException {
        return orderManager.getOrdersByTableId(tableId);
    }

    public List<OrderLine> getOrderLinesByOrderId(int orderId) throws BusinessException {
        return orderManager.getOrderLinesByOrderId(orderId);
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines)
            throws BusinessException, ValidationException {
        takeOrderManager.takeOrder(tableId, lines);
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        return takeOrderManager.getAvailableProducts();
    }

    public List<RestaurantTable> getAllTables() throws BusinessException {
        return tableManager.getAllTables();
    }

    public RestaurantTable getTableById(int id) throws BusinessException {
        return tableManager.getTableById(id);
    }

    public List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws BusinessException {
        return searchManager.searchOrdersByTableId(tableId);
    }

    public List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(
            Integer productId, String productName) throws BusinessException {
        return searchManager.searchProductCategoryConstraint(productId, productName);
    }

    public List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws BusinessException {
        return searchManager.searchLotStorageProduct(lotId);
    }
}