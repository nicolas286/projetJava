package businessPackage.Managers;

import dataAccessPackage.api.OrderDataAccess;
import dataAccessPackage.api.ProductDataAccess;
import dataAccessPackage.api.TableDataAccess;
import dataAccessPackage.impl.OrderDBAccess;
import dataAccessPackage.impl.ProductDBAccess;
import dataAccessPackage.impl.TableDBAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;
import modelPackage.entity.OrderLine;
import modelPackage.entity.Product;
import modelPackage.entity.RestaurantTable;
import modelPackage.enums.OrderStatus;
import modelPackage.input.TakeOrderLine;

import java.time.LocalDateTime;
import java.util.List;

public class TakeOrderManager {

    private final ProductDataAccess productDataAccess;
    private final OrderDataAccess orderDataAccess;
    private final TableDataAccess tableDataAccess;

    public TakeOrderManager() {
        this(new ProductDBAccess(), new OrderDBAccess(), new TableDBAccess());
    }

    public TakeOrderManager(ProductDataAccess productDataAccess,
                            OrderDataAccess orderDataAccess,
                            TableDataAccess tableDataAccess) {
        if (productDataAccess == null) {
            throw new IllegalArgumentException("ProductDataAccess cannot be null.");
        }
        if (orderDataAccess == null) {
            throw new IllegalArgumentException("OrderDataAccess cannot be null.");
        }
        if (tableDataAccess == null) {
            throw new IllegalArgumentException("TableDataAccess cannot be null.");
        }

        this.productDataAccess = productDataAccess;
        this.orderDataAccess = orderDataAccess;
        this.tableDataAccess = tableDataAccess;
    }

    public List<Product> getAvailableProducts() throws BusinessException {
        try {
            return productDataAccess.getProductsFromActiveMenus();
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to retrieve products from active menus.", e);
        }
    }

    public void takeOrder(int tableId, List<TakeOrderLine> lines)
            throws BusinessException, ValidationException {

        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        if (lines == null || lines.isEmpty()) {
            throw new BusinessException("At least one order line is required.");
        }

        validateTakeOrderLines(lines);

        try {
            RestaurantTable table = tableDataAccess.findById(tableId);

            if (table == null) {
                throw new BusinessException("Table not found.");
            }

            Order order = new Order(
                    0,
                    LocalDateTime.now(),
                    null,
                    null,
                    OrderStatus.ORDERED,
                    false,
                    table
            );

            int lineNumber = 1;

            for (TakeOrderLine takeOrderLine : lines) {
                order.addOrderLine(toOrderLine(lineNumber, takeOrderLine));
                lineNumber++;
            }

            orderDataAccess.insert(order);

        } catch (DataAccessException e) {
            throw new BusinessException("Unable to create order.", e);
        }
    }

    private OrderLine toOrderLine(int lineNumber, TakeOrderLine takeOrderLine) {
        Product product = takeOrderLine.getProduct();

        return new OrderLine(
                lineNumber,
                product,
                product.getName(),
                product.getPrice(),
                takeOrderLine.getQuantity()
        );
    }

    private void validateTakeOrderLines(List<TakeOrderLine> lines) throws ValidationException {
        for (TakeOrderLine takeOrderLine : lines) {
            if (takeOrderLine == null) {
                throw new ValidationException("An order line cannot be null.");
            }

            if (takeOrderLine.getProduct() == null) {
                throw new ValidationException("A selected product is missing.");
            }

            if (takeOrderLine.getQuantity() <= 0) {
                throw new ValidationException("Quantity must be positive.");
            }
        }
    }
}