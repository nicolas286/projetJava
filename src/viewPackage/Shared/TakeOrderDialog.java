package viewPackage.Shared;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Product;
import modelPackage.entity.RestaurantTable;
import modelPackage.input.TakeOrderLine;

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

    public TakeOrderDialog(JFrame parent) {
        this(parent, new RestaurantController());
    }

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

    public TakeOrderDialog(JFrame parent, RestaurantController restaurantController, RestaurantTable preselectedTable) {
        this(parent, restaurantController);
        selectTableById(preselectedTable.getId());
        tableComboBox.setEnabled(false);
    }

    private void buildInterface() {
        setSize(750, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableComboBox = new JComboBox<>();
        productComboBox = new JComboBox<>();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

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
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void selectTableById(int tableId) {
        ComboBoxModel<RestaurantTable> model = tableComboBox.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            RestaurantTable table = model.getElementAt(i);
            if (table.getId() == tableId) {
                tableComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void addLine() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a product.",
                    "Validation error",
                    JOptionPane.ERROR_MESSAGE
            );
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
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a table.",
                        "Validation error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (lines.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please add at least one line.",
                        "Validation error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            restaurantController.takeOrder(selectedTable.getId(), lines);

            saved = true;
            JOptionPane.showMessageDialog(
                    this,
                    "Order created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Validation error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Business error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSaved() {
        return saved;
    }
}