package viewPackage.Orders.Dialogs;

import controllerPackage.OrderController;
import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.entity.Order;
import modelPackage.enums.OrderStatus;
import modelPackage.entity.RestaurantTable;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFormDialog extends JDialog {

    private final OrderController orderController;
    private final RestaurantController restaurantController;
    private final Order existingOrder;
    private boolean saved;

    private JTextField dateOrderedField;
    private JTextField dateCompletedField;
    private JComboBox<OrderStatus> statusComboBox;
    private JCheckBox paidCheckBox;
    private JComboBox<RestaurantTable> tableComboBox;
    private JTextField dateDeliveredField;

    public OrderFormDialog(JFrame parent, OrderController orderController, RestaurantController restaurantController) {
        this(parent, orderController, restaurantController, null);
    }

    public OrderFormDialog(JFrame parent, OrderController orderController, RestaurantController restaurantController, Order existingOrder) {
        super(parent, existingOrder == null ? "Add Order" : "Edit Order", true);
        this.orderController = orderController;
        this.restaurantController = restaurantController;
        this.existingOrder = existingOrder;
        this.saved = false;

        buildInterface();
        loadTables();
        if (existingOrder != null) {
            fillForm();
        }
    }

    private void buildInterface() {
        setSize(520, 340);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dateOrderedField = new JTextField();
        dateCompletedField = new JTextField();
        statusComboBox = new JComboBox<>(OrderStatus.values());
        paidCheckBox = new JCheckBox("Paid");
        tableComboBox = new JComboBox<>();
        dateDeliveredField = new JTextField();

        formPanel.add(new JLabel("Date Ordered (yyyy-MM-ddTHH:mm):"));
        formPanel.add(dateOrderedField);

        formPanel.add(new JLabel("Date Completed (optional):"));
        formPanel.add(dateCompletedField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusComboBox);

        formPanel.add(new JLabel("Paid:"));
        formPanel.add(paidCheckBox);

        formPanel.add(new JLabel("Table:"));
        formPanel.add(tableComboBox);

        formPanel.add(new JLabel("Date Delivered (optional):"));
        formPanel.add(dateDeliveredField);

        JButton saveButton = new JButton(existingOrder == null ? "Save" : "Update");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveOrder());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTables() {
        try {
            List<RestaurantTable> tables = restaurantController.getAllTables();
            DefaultComboBoxModel<RestaurantTable> comboBoxModel = new DefaultComboBoxModel<>();

            for (RestaurantTable table : tables) {
                comboBoxModel.addElement(table);
            }

            tableComboBox.setModel(comboBoxModel);

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillForm() {
        dateOrderedField.setText(existingOrder.getDateOrdered().toString());

        if (existingOrder.getDateCompleted() != null) {
            dateCompletedField.setText(existingOrder.getDateCompleted().toString());
        }

        statusComboBox.setSelectedItem(existingOrder.getStatus());
        paidCheckBox.setSelected(existingOrder.isPaid());

        selectTableById(existingOrder.getTable().getId());

        if (existingOrder.getDateDelivered() != null) {
            dateDeliveredField.setText(existingOrder.getDateDelivered().toString());
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

    private void saveOrder() {
        try {
            LocalDateTime dateOrdered = LocalDateTime.parse(dateOrderedField.getText().trim());

            LocalDateTime dateCompleted = null;
            if (!dateCompletedField.getText().trim().isEmpty()) {
                dateCompleted = LocalDateTime.parse(dateCompletedField.getText().trim());
            }

            OrderStatus status = (OrderStatus) statusComboBox.getSelectedItem();
            boolean isPaid = paidCheckBox.isSelected();

            RestaurantTable selectedTable = (RestaurantTable) tableComboBox.getSelectedItem();
            if (selectedTable == null) {
                JOptionPane.showMessageDialog(this, "Please select a table.", "Validation error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime dateDelivered = null;
            if (!dateDeliveredField.getText().trim().isEmpty()) {
                dateDelivered = LocalDateTime.parse(dateDeliveredField.getText().trim());
            }

            Order order;
            if (existingOrder == null) {
                order = new Order(0, dateOrdered, dateCompleted, dateDelivered, status, isPaid, selectedTable);
                orderController.addOrder(order);
            } else {
                order = new Order(existingOrder.getId(), dateOrdered, dateCompleted, dateDelivered, status, isPaid, selectedTable);
                orderController.updateOrder(order);
            }

            saved = true;
            dispose();
        } catch (BusinessException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Business error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format.", "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}