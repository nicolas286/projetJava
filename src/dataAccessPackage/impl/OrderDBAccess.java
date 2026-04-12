package dataAccessPackage.impl;

import dataAccessPackage.api.GenericDAO;
import dataAccessPackage.api.OrderDataAccess;
import dataAccessPackage.core.AbstractDataAccess;
import exceptionPackage.DataAccessException;
import modelPackage.entity.Order;
import modelPackage.enums.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDBAccess extends AbstractDataAccess implements OrderDataAccess, GenericDAO<Order, Integer> {

    @Override
    public void insert(Order order) throws DataAccessException {
        String sql = """
                INSERT INTO `Order` (dateOrdered, dateCompleted, status, isPaid, `table`, dateDelivered)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setTimestamp(1, Timestamp.valueOf(order.getDateOrdered()));

            if (order.getDateCompleted() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(order.getDateCompleted()));
            } else {
                statement.setNull(2, java.sql.Types.TIMESTAMP);
            }

            statement.setString(3, order.getStatus().name());
            statement.setBoolean(4, order.isPaid());
            statement.setInt(5, order.getTableId());

            if (order.getDateDelivered() != null) {
                statement.setTimestamp(6, Timestamp.valueOf(order.getDateDelivered()));
            } else {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            }

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error while inserting order.", e);
        }
    }

    @Override
    public void update(Order order) throws DataAccessException {
        String sql = """
                UPDATE `Order`
                SET dateOrdered = ?, dateCompleted = ?, status = ?, isPaid = ?, `table` = ?, dateDelivered = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(order.getDateOrdered()));

            if (order.getDateCompleted() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(order.getDateCompleted()));
            } else {
                statement.setNull(2, java.sql.Types.TIMESTAMP);
            }

            statement.setString(3, order.getStatus().name());
            statement.setBoolean(4, order.isPaid());
            statement.setInt(5, order.getTableId());

            if (order.getDateDelivered() != null) {
                statement.setTimestamp(6, Timestamp.valueOf(order.getDateDelivered()));
            } else {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            }

            statement.setInt(7, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating order.", e);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String deleteOrderLinesSql = "DELETE FROM OrderLine WHERE `order` = ?";
        String deletePaymentsSql = "DELETE FROM Payment WHERE `order` = ?";
        String deleteOrderSql = "DELETE FROM `Order` WHERE id = ?";

        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteOrderLinesStatement = connection.prepareStatement(deleteOrderLinesSql);
                 PreparedStatement deletePaymentsStatement = connection.prepareStatement(deletePaymentsSql);
                 PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderSql)) {

                deleteOrderLinesStatement.setInt(1, id);
                deleteOrderLinesStatement.executeUpdate();

                deletePaymentsStatement.setInt(1, id);
                deletePaymentsStatement.executeUpdate();

                deleteOrderStatement.setInt(1, id);
                deleteOrderStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DataAccessException("Error while deleting order.", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting order.", e);
        }
    }

    @Override
    public Order findById(Integer id) throws DataAccessException {
        String sql = """
                SELECT id, dateOrdered, dateCompleted, status, isPaid, `table`, dateDelivered
                FROM `Order`
                WHERE id = ?
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving order with id " + id + ".", e);
        }
    }

    @Override
    public List<Order> findAll() throws DataAccessException {
        String sql = """
                SELECT id, dateOrdered, dateCompleted, status, isPaid, `table`, dateDelivered
                FROM `Order`
                ORDER BY id
                """;

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                orders.add(mapOrder(resultSet));
            }

            return orders;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving orders.", e);
        }
    }

    @Override
    public List<Order> getOrdersByTableId(int tableId) throws DataAccessException {
        String sql = """
                SELECT id, dateOrdered, dateCompleted, status, isPaid, `table`, dateDelivered
                FROM `Order`
                WHERE `table` = ?
                ORDER BY dateOrdered DESC
                """;

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, tableId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }

            return orders;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving orders for table " + tableId + ".", e);
        }
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        LocalDateTime dateCompleted = null;
        if (resultSet.getTimestamp("dateCompleted") != null) {
            dateCompleted = resultSet.getTimestamp("dateCompleted").toLocalDateTime();
        }

        LocalDateTime dateDelivered = null;
        if (resultSet.getTimestamp("dateDelivered") != null) {
            dateDelivered = resultSet.getTimestamp("dateDelivered").toLocalDateTime();
        }

        return new Order(
                resultSet.getInt("id"),
                resultSet.getTimestamp("dateOrdered").toLocalDateTime(),
                dateCompleted,
                dateDelivered,
                OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getBoolean("isPaid"),
                resultSet.getInt("table")
        );
    }
}