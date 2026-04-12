package viewPackage.Search;

import controllerPackage.SearchController;
import exceptionPackage.BusinessException;
import modelPackage.search.LotStorageProductSearchResult;
import viewPackage.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchLotStorageProductPanel extends JPanel {

    private final MainFrame parentFrame;
    private final SearchController searchController;

    private JTextField lotIdField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchLotStorageProductPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.searchController = new SearchController();

        buildInterface();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Search 3 - Lot, Storage and Products", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Lot Id:"));
        lotIdField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        topPanel.add(lotIdField);
        topPanel.add(searchButton);
        topPanel.add(backButton);

        String[] columns = {"Lot Id", "Quantity", "Purchase Price", "Product Id", "Product Name", "Storage Id", "Refrigerated"};
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
            int lotId = Integer.parseInt(lotIdField.getText().trim());
            List<LotStorageProductSearchResult> results = searchController.searchLotStorageProduct(lotId);

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lot id must be a valid integer.", "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }
}