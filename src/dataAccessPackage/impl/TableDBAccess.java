package dataAccessPackage.impl;

import dataAccessPackage.api.GenericDAO;
import dataAccessPackage.api.TableDataAccess;
import dataAccessPackage.core.AbstractDataAccess;
import exceptionPackage.DataAccessException;
import modelPackage.entity.RestaurantTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDBAccess extends AbstractDataAccess implements TableDataAccess, GenericDAO<RestaurantTable, Integer> {

    @Override
    public void insert(RestaurantTable entity) throws DataAccessException {
        // Pas d'implémentation CRUD complet (hors scope)
    }

    @Override
    public void update(RestaurantTable entity) throws DataAccessException {
        // Pas d'implémentation CRUD complet (hors scope)
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        // Pas d'implémentation CRUD complet (hors scope)
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