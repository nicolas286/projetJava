import businessPackage.TableManager;
import controllerPackage.TableController;
import dataAccessPackage.TableDBAccess;
import viewPackage.TableListView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableManager tableManager = new TableManager(new TableDBAccess());
            TableController tableController = new TableController(tableManager);
            TableListView tableListView = new TableListView(tableController);
            tableListView.setVisible(true);
        });
    }
}