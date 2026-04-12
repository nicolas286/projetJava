package businessPackage.Managers;

import dataAccessPackage.impl.SearchDBAccess;
import dataAccessPackage.api.SearchDataAccess;
import exceptionPackage.BusinessException;
import exceptionPackage.DataAccessException;
import modelPackage.search.LotStorageProductSearchResult;
import modelPackage.search.ProductCategoryConstraintSearchResult;
import modelPackage.search.TableOrderLineSearchResult;

import java.util.List;

public class SearchManager {

    private final SearchDataAccess searchDataAccess;

    public SearchManager() {
        this(new SearchDBAccess());
    }

    public SearchManager(SearchDataAccess searchDataAccess) {
        if (searchDataAccess == null) {
            throw new IllegalArgumentException("SearchDataAccess cannot be null.");
        }
        this.searchDataAccess = searchDataAccess;
    }

    public List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws BusinessException {
        if (tableId <= 0) {
            throw new BusinessException("Table id must be positive.");
        }

        try {
            return searchDataAccess.searchOrdersByTableId(tableId);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to search orders for this table.", e);
        }
    }

    public List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(Integer productId, String productName)
            throws BusinessException {
        if ((productId == null || productId <= 0) && (productName == null || productName.isBlank())) {
            throw new BusinessException("Please enter a product id or a product name.");
        }

        try {
            return searchDataAccess.searchProductCategoryConstraint(productId, productName);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to search product/category/constraint.", e);
        }
    }

    public List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws BusinessException {
        if (lotId <= 0) {
            throw new BusinessException("Lot id must be positive.");
        }

        try {
            return searchDataAccess.searchLotStorageProduct(lotId);
        } catch (DataAccessException e) {
            throw new BusinessException("Unable to search lot/storage/product.", e);
        }
    }
}