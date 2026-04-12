package dataAccessPackage.api;

import exceptionPackage.DataAccessException;
import modelPackage.Product;

import java.util.List;

public interface ProductDataAccess extends GenericDAO<Product, Integer> {

    List<Product> getProductsFromActiveMenus() throws DataAccessException;
}