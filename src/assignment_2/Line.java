package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * chat pane line bean
 * Created by martin on 17/04/2015.
 */
public class Line implements Serializable{

    private final Icon icon;
    private final Color color;
    private final String name;
    private final String text;

    public Line(Icon icon, Color color, String name, String text) {
        this.icon = icon;
        this.color = color;
        this.name = name;
        this.text = text;
    }

    public Icon getIcon() {
        return icon;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
