package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDBAccess extends AbstractDAO implements OrderDataAccess {

    @Override
    public void insert(Order order) throws DataAccessException {
        String sql = "INSERT INTO `Order` (dateOrdered, dateCompleted, status, `table`, dateDelivered) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, Timestamp.valueOf(order.getDateOrdered()));

            if (order.getDateCompleted() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(order.getDateCompleted()));
            } else {
                statement.setTimestamp(2, null);
            }

            statement.setString(3, order.getStatus());
            statement.setInt(4, order.getTableId());

            if (order.getDateDelivered() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(order.getDateDelivered()));
            } else {
                statement.setTimestamp(5, null);
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
        String sql = "UPDATE `Order` SET dateOrdered = ?, dateCompleted = ?, status = ?, `table` = ?, dateDelivered = ? WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(order.getDateOrdered()));

            if (order.getDateCompleted() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(order.getDateCompleted()));
            } else {
                statement.setTimestamp(2, null);
            }

            statement.setString(3, order.getStatus());
            statement.setInt(4, order.getTableId());

            if (order.getDateDelivered() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(order.getDateDelivered()));
            } else {
                statement.setTimestamp(5, null);
            }

            statement.setInt(6, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating order.", e);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String sql = "DELETE FROM `Order` WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting order.", e);
        }
    }

    @Override
    public Order findById(Integer id) throws DataAccessException {
        return getOrderById(id);
    }

    @Override
    public List<Order> findAll() throws DataAccessException {
        return getAllOrders();
    }

    @Override
    public List<Order> getAllOrders() throws DataAccessException {
        String sql = "SELECT id, dateOrdered, dateCompleted, status, `table`, dateDelivered FROM `Order` ORDER BY id";
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
    public Order getOrderById(int id) throws DataAccessException {
        String sql = "SELECT id, dateOrdered, dateCompleted, status, `table`, dateDelivered FROM `Order` WHERE id = ?";

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
    public List<Order> getOrdersByTableId(int tableId) throws DataAccessException {
        String sql = "SELECT id, dateOrdered, dateCompleted, status, `table`, dateDelivered FROM `Order` WHERE `table` = ? ORDER BY dateOrdered DESC";
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
                resultSet.getString("status"),
                resultSet.getInt("table")
        );
    }
}