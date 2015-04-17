package assignment_2;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * icon constants
 * Created by martin on 17/04/2015.
 */
class Icons {

    private static final List<Icon> icons = new ArrayList<>();

    static {
        Arrays.asList(
                "nurse.png",
                "youngGuy.png",
                "guyWithHat.png",
                "boldGuy.png",
                "ninja.png",
                "oldGuy.png",
                "server.png"
        ).forEach(f -> icons.add(getImage(f)));
    }

    private static ImageIcon getImage(String filename) {
        URL imgURL = Icons.class.getClassLoader().getResource("assignment_2/icons/" + filename);
        if(imgURL != null) {
            ImageIcon icon =  new ImageIcon(imgURL);
            icon.setDescription(filename);
            return icon;
        }
        else System.out.println("Couldn't find file: " + "assignment_2/icons/" + filename);
        return null;
    }

    static List<Icon> getAll() {
        return icons;
    }

    static Icon get(int index) {
        if (index >= icons.size()) throw new IllegalArgumentException("no such icon");
        else return icons.get(index);
    }

    static Icon getByFilename(String filename) {
        return icons.stream()
                .filter( i -> ((ImageIcon) i).getDescription().equals(filename))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("no such icon"));
    }
}
