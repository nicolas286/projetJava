package viewPackage;

import controllerPackage.SearchController;
import exceptionPackage.BusinessException;
import modelPackage.ProductCategoryConstraintSearchResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchProductCategoryConstraintPanel extends JPanel {

    private final MainFrame parentFrame;
    private final SearchController searchController;

    private JTextField productIdField;
    private JTextField productNameField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchProductCategoryConstraintPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.searchController = new SearchController();

        buildInterface();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Search 2 - Product, Category and Constraints", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Product Id:"));
        productIdField = new JTextField(8);
        topPanel.add(productIdField);

        topPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField(12);
        topPanel.add(productNameField);

        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        topPanel.add(searchButton);
        topPanel.add(backButton);

        String[] columns = {"Product Id", "Product Name", "Price", "Category", "Constraint"};
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
            Integer productId = null;
            if (!productIdField.getText().trim().isEmpty()) {
                productId = Integer.parseInt(productIdField.getText().trim());
            }

            String productName = productNameField.getText().trim();

            List<ProductCategoryConstraintSearchResult> results =
                    searchController.searchProductCategoryConstraint(productId, productName);

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Product id must be a valid integer.", "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }
}