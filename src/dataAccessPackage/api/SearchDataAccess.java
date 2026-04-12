package dataAccessPackage.api;

import exceptionPackage.DataAccessException;
import modelPackage.LotStorageProductSearchResult;
import modelPackage.ProductCategoryConstraintSearchResult;
import modelPackage.TableOrderLineSearchResult;

import java.util.List;

public interface SearchDataAccess {

    List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws DataAccessException;

    List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(Integer productId, String productName)
            throws DataAccessException;

    List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws DataAccessException;
}