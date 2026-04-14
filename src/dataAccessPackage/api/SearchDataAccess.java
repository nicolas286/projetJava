package dataAccessPackage.api;

import exceptionPackage.DataAccessException;
import modelPackage.search.LotStorageProductSearchResult;
import modelPackage.search.ProductCategoryConstraintSearchResult;
import modelPackage.search.TableOrderLineSearchResult;

import java.util.List;

public interface SearchDataAccess {

    List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws DataAccessException;

    List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(Integer productId, String productName)
            throws DataAccessException;

    List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws DataAccessException;
}