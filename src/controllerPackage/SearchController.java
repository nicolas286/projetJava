package controllerPackage;

import businessPackage.SearchManager;
import exceptionPackage.BusinessException;
import modelPackage.search.LotStorageProductSearchResult;
import modelPackage.search.ProductCategoryConstraintSearchResult;
import modelPackage.search.TableOrderLineSearchResult;

import java.util.List;

public class SearchController {

    private final SearchManager searchManager;

    public SearchController() {
        this.searchManager = new SearchManager();
    }

    public List<TableOrderLineSearchResult> searchOrdersByTableId(int tableId) throws BusinessException {
        return searchManager.searchOrdersByTableId(tableId);
    }

    public List<ProductCategoryConstraintSearchResult> searchProductCategoryConstraint(Integer productId, String productName)
            throws BusinessException {
        return searchManager.searchProductCategoryConstraint(productId, productName);
    }

    public List<LotStorageProductSearchResult> searchLotStorageProduct(int lotId) throws BusinessException {
        return searchManager.searchLotStorageProduct(lotId);
    }
}