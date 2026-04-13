package viewPackage.Shared.Factories;

import javax.swing.*;
import java.awt.*;

public final class LabelFactory {

    private LabelFactory() {
    }

    public static JLabel createTitleLabel(String text) {
        return createTitleLabel(text, 22);
    }

    public static JLabel createTitleLabel(String text, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
    }

    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
}