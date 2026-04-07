package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.RestaurantTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDBAccess extends AbstractDAO implements TableDataAccess {

    @Override
    public List<RestaurantTable> getAllTables() throws DataAccessException {
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

    @Override
    public RestaurantTable getTableById(int id) throws DataAccessException {
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