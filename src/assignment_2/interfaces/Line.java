package assignment_2.interfaces;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * chat line interface
 * Created by martin on 19/04/2015.
 */
public interface Line extends Serializable{
    Icon getIcon();
    Color getColor();
    String getName();
    String getText();
}
