package assignment_2;

import javax.swing.*;
import javax.swing.Icon;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * automatically creates list of ICONS from .png files in ICONS/ dir
 * images should be of 24x24 pixels size for visual consistency
 * Created by martin on 17/04/2015.
 */
class Icons {

    private static final List<Icon> ICONS;
    private static final String ICONS_FOLDER = "assignment_2/icons/";

    static {

        List<Icon> icons = new ArrayList<>();
        File folder = new File(getResource(ICONS_FOLDER).getPath());

        File[] files = Arrays.asList(folder.listFiles())
                .stream()
                .filter(File::isFile)
                .toArray(File[]::new);

        Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".png"))
                .forEachOrdered(f -> icons.add(new ImageIcon(f.getPath())));

        ICONS = Collections.unmodifiableList(icons);
    }

    private static URL getResource(final String path) {
        return Icons.class.getClassLoader().getResource(path);
    }

    static List<Icon> getAll() {
        return ICONS;
    }

    static Icon get(final int index) {
        if (index >= ICONS.size()) throw new IllegalArgumentException("no such icon");
        else return ICONS.get(index);
    }

    static Icon createIcon(final String filename) {
        URL url = getResource(ICONS_FOLDER + filename);
        if(url == null) System.err.println(filename + " not found");
        return new ImageIcon(url);
    }
}
