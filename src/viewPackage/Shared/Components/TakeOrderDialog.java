package viewPackage.Shared.Components;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Product;
import modelPackage.entity.RestaurantTable;
import modelPackage.input.TakeOrderLine;
import viewPackage.Shared.Factories.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TakeOrderDialog extends JDialog {

    private final RestaurantController restaurantController;
    private boolean saved;

    private JComboBox<RestaurantTable> tableComboBox;
    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;

    private JTable linesTable;
    private DefaultTableModel linesTableModel;

    private final List<TakeOrderLine> lines;

    public TakeOrderDialog(JFrame parent, RestaurantController restaurantController) {
        super(parent, "Take Order", true);
        if (restaurantController == null) {
            throw new IllegalArgumentException("RestaurantController cannot be null.");
        }

        this.restaurantController = restaurantController;
        this.saved = false;
        this.lines = new ArrayList<>();

        buildInterface();
        loadTables();
        loadProducts();
    }
    private void buildInterface() {
        setSize(750, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableComboBox = new JComboBox<>();
        productComboBox = new JComboBox<>();
        quantitySpinner = createQuantitySpinner();

        formPanel.add(new JLabel("Table:"));
        formPanel.add(tableComboBox);

        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantitySpinner);

        String[] columnNames = {"Product", "Unit price", "Quantity", "Line total"};
        linesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        linesTable = new JTable(linesTableModel);
        JScrollPane scrollPane = new JScrollPane(linesTable);

        JButton addLineButton = new JButton("Add line");
        JButton removeLineButton = new JButton("Remove selected line");
        JButton confirmButton = new JButton("Confirm order");
        JButton cancelButton = new JButton("Cancel");

        addLineButton.addActionListener(e -> addLine());
        removeLineButton.addActionListener(e -> removeSelectedLine());
        confirmButton.addActionListener(e -> confirmOrder());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addLineButton);
        buttonPanel.add(removeLineButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTables() {
        try {
            List<RestaurantTable> tables = restaurantController.getAllTables();
            DefaultComboBoxModel<RestaurantTable> comboBoxModel = new DefaultComboBoxModel<>();

            for (RestaurantTable table : tables) {
                if (table.isActive()) {
                    comboBoxModel.addElement(table);
                }
            }

            tableComboBox.setModel(comboBoxModel);

        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            List<Product> products = restaurantController.getAvailableProducts();
            DefaultComboBoxModel<Product> comboBoxModel = new DefaultComboBoxModel<>();

            for (Product product : products) {
                comboBoxModel.addElement(product);
            }

            productComboBox.setModel(comboBoxModel);

        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        }
    }

    private JSpinner createQuantitySpinner() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JFormattedTextField textField =
                ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setEditable(false);
        textField.setFocusable(false);

        return spinner;
    }

    private void addLine() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (selectedProduct == null) {
            DialogUtils.showValidationError(this, "Please select a product.");
            return;
        }

        TakeOrderLine line = new TakeOrderLine(selectedProduct, quantity);
        lines.add(line);

        double lineTotal = selectedProduct.getPrice() * quantity;

        Object[] row = {
                selectedProduct.getName(),
                selectedProduct.getPrice(),
                quantity,
                lineTotal
        };
        linesTableModel.addRow(row);
    }

    private void removeSelectedLine() {
        int selectedRow = linesTable.getSelectedRow();

        if (selectedRow >= 0) {
            lines.remove(selectedRow);
            linesTableModel.removeRow(selectedRow);
        }
    }

    private void confirmOrder() {
        try {
            RestaurantTable selectedTable = (RestaurantTable) tableComboBox.getSelectedItem();

            if (selectedTable == null) {
                DialogUtils.showValidationError(this, "Please select a table.");
                return;
            }

            if (lines.isEmpty()) {
                DialogUtils.showValidationError(this, "Please add at least one line.");
                return;
            }

            restaurantController.takeOrder(selectedTable.getId(), lines);

            saved = true;
            DialogUtils.showInfo(this, "Order created successfully.");
            dispose();

        } catch (ValidationException e) {
            DialogUtils.showValidationError(this, e.getMessage());
        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}