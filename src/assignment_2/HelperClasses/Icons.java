package assignment_2.HelperClasses;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * automatically creates list of ICONS from .png files in ICONS/ dir
 * images should be of 24x24 pixels size for visual consistency
 * Created by martin on 17/04/2015.
 */
public class Icons {

    private static final List<Icon> ICONS = new ArrayList<>();
    private static final String ICONS_FOLDER = "assignment_2/icons/";

    static {
        File folder = new File(getResource(ICONS_FOLDER).getPath());

        File[] files = Arrays.asList(folder.listFiles())
                .stream()
                .filter(File::isFile)
                .toArray(File[]::new);

        Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".png"))
                .forEachOrdered(f -> ICONS.add(new ImageIcon(f.getPath())));
    }

    private static URL getResource(String path) {
        return Icons.class.getClassLoader().getResource(path);
    }

    public static List<Icon> getAll() {
        return ICONS;
    }

    public static Icon get(int index) {
        if (index >= ICONS.size()) throw new IllegalArgumentException("no such icon");
        else return ICONS.get(index);
    }

    public static Icon createIcon(String filename) {
        URL url = getResource(ICONS_FOLDER + filename);
        if(url == null) System.err.println(filename + " not found");
        return new ImageIcon(url);
    }
}
