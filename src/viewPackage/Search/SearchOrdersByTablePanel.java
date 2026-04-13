package viewPackage.Search;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.search.TableOrderLineSearchResult;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import java.util.List;

public class SearchOrdersByTablePanel extends AbstractSearchPanel {

    private JTextField tableIdField;

    public SearchOrdersByTablePanel(MainFrame parentFrame, RestaurantController restaurantController) {
        super(parentFrame, restaurantController);
    }

    @Override
    protected String getTitleText() {
        return "Search 1 - Orders of a Table and Their Lines";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{
                "Table Id", "Pos X", "Pos Y", "Floor", "Capacity", "Active",
                "Order Id", "Date Ordered", "Date Completed", "Date Delivered", "Status",
                "Line No", "Product Id", "Name Snapshot", "Price Snapshot", "Quantity"
        };
    }

    @Override
    protected JPanel buildSearchPanel() {
        JPanel panel = new JPanel();

        panel.add(LabelFactory.createFormLabel("Table Id:"));
        tableIdField = new JTextField(10);
        panel.add(tableIdField);

        panel.add(createSearchButton());
        panel.add(createBackButton());

        return panel;
    }

    @Override
    protected void search() {
        try {
            int tableId = Integer.parseInt(tableIdField.getText().trim());
            List<TableOrderLineSearchResult> results = restaurantController.searchOrdersByTableId(tableId);

            tableModel.setRowCount(0);

            for (TableOrderLineSearchResult result : results) {
                tableModel.addRow(new Object[]{
                        result.getTableId(),
                        result.getPositionX(),
                        result.getPositionY(),
                        result.getFloor(),
                        result.getCapacity(),
                        result.isActive(),
                        result.getOrderId(),
                        result.getDateOrdered(),
                        result.getDateCompleted(),
                        result.getDateDelivered(),
                        result.getStatus(),
                        result.getLineNumber(),
                        result.getProductId(),
                        result.getNameSnapshot(),
                        result.getPriceSnapshot(),
                        result.getQuantity()
                });
            }
        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        } catch (NumberFormatException e) {
            DialogUtils.showValidationError(this, "Table id must be a valid integer.");
        }
    }
}
