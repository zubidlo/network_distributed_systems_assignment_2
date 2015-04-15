package assignment_2.helperClasses;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constants to set RMI
 * Created by martin on 14/04/2015.
 */
public class Constants {
    public static final String HOST = "localhost";
    public static final int PORT = 2001;
    public static final String RMI_ID = "chat_server";
    public static final List<Icon> ICONS = new ArrayList<>();

    static {
        Arrays.asList(
                "icon1.png",
                "icon2.png",
                "icon3.png",
                "icon4.png",
                "icon5.png",
                "icon6.png"
        ).forEach(f -> ICONS.add(getImage(f)));
    }

    private static ImageIcon getImage(String filename) {
        URL imgURL = (Constants.class.getClassLoader().getResource("assignment_2/icons/" + filename));
        if(imgURL != null) return new ImageIcon(imgURL);
        else System.out.println("Couldn't find file: " + "assignment_2/icons/" + filename);
        return null;
    }

    public static void printInstalledFonts() {
        Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                .forEach(System.out::println);
    }
}
