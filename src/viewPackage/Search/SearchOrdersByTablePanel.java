package viewPackage.Search;

import controllerPackage.SearchController;
import exceptionPackage.BusinessException;
import modelPackage.search.TableOrderLineSearchResult;
import viewPackage.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchOrdersByTablePanel extends JPanel {

    private final MainFrame parentFrame;
    private final SearchController searchController;

    private JTextField tableIdField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchOrdersByTablePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.searchController = new SearchController();

        buildInterface();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Search 1 - Orders of a Table and Their Lines", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Table Id:"));
        tableIdField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        topPanel.add(tableIdField);
        topPanel.add(searchButton);
        topPanel.add(backButton);

        String[] columns = {
                "Table Id", "Pos X", "Pos Y", "Floor", "Capacity", "Active",
                "Order Id", "Date Ordered", "Date Completed", "Date Delivered", "Status",
                "Line No", "Product Id", "Name Snapshot", "Price Snapshot", "Quantity"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        searchButton.addActionListener(e -> search());
        backButton.addActionListener(e -> parentFrame.showHomeView());

        add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void search() {
        try {
            int tableId = Integer.parseInt(tableIdField.getText().trim());
            List<TableOrderLineSearchResult> results = searchController.searchOrdersByTableId(tableId);

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Table id must be a valid integer.", "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }
}