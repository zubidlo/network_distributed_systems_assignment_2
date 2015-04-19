package assignment_2;

import assignment_2.interfaces.Line;

import javax.swing.*;
import java.awt.*;

/**
 * chat pane line bean
 * Created by martin on 17/04/2015.
 */
public final class ChatLine implements Line {

    private final Icon icon;
    private final Color color;
    private final String name;
    private final String text;

    public ChatLine(
            final Icon icon,
            final Color color,
            final String name,
            final String text) {

        this.icon = icon;
        this.color = color;
        this.name = name;
        this.text = text;
    }

    public final Icon getIcon() {
        return icon;
    }
    public final Color getColor() {
        return color;
    }
    public final String getName() {
        return name;
    }
    public final String getText() { return text; }
}
