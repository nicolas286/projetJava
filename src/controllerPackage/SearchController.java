package controllerPackage;

import businessPackage.SearchManager;
import exceptionPackage.BusinessException;
import modelPackage.LotStorageProductSearchResult;
import modelPackage.ProductCategoryConstraintSearchResult;
import modelPackage.TableOrderLineSearchResult;

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