package dataAccessPackage.impl;

import dataAccessPackage.api.OrderDataAccess;
import dataAccessPackage.core.AbstractDataAccess;
import exceptionPackage.DataAccessException;
import modelPackage.entity.Order;
import modelPackage.entity.OrderLine;
import modelPackage.entity.Product;
import modelPackage.entity.RestaurantTable;
import modelPackage.enums.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDBAccess extends AbstractDataAccess implements OrderDataAccess {

    private static final String ORDER_SELECT = """
            SELECT
                o.id,
                o.dateOrdered,
                o.dateCompleted,
                o.status,
                o.isPaid,
                o.dateDelivered,
                t.id AS table_id,
                t.positionX,
                t.positionY,
                t.floor,
                t.capacity,
                t.isActive
            FROM `Order` o
            JOIN `Table` t ON t.id = o.`table`
            """;

    @Override
    public void insert(Order order) throws DataAccessException {
        String sql = """
                INSERT INTO `Order` (dateOrdered, dateCompleted, status, isPaid, `table`, dateDelivered)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false); // Empêcher l'envoi immédiat de chaque requête

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                fillOrderStatement(statement, order, false);
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1)); // On récupère l'id généré par la DB
                    } else {
                        throw new DataAccessException("No generated id returned for inserted order.");
                    }
                }
            }

            insertOrderLines(order, connection); // On a un order complet avec id pour insérer les lignes
            connection.commit();

        } catch (SQLException e) {
            rollbackQuietly(connection, e); // Si ça plante, on annule (rollback transaction)
            throw new DataAccessException("Error while inserting order.", e);
        } finally {
            restoreAutoCommit(connection);
        }
    }

    @Override
    public void update(Order order) throws DataAccessException {
        String sql = """
                UPDATE `Order`
                SET dateOrdered = ?, dateCompleted = ?, status = ?, isPaid = ?, `table` = ?, dateDelivered = ?
                WHERE id = ?
                """;

        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                fillOrderStatement(statement, order, true); // Ici on a déjà l'id puisque update, on l'inclut pour le WHERE
                statement.executeUpdate();
            }

            deleteOrderLines(order.getId(), connection);
            insertOrderLines(order, connection); // Reset des lignes avec les nouvelles éventuelles
            connection.commit();

        } catch (SQLException e) {
            rollbackQuietly(connection, e);
            throw new DataAccessException("Error while updating order.", e);
        } finally {
            restoreAutoCommit(connection);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String deletePaymentsSql = "DELETE FROM Payment WHERE `order` = ?";
        String deleteOrderSql = "DELETE FROM `Order` WHERE id = ?";

        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement deletePaymentsStatement = connection.prepareStatement(deletePaymentsSql);
                    PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderSql)
            ) {
                deletePaymentsStatement.setInt(1, id);
                deletePaymentsStatement.executeUpdate();

                deleteOrderLines(id, connection);

                deleteOrderStatement.setInt(1, id);
                deleteOrderStatement.executeUpdate();

                connection.commit();
            }

        } catch (SQLException e) {
            rollbackQuietly(connection, e);
            throw new DataAccessException("Error while deleting order.", e);
        } finally {
            restoreAutoCommit(connection);
        }
    }

    @Override
    public Order findById(Integer id) throws DataAccessException {
        String sql = ORDER_SELECT + " WHERE o.id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = mapOrder(resultSet);
                    loadOrderLines(order);
                    return order;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving order with id " + id + ".", e);
        }
    }

    @Override
    public List<Order> findAll() throws DataAccessException {
        String sql = ORDER_SELECT + " ORDER BY o.id";

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapOrders(resultSet);
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving orders.", e);
        }
    }

    @Override
    public List<Order> getOrdersByTableId(int tableId) throws DataAccessException {
        String sql = ORDER_SELECT + " WHERE t.id = ? ORDER BY o.dateOrdered DESC";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, tableId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapOrders(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving orders for table " + tableId + ".", e);
        }
    }

    private void fillOrderStatement(PreparedStatement statement, Order order, boolean includeId) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(order.getDateOrdered()));
        setNullableTimestamp(statement, 2, order.getDateCompleted());
        statement.setString(3, order.getStatus().name());
        statement.setBoolean(4, order.isPaid());
        statement.setInt(5, order.getTable().getId());
        setNullableTimestamp(statement, 6, order.getDateDelivered());

        if (includeId) {
            statement.setInt(7, order.getId()); // Inclusion de l'id en param 7 pour update
        }
    }

    private void setNullableTimestamp(PreparedStatement statement, int index, LocalDateTime value) throws SQLException {
        if (value != null) {
            statement.setTimestamp(index, Timestamp.valueOf(value)); // Conversion LocalDateTime => Timestamp pour DB
        } else {
            statement.setNull(index, Types.TIMESTAMP); // NULL en DB
        }
    }

    private List<Order> mapOrders(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();

        while (resultSet.next()) {
            Order order = mapOrder(resultSet);
            loadOrderLines(order);
            orders.add(order);
        }

        return orders;
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        RestaurantTable table = new RestaurantTable(
                resultSet.getInt("table_id"),
                resultSet.getInt("positionX"),
                resultSet.getInt("positionY"),
                (Integer) resultSet.getObject("floor"),
                resultSet.getInt("capacity"),
                resultSet.getBoolean("isActive")
        );

        return new Order(
                resultSet.getInt("id"),
                resultSet.getTimestamp("dateOrdered").toLocalDateTime(),
                getNullableDateTime(resultSet, "dateCompleted"),
                getNullableDateTime(resultSet, "dateDelivered"),
                OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getBoolean("isPaid"),
                table
        );
    }

    private void insertOrderLines(Order order, Connection connection) throws SQLException {
        String sql = """
                INSERT INTO OrderLine (number, `order`, nameSnapshot, priceSnapshot, product, quantity)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (OrderLine line : order.getOrderLines()) {
                statement.setInt(1, line.getNumber());
                statement.setInt(2, order.getId());
                statement.setString(3, line.getNameSnapshot());
                statement.setDouble(4, line.getPriceSnapshot());
                statement.setInt(5, line.getProduct().getId());
                statement.setInt(6, line.getQuantity());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void deleteOrderLines(int orderId, Connection connection) throws SQLException {
        String sql = "DELETE FROM OrderLine WHERE `order` = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }

    private void loadOrderLines(Order order) throws DataAccessException {
        String sql = """
                SELECT
                    ol.number,
                    ol.nameSnapshot,
                    ol.priceSnapshot,
                    ol.quantity,
                    p.id AS product_id,
                    p.name AS product_name,
                    p.price AS product_price,
                    p.lot AS product_lot
                FROM OrderLine ol
                JOIN Product p ON p.id = ol.product
                WHERE ol.`order` = ?
                ORDER BY ol.number
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, order.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    order.addOrderLine(mapOrderLine(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while loading order lines for order " + order.getId() + ".", e);
        }
    }



    private OrderLine mapOrderLine(ResultSet resultSet) throws SQLException {
        Product product = new Product(
                resultSet.getInt("product_id"),
                resultSet.getString("product_name"),
                resultSet.getDouble("product_price"),
                resultSet.getInt("product_lot")
        );

        return new OrderLine(
                resultSet.getInt("number"),
                product,
                resultSet.getString("nameSnapshot"),
                resultSet.getDouble("priceSnapshot"),
                resultSet.getInt("quantity")
        );
    }

    private LocalDateTime getNullableDateTime(ResultSet resultSet, String column) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(column);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private void rollbackQuietly(Connection connection, SQLException originalException) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }

    private void restoreAutoCommit(Connection connection) throws DataAccessException {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DataAccessException("Error while restoring auto-commit.", e);
            }
        }
    }
}