package viewPackage.Shared.Factories;

import javax.swing.*;
import java.awt.event.ActionListener;

public final class ButtonFactory {

    private ButtonFactory() {
    }

    public static JButton createPrimaryButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    public static JButton createDisabledButton(String text) {
        JButton button = new JButton(text);
        button.setEnabled(false);
        return button;
    }

    public static JButton createButton(String text) {
        return new JButton(text);
    }
}