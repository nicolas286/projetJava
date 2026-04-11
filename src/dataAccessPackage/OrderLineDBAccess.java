package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineDBAccess extends AbstractDAO<OrderLine, OrderLineId> implements OrderLineDataAccess {

    @Override
    public void insert(OrderLine orderLine) throws DataAccessException {
        String sql = """
                INSERT INTO OrderLine (number, `order`, nameSnapshot, priceSnapshot, product, quantity)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderLine.getNumber());
            statement.setInt(2, orderLine.getOrderId());
            statement.setString(3, orderLine.getNameSnapshot());
            statement.setDouble(4, orderLine.getPriceSnapshot());
            statement.setInt(5, orderLine.getProductId());
            statement.setInt(6, orderLine.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while inserting order line.", e);
        }
    }

    @Override
    public void update(OrderLine orderLine) throws DataAccessException {
        String sql = """
                UPDATE OrderLine
                SET nameSnapshot = ?, priceSnapshot = ?, product = ?, quantity = ?
                WHERE number = ? AND `order` = ?
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, orderLine.getNameSnapshot());
            statement.setDouble(2, orderLine.getPriceSnapshot());
            statement.setInt(3, orderLine.getProductId());
            statement.setInt(4, orderLine.getQuantity());
            statement.setInt(5, orderLine.getNumber());
            statement.setInt(6, orderLine.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating order line.", e);
        }
    }

    @Override
    public void delete(OrderLineId id) throws DataAccessException {
        String sql = "DELETE FROM OrderLine WHERE number = ? AND `order` = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id.getNumber());
            statement.setInt(2, id.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting order line.", e);
        }
    }

    @Override
    public OrderLine findById(OrderLineId id) throws DataAccessException {
        String sql = """
                SELECT number, `order`, nameSnapshot, priceSnapshot, product, quantity
                FROM OrderLine
                WHERE number = ? AND `order` = ?
                """;

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id.getNumber());
            statement.setInt(2, id.getOrderId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrderLine(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error while retrieving order line with number " + id.getNumber()
                            + " for order id " + id.getOrderId() + ".", e
            );
        }
    }

    @Override
    public List<OrderLine> findAll() throws DataAccessException {
        String sql = """
                SELECT number, `order`, nameSnapshot, priceSnapshot, product, quantity
                FROM OrderLine
                ORDER BY `order`, number
                """;

        List<OrderLine> lines = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                lines.add(mapOrderLine(resultSet));
            }

            return lines;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving all order lines.", e);
        }
    }

    @Override
    public List<OrderLine> getLinesByOrderId(int orderId) throws DataAccessException {
        String sql = """
                SELECT number, `order`, nameSnapshot, priceSnapshot, product, quantity
                FROM OrderLine
                WHERE `order` = ?
                ORDER BY number
                """;

        List<OrderLine> lines = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lines.add(mapOrderLine(resultSet));
                }
            }

            return lines;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving order lines for order id " + orderId + ".", e);
        }
    }

    private OrderLine mapOrderLine(ResultSet resultSet) throws SQLException {
        return new OrderLine(
                resultSet.getInt("number"),
                resultSet.getInt("order"),
                resultSet.getString("nameSnapshot"),
                resultSet.getDouble("priceSnapshot"),
                resultSet.getInt("product"),
                resultSet.getInt("quantity")
        );
    }
}