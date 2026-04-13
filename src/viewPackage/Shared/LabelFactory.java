package viewPackage.Shared;

import javax.swing.*;
import java.awt.*;

public final class LabelFactory {

    private LabelFactory() {
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return label;
    }
}