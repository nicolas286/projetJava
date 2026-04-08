package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDBAccess extends AbstractDAO implements ProductDataAccess {

    @Override
    public void insert(Product entity) throws DataAccessException {
        String sql = "INSERT INTO Product (id, name, price, lot) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setDouble(3, entity.getPrice());
            statement.setInt(4, entity.getLotId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while inserting product.", e);
        }
    }

    @Override
    public void update(Product entity) throws DataAccessException {
        String sql = "UPDATE Product SET name = ?, price = ?, lot = ? WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getPrice());
            statement.setInt(3, entity.getLotId());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating product.", e);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String sql = "DELETE FROM Product WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting product.", e);
        }
    }

    @Override
    public Product findById(Integer id) throws DataAccessException {
        return getProductById(id);
    }

    @Override
    public List<Product> findAll() throws DataAccessException {
        return getAllProducts();
    }

    @Override
    public List<Product> getAllProducts() throws DataAccessException {
        String sql = "SELECT id, name, price, lot FROM Product ORDER BY name";
        List<Product> products = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving products.", e);
        }
    }

    @Override
    public Product getProductById(int id) throws DataAccessException {
        String sql = "SELECT id, name, price, lot FROM Product WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving product.", e);
        }
    }

    @Override
    public List<Product> getProductsFromActiveMenus() throws DataAccessException {
        String sql = """
                SELECT DISTINCT p.id, p.name, p.price, p.lot
                FROM Product p
                JOIN MenuLine ml ON ml.product = p.id
                JOIN Menu m ON m.name = ml.menu
                WHERE m.timePeriodStart <= ?
                  AND (m.timePeriodEnd IS NULL OR m.timePeriodEnd >= ?)
                ORDER BY p.name
                """;

        List<Product> products = new ArrayList<>();
        Timestamp now = Timestamp.valueOf(java.time.LocalDateTime.now());

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, now);
            statement.setTimestamp(2, now);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }

            return products;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving products from active menus.", e);
        }
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getInt("lot")
        );
    }
}