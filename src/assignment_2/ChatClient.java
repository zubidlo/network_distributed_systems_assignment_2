package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.List;
import static assignment_2.HelperClasses.Utils.*;

/**
 * Client implementation
 * Created by martin on 14/04/2015.
 */
class ChatClient extends UnicastRemoteObject implements Client, Serializable {

    private transient final ChatViewClassic chatView;
    private transient final Server server;
    private final String username;
    private final Color userColor;
    private final Icon icon;

    private ChatClient(Server server, String name, Icon i) throws RemoteException {
        this.server = server;
        username = name;
        chatView = new ChatViewClassic(String.format("Chat[%s]", username));
        userColor = randColor();
        icon = i;
        addListeners();
        server.connect(this);
    }

    private void addListeners() {
        chatView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                chatView.dispose();
                disconnectAndExit();
            }
        });

        chatView.messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) sendText();
            }
        });
    }

    private void disconnectAndExit() {
        try {
            server.disconnect(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void sendText() {
        try {
            server.send(new Line(icon, userColor, username, chatView.messageField.getText()));
            chatView.messageField.setText("");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void print(Line line) throws RemoteException {
        chatView.print(line);
    }

    @Override
    public void print(List<Client> connectedClients) throws RemoteException {
        chatView.print(connectedClients);
    }

    @Override
    public String getUserName() throws RemoteException {
        return username;
    }

    @Override
    public Color getColor() throws RemoteException {
        return userColor;
    }

    @Override
    public Icon getIcon() throws RemoteException {
        return icon;
    }

    private static void setUIManagerDefaults() {

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

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        if(args.length != 3) {
            System.out.println("Usage: java ChatClient hostname port rmi_id");
        }

        setUIManagerDefaults();

        String username = "anonymous";
        String result = JOptionPane.showInputDialog(
                null,
                "enter your nickname",
                username);

        if(!result.equals("")) username = result;

        Icon[] icons = Icons.getAll().stream()
                .filter(i -> !((ImageIcon) i).getDescription().equals("server.png"))
                .toArray(Icon[]::new);

        Icon chosenIcon = (Icon) JOptionPane.showInputDialog(
                null,
                "select your avatar",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                icons,
                Icons.get(0));

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String rmi_id = args[2];

        System.out.print("available RMI stubs: ");
        printRegistryList(makeRmiUrlString(hostname, port, rmi_id));
        Server server = (Server) Naming.lookup(makeRmiUrlString(hostname, port, rmi_id));
        new ChatClient(server, username, chosenIcon);
    }
}
