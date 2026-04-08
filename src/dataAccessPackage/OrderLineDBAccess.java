package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.OrderLine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineDBAccess extends AbstractDAO implements OrderLineDataAccess {

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
            throw new DataAccessException("Error while retrieving order lines.", e);
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