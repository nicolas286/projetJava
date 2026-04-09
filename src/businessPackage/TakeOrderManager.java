package businessPackage;

import dataAccessPackage.OrderDBAccess;
import dataAccessPackage.OrderDataAccess;
import dataAccessPackage.OrderLineDBAccess;
import dataAccessPackage.OrderLineDataAccess;
import dataAccessPackage.ProductDBAccess;
import dataAccessPackage.ProductDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.Order;
import modelPackage.OrderLine;
import modelPackage.Product;
import modelPackage.TakeOrderLine;

import java.time.LocalDateTime;
import java.util.List;

public class TakeOrderManager {

    private final ProductDataAccess productDataAccess;
    private final OrderDataAccess orderDataAccess;
    private final OrderLineDataAccess orderLineDataAccess;

    public TakeOrderManager() {
        this.productDataAccess = new ProductDBAccess();
        this.orderDataAccess = new OrderDBAccess();
        this.orderLineDataAccess = new OrderLineDBAccess();
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        try {
            return productDataAccess.getProductsFromActiveMenus();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve products from active menus.", e);
        }
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines) throws BusinessException {
        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        if (lines == null || lines.isEmpty()) {
            throw new BusinessException("At least one order line is required.");
        }

        try {
            Order order = new Order(
                    0,
                    LocalDateTime.now(),
                    null,
                    null,
                    "ORDERED",
                    false,
                    tableId
            );

            orderDataAccess.insert(order);

            int orderId = order.getId();
            int lineNumber = 1;

            for (TakeOrderLine takeOrderLine : lines) {
                if (takeOrderLine.getProduct() == null) {
                    throw new BusinessException("A selected product is missing.");
                }

                if (takeOrderLine.getQuantity() <= 0) {
                    throw new BusinessException("Quantity must be positive.");
                }

                Product product = takeOrderLine.getProduct();

                OrderLine orderLine = new OrderLine(
                        lineNumber,
                        orderId,
                        product.getName(),
                        product.getPrice(),
                        product.getId(),
                        takeOrderLine.getQuantity()
                );

                orderLineDataAccess.insert(orderLine);
                lineNumber++;
            }

        } catch (DataAccessException e) {
            throw new BusinessException("Unable to create order.", e);
        }
    }
}