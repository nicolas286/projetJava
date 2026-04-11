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
        this(new ProductDBAccess(), new OrderDBAccess(), new OrderLineDBAccess());
    }

    public TakeOrderManager(ProductDataAccess productDataAccess,
                            OrderDataAccess orderDataAccess,
                            OrderLineDataAccess orderLineDataAccess) {
        if (productDataAccess == null) {
            throw new IllegalArgumentException("ProductDataAccess cannot be null.");
        }
        if (orderDataAccess == null) {
            throw new IllegalArgumentException("OrderDataAccess cannot be null.");
        }
        if (orderLineDataAccess == null) {
            throw new IllegalArgumentException("OrderLineDataAccess cannot be null.");
        }

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

    public void takeOrder(int tableId, List<TakeOrderLine> lines) throws BusinessException {
        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        if (lines == null || lines.isEmpty()) {
            throw new BusinessException("At least one order line is required.");
        }

        validateTakeOrderLines(lines);

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

    private void validateTakeOrderLines(List<TakeOrderLine> lines) throws BusinessException {
        for (TakeOrderLine takeOrderLine : lines) {
            if (takeOrderLine == null) {
                throw new BusinessException("An order line cannot be null.");
            }

            if (takeOrderLine.getProduct() == null) {
                throw new BusinessException("A selected product is missing.");
            }

            if (takeOrderLine.getProduct().getId() <= 0) {
                throw new BusinessException("Product id must be positive.");
            }

            if (takeOrderLine.getProduct().getName() == null || takeOrderLine.getProduct().getName().isBlank()) {
                throw new BusinessException("Product name is required.");
            }

            if (takeOrderLine.getProduct().getPrice() < 0) {
                throw new BusinessException("Product price cannot be negative.");
            }

            if (takeOrderLine.getQuantity() <= 0) {
                throw new BusinessException("Quantity must be positive.");
            }
        }
    }
}