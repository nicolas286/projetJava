package viewPackage.Shared.Factories;

import javax.swing.*;
import java.awt.*;

public final class FormFactory {

    private FormFactory() {
    }

    public static JPanel createFormPanel(int rows, int cols) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    public static void addFormRow(JPanel panel, String labelText, JComponent field) {
        panel.add(LabelFactory.createFormLabel(labelText));
        panel.add(field);
    }

    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }
}