package viewPackage.Orders.Dialogs;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import exceptionPackage.ValidationException;
import modelPackage.entity.Order;
import modelPackage.entity.RestaurantTable;
import modelPackage.enums.OrderStatus;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.FormFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFormDialog extends JDialog {

    private final RestaurantController restaurantController;
    private final Order existingOrder;
    private boolean saved;

    private JTextField dateOrderedField;
    private JTextField dateCompletedField;
    private JComboBox<OrderStatus> statusComboBox;
    private JCheckBox paidCheckBox;
    private JComboBox<RestaurantTable> tableComboBox;
    private JTextField dateDeliveredField;

    public OrderFormDialog(JFrame parent, RestaurantController restaurantController, Order existingOrder) {
        super(parent, "Edit Order", true);

        if (restaurantController == null) {
            throw new IllegalArgumentException("RestaurantController cannot be null.");
        }
        if (existingOrder == null) {
            throw new IllegalArgumentException("Existing order cannot be null.");
        }

        this.restaurantController = restaurantController;
        this.existingOrder = existingOrder;
        this.saved = false;

        buildInterface();
        loadTables();
        fillForm();
    }

    private void buildInterface() {
        setSize(520, 340);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = FormFactory.createFormPanel(6, 2);

        dateOrderedField = new JTextField();
        dateCompletedField = new JTextField();
        statusComboBox = new JComboBox<>(OrderStatus.values());
        paidCheckBox = new JCheckBox();
        tableComboBox = new JComboBox<>();
        dateDeliveredField = new JTextField();

        FormFactory.addFormRow(formPanel, "Date Ordered (yyyy-MM-ddTHH:mm):", dateOrderedField);
        FormFactory.addFormRow(formPanel, "Date Completed (optional):", dateCompletedField);
        FormFactory.addFormRow(formPanel, "Status:", statusComboBox);
        FormFactory.addFormRow(formPanel, "Paid:", paidCheckBox);
        FormFactory.addFormRow(formPanel, "Table:", tableComboBox);
        FormFactory.addFormRow(formPanel, "Date Delivered (optional):", dateDeliveredField);

        JPanel buttonPanel = FormFactory.createButtonPanel(
                ButtonFactory.createPrimaryButton("Update", e -> saveOrder()),
                ButtonFactory.createPrimaryButton("Cancel", e -> dispose())
        );

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
            DialogUtils.showError(this, e.getMessage());
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
            LocalDateTime dateOrdered = parseRequiredDateTime(
                    dateOrderedField.getText(),
                    "Date Ordered must be provided in format yyyy-MM-ddTHH:mm."
            );

            LocalDateTime dateCompleted = parseOptionalDateTime(
                    dateCompletedField.getText(),
                    "Date Completed must use format yyyy-MM-ddTHH:mm."
            );

            OrderStatus status = (OrderStatus) statusComboBox.getSelectedItem();
            boolean isPaid = paidCheckBox.isSelected();

            RestaurantTable selectedTable = (RestaurantTable) tableComboBox.getSelectedItem();
            if (selectedTable == null) {
                DialogUtils.showValidationError(this, "Please select a table.");
                return;
            }

            LocalDateTime dateDelivered = parseOptionalDateTime(
                    dateDeliveredField.getText(),
                    "Date Delivered must use format yyyy-MM-ddTHH:mm."
            );

            Order updatedOrder = new Order(
                    existingOrder.getId(),
                    dateOrdered,
                    dateCompleted,
                    dateDelivered,
                    status,
                    isPaid,
                    selectedTable
            );

            restaurantController.updateOrder(updatedOrder);

            saved = true;
            dispose();

        } catch (ValidationException ex) {
            DialogUtils.showValidationError(this, ex.getMessage());
        } catch (BusinessException ex) {
            DialogUtils.showError(this, ex.getMessage());
        }
    }

    private LocalDateTime parseRequiredDateTime(String text, String errorMessage) throws ValidationException {
        String value = text.trim();

        if (value.isEmpty()) {
            throw new ValidationException(errorMessage);
        }

        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            throw new ValidationException(errorMessage);
        }
    }

    private LocalDateTime parseOptionalDateTime(String text, String errorMessage) throws ValidationException {
        String value = text.trim();

        if (value.isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            throw new ValidationException(errorMessage);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}