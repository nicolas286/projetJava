package businessPackage;

import dataAccessPackage.OrderDataAccess;
import dataAccessPackage.OrderLineDataAccess;
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

    public TakeOrderManager(ProductDataAccess productDataAccess,
                            OrderDataAccess orderDataAccess,
                            OrderLineDataAccess orderLineDataAccess) {
        this.productDataAccess = productDataAccess;
        this.orderDataAccess = orderDataAccess;
        this.orderLineDataAccess = orderLineDataAccess;
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        try {
            return productDataAccess.getProductsFromActiveMenus();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve products from active menus.", e);
        }
    }

    public void takeOrder(int orderId, int tableId, List<TakeOrderLine> lines) throws BusinessException {
        if (orderId <= 0) {
            throw new BusinessException("Order id must be positive.");
        }

        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        if (lines == null || lines.isEmpty()) {
            throw new BusinessException("At least one order line is required.");
        }

        try {
            Order existingOrder = orderDataAccess.getOrderById(orderId);
            if (existingOrder != null) {
                throw new BusinessException("An order with this id already exists.");
            }

            Order order = new Order(
                    orderId,
                    LocalDateTime.now(),
                    null,
                    null,
                    "ORDERED",
                    tableId
            );

            orderDataAccess.insert(order);

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