package viewPackage.Search;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.search.ProductCategoryConstraintSearchResult;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import java.util.List;

public class SearchProductCategoryConstraintPanel extends AbstractSearchPanel {

    private JTextField productIdField;
    private JTextField productNameField;

    public SearchProductCategoryConstraintPanel(MainFrame parentFrame, RestaurantController restaurantController) {
        super(parentFrame, restaurantController);
    }

    @Override
    protected String getTitleText() {
        return "Search 2 - Product, Category and Constraints";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{
                "Product Id", "Product Name", "Price", "Category", "Constraint"
        };
    }

    @Override
    protected JPanel buildSearchPanel() {
        JPanel panel = new JPanel();

        panel.add(LabelFactory.createFormLabel("Product Id:"));
        productIdField = new JTextField(8);
        panel.add(productIdField);

        panel.add(LabelFactory.createFormLabel("Product Name:"));
        productNameField = new JTextField(12);
        panel.add(productNameField);

        panel.add(createSearchButton());
        panel.add(createBackButton());

        return panel;
    }

    @Override
    protected void search() {
        try {
            Integer productId = null;
            if (!productIdField.getText().trim().isEmpty()) {
                productId = Integer.parseInt(productIdField.getText().trim());
            }

            String productName = productNameField.getText().trim();

            List<ProductCategoryConstraintSearchResult> results =
                    restaurantController.searchProductCategoryConstraint(productId, productName);

            tableModel.setRowCount(0);

            for (ProductCategoryConstraintSearchResult result : results) {
                tableModel.addRow(new Object[]{
                        result.getProductId(),
                        result.getProductName(),
                        result.getProductPrice(),
                        result.getCategoryName(),
                        result.getConstraintName()
                });
            }

        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        } catch (NumberFormatException e) {
            DialogUtils.showValidationError(this, "Product id must be a valid integer.");
        }
    }
}