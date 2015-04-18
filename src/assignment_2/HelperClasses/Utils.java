package assignment_2.HelperClasses;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Enumeration;
import static java.lang.System.*;

/**
 * useful utilities
 * Created by martin on 18/04/2015.
 */
public class Utils {

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

    public static String makeRmiUrlString(String hostname, int port, String rmi_id) {
        return "rmi://" +  hostname + ":" + port + "/" + rmi_id;
    }

    public static void printRegistryList(String urlString) {
        try {
            Arrays.stream(Naming.list(urlString)).forEach(out::println);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
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

    public static void main(String[] args) {
        out.println(ipAddress());
    }
}
