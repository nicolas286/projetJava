package viewPackage.Search;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.search.LotStorageProductSearchResult;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import java.util.List;

public class SearchLotStorageProductPanel extends AbstractSearchPanel {

    private JTextField lotIdField;

    public SearchLotStorageProductPanel(MainFrame parentFrame, RestaurantController restaurantController) {
        super(parentFrame, restaurantController);
    }

    @Override
    protected String getTitleText() {
        return "Search 3 - Lot, Storage and Products";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{
                "Lot Id", "Quantity", "Purchase Price", "Product Id",
                "Product Name", "Storage Id", "Refrigerated"
        };
    }

    @Override
    protected JPanel buildSearchPanel() {
        JPanel panel = new JPanel();

        panel.add(LabelFactory.createFormLabel("Lot Id:"));
        lotIdField = new JTextField(10);
        panel.add(lotIdField);

        panel.add(createSearchButton());
        panel.add(createBackButton());

        return panel;
    }

    @Override
    protected void search() {
        try {
            int lotId = Integer.parseInt(lotIdField.getText().trim());
            List<LotStorageProductSearchResult> results = restaurantController.searchLotStorageProduct(lotId);

            tableModel.setRowCount(0);

            for (LotStorageProductSearchResult result : results) {
                tableModel.addRow(new Object[]{
                        result.getLotId(),
                        result.getQuantity(),
                        result.getPrice(),
                        result.getProductId(),
                        result.getProductName(),
                        result.getStorageId(),
                        result.isRefrigerated()
                });
            }
        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        } catch (NumberFormatException e) {
            DialogUtils.showValidationError(this, "Lot id must be a valid integer.");
        }
    }
}