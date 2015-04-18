package assignment_2;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * automatically creates list of icons from .png files in icons/ dir
 * images should be of 24x24 pixels size for visual consistency
 * Created by martin on 17/04/2015.
 */
class Icons {

    private static final List<Icon> icons = new ArrayList<>();
    private static final String iconsDirName = "assignment_2/icons/";

    static {
        File folder = new File(getResource(iconsDirName).getPath());

        File[] files = Arrays.asList(folder.listFiles())
                .stream()
                .filter(File::isFile)
                .toArray(File[]::new);

        Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".png"))
                .forEachOrdered(f -> icons.add(new ImageIcon(f.getPath())));
    }

    private static URL getResource(String path) {
        return Icons.class.getClassLoader().getResource(path);
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
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no such icon"));
    }

    static Icon createIcon(String filename) {
        URL url = getResource(iconsDirName + filename);
        if(url == null) System.out.println(filename + " not found");
        return new ImageIcon(url);
    }
}
