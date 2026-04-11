package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.LotStorageProductSearchResult;
import modelPackage.ProductCategoryConstraintSearchResult;
import modelPackage.TableOrderLineSearchResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchDBAccess extends AbstractDAO implements SearchDataAccess {

    @Override
    public List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws DataAccessException {
        String sql = """
                SELECT
                    t.id AS table_id,
                    t.positionX,
                    t.positionY,
                    t.floor,
                    t.capacity,
                    t.isActive,
                    o.id AS order_id,
                    o.dateOrdered,
                    o.dateCompleted,
                    o.dateDelivered,
                    o.status,
                    ol.number AS line_number,
                    ol.product,
                    ol.nameSnapshot,
                    ol.priceSnapshot,
                    ol.quantity
                FROM `Table` t
                JOIN `Order` o ON o.`table` = t.id
                JOIN OrderLine ol ON ol.`order` = o.id
                WHERE t.id = ?
                ORDER BY o.id, ol.number
                """;

        List<TableOrderLineSearchResult> results = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, tableId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Integer floor = null;
                    int floorValue = rs.getInt("floor");
                    if (!rs.wasNull()) {
                        floor = floorValue;
                    }

                    LocalDateTime dateCompleted = rs.getTimestamp("dateCompleted") != null
                            ? rs.getTimestamp("dateCompleted").toLocalDateTime()
                            : null;

                    LocalDateTime dateDelivered = rs.getTimestamp("dateDelivered") != null
                            ? rs.getTimestamp("dateDelivered").toLocalDateTime()
                            : null;

                    results.add(new TableOrderLineSearchResult(
                            rs.getInt("table_id"),
                            rs.getInt("positionX"),
                            rs.getInt("positionY"),
                            floor,
                            rs.getInt("capacity"),
                            rs.getBoolean("isActive"),
                            rs.getInt("order_id"),
                            rs.getTimestamp("dateOrdered").toLocalDateTime(),
                            dateCompleted,
                            dateDelivered,
                            rs.getString("status"),
                            rs.getInt("line_number"),
                            rs.getInt("product"),
                            rs.getString("nameSnapshot"),
                            rs.getDouble("priceSnapshot"),
                            rs.getInt("quantity")
                    ));
                }
            }

            return results;
        } catch (SQLException e) {
            throw new DataAccessException("Error while searching orders by table.", e);
            // throw new DataAccessException("Error while searching orders by table id " + tableId + ".", e);
        }
    }

    @Override
    public List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(Integer productId, String productName)
            throws DataAccessException {

        String sql = """
                SELECT
                    p.id AS product_id,
                    p.name AS product_name,
                    p.price,
                    c.name AS category_name,
                    ct.name AS constraint_name
                FROM Product p
                JOIN CategoryProduct cp ON cp.product = p.id
                JOIN Category c ON c.id = cp.category
                LEFT JOIN ProductConstraint pc ON pc.product = p.id
                LEFT JOIN `Constraint` ct ON ct.id = pc.`constraint`
                WHERE (? IS NULL OR p.id = ?)
                  AND (? IS NULL OR LOWER(p.name) LIKE LOWER(?))
                ORDER BY p.name, c.name, ct.name
                """;

        List<ProductCategoryConstraintSearchResult> results = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            if (productId == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, productId);
                statement.setInt(2, productId);
            }

            if (productName == null || productName.isBlank()) {
                statement.setNull(3, java.sql.Types.VARCHAR);
                statement.setNull(4, java.sql.Types.VARCHAR);
            } else {
                statement.setString(3, productName);
                statement.setString(4, "%" + productName.trim() + "%");
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(new ProductCategoryConstraintSearchResult(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            rs.getString("category_name"),
                            rs.getString("constraint_name")
                    ));
                }
            }

            return results;
        } catch (SQLException e) {
            throw new DataAccessException("Error while searching product/category/constraint.", e);
            // throw new DataAccessException("Error while searching product/category/constraint for productId " + productId + " and productName " + productName + ".", e);
        }
    }

    @Override
    public List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws DataAccessException {
        String sql = """
                SELECT
                    l.id AS lot_id,
                    l.quantity,
                    l.price,
                    p.id AS product_id,
                    p.name AS product_name,
                    s.id AS storage_id,
                    s.isRefrigerated
                FROM Lot l
                JOIN Product p ON p.lot = l.id
                JOIN LotStorage ls ON ls.lot = l.id
                JOIN Storage s ON s.id = ls.storage
                WHERE l.id = ?
                ORDER BY p.name, s.id
                """;

        List<LotStorageProductSearchResult> results = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, lotId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(new LotStorageProductSearchResult(
                            rs.getInt("lot_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("storage_id"),
                            rs.getBoolean("isRefrigerated")
                    ));
                }
            }

            return results;
        } catch (SQLException e) {
            throw new DataAccessException("Error while searching lot/storage/product.", e);
            // throw new DataAccessException("Error while searching lot/storage/product for lot id " + lotId + ".", e);
        }
    }
}
