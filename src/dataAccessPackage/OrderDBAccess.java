package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDBAccess extends AbstractDAO implements OrderDataAccess {

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