package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * chat pane line bean
 * Created by martin on 17/04/2015.
 */
final class ChatLine implements Serializable{

    final Icon icon;
    final Color color;
    final String name;
    final String text;

    ChatLine(Icon i, Color c, String n, String t) {

        icon = i;
        color = c;
        name = n;
        text = t;
    }
}
