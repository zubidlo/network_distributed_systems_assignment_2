package assignment_2.HelperClasses;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.util.Arrays;
import java.util.Enumeration;
import static java.lang.System.*;

/**
 * useful utilities
 * Created by martin on 18/04/2015.
 */
public class Utils {

    // Suppress default constructor for noninstantiability
    private Utils() {
        throw new AssertionError();
    }

    public static Color randColor() {
        return new Color(rand(), rand(), rand());
    }

    private static int rand() {
        return (int) Math.round(150 * Math.random() + 50);
    }

    public static void printInstalledFonts() {
        Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                .forEach(System.out::println);
    }

    public static void printUIManagerDefaults() {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())  {
            Object key = keys.nextElement();
            System.out.format("%s value:%s%n", key, UIManager.get(key));
        }
    }

    public static void printInstalledLookAndFeels() {
        Arrays.stream(UIManager.getInstalledLookAndFeels())
                .forEach(System.out::println);
    }

    public static String makeRmiUrlString(final String hostname,final int port,final String rmi_id) {
        return "rmi://" +  hostname + ":" + port + "/" + rmi_id;
    }

    public static void printRegistryList(final String urlString) {
        try {
            Arrays.stream(Naming.list(urlString)).forEach(out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String ipAddress() {
        String hostname = "localhost";
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostname;
    }

    public static void setUIManagerDefaults() {

        Color backgroundColor = Color.white;
        Color foregroundColor = Color.darkGray;
        Color selectionBackgroundColor = new Color(255, 204, 153);
        Font hoboFont = new Font("Hobo std", Font.PLAIN, 14);
        Icon questionMarkIcon = Icons.createIcon("bullet_question.jpg");

        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        UIManager.put("OptionPane.questionIcon", questionMarkIcon);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Button.background", backgroundColor);
        UIManager.put("Button.font", hoboFont);
        UIManager.put("Button.select", selectionBackgroundColor);
        UIManager.put("ComboBox.background", backgroundColor);
        UIManager.put("ComboBox.selectionBackground", selectionBackgroundColor);
        UIManager.put("TextField.selectionBackground", selectionBackgroundColor);
        UIManager.put("TextField.foreground", foregroundColor);
        UIManager.put("TextField.selectionForeground", foregroundColor);
        UIManager.put("TextField.font", hoboFont);
        UIManager.put("Label.font", hoboFont);
    }

    public static void main(String[] args) {
        //printInstalledFonts();
        //printInstalledLookAndFeels();
        printUIManagerDefaults();
        //out.println(ipAddress());
    }
}
