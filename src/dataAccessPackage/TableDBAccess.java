package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.RestaurantTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDBAccess extends AbstractDAO<RestaurantTable, Integer> implements TableDataAccess {

    @Override
    public void insert(RestaurantTable entity) throws DataAccessException {
        String sql = "INSERT INTO `Table` (id, positionX, positionY, floor, capacity, isActive) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            statement.setInt(2, entity.getPositionX());
            statement.setInt(3, entity.getPositionY());

            if (entity.getFloor() != null) {
                statement.setInt(4, entity.getFloor());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            statement.setInt(5, entity.getCapacity());
            statement.setBoolean(6, entity.isActive());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while inserting table.", e);
        }
    }

    @Override
    public void update(RestaurantTable entity) throws DataAccessException {
        String sql = "UPDATE `Table` SET positionX = ?, positionY = ?, floor = ?, capacity = ?, isActive = ? WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, entity.getPositionX());
            statement.setInt(2, entity.getPositionY());

            if (entity.getFloor() != null) {
                statement.setInt(3, entity.getFloor());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }

            statement.setInt(4, entity.getCapacity());
            statement.setBoolean(5, entity.isActive());
            statement.setInt(6, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while updating table.", e);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String sql = "DELETE FROM `Table` WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting table.", e);
        }
    }

    @Override
    public RestaurantTable findById(Integer id) throws DataAccessException {
        String sql = "SELECT id, positionX, positionY, floor, capacity, isActive FROM `Table` WHERE id = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapTable(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving table with id " + id + ".", e);
        }
    }

    @Override
    public List<RestaurantTable> findAll() throws DataAccessException {
        String sql = "SELECT id, positionX, positionY, floor, capacity, isActive FROM `Table` ORDER BY id";
        List<RestaurantTable> tables = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tables.add(mapTable(resultSet));
            }

            return tables;
        } catch (SQLException e) {
            throw new DataAccessException("Error while retrieving tables.", e);
        }
    }

    private RestaurantTable mapTable(ResultSet resultSet) throws SQLException {
        Integer floor = null;
        int floorValue = resultSet.getInt("floor");
        if (!resultSet.wasNull()) {
            floor = floorValue;
        }

        return new RestaurantTable(
                resultSet.getInt("id"),
                resultSet.getInt("positionX"),
                resultSet.getInt("positionY"),
                floor,
                resultSet.getInt("capacity"),
                resultSet.getBoolean("isActive")
        );
    }
}