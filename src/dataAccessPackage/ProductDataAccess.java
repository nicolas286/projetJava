package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Product;

import java.util.List;

public interface ProductDataAccess extends GenericDAO<Product, Integer> {

    List<Product> getAllProducts() throws DataAccessException; // redondant (même remarque = findAll de la générique)

    Product getProductById(int id) throws DataAccessException; // idem = findById

    List<Product> getProductsFromActiveMenus() throws DataAccessException; // ok
}
