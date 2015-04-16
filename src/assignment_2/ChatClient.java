package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.URL;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private volatile String currentText;


    private ChatClient(
            final String name,
            final Icon i,
            String hostname,
            int port,
            String rmi_id) throws RemoteException, NotBoundException {
        username = name;
        chatView = new ChatViewClassic(String.format("Chat[%s]", username));
        userColor = ChatViewClassic.randColor();
        icon = i;
        server = lookUpRemote(hostname, port, rmi_id);
        addListeners();
        currentText = " ...connected...";
        server.connect(this);
    }

    private Server lookUpRemote(String hostname, int port, String rmi_id) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        return  (Server) registry.lookup(rmi_id);
    }

    private void addListeners() {
        chatView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
            currentText = " ...disconnected...";
            server.disconnect(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void sendText() {
        try {
            currentText = chatView.messageField.getText();
            server.send(ChatClient.this);
            chatView.messageField.setText("");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void postMessage(
            final Icon icon,
            final Color userColor,
            final String username,
            final String text) throws RemoteException {
        chatView.postMessage(icon, userColor, username, text);
    }

    @Override
    public void updateConnectedClientList(List<Client> connectedClients) throws RemoteException {
        chatView.updateOnlineUsers(connectedClients);
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
    public String getText() throws RemoteException {
        return currentText;
    }

    @Override
    public Icon getIcon() throws RemoteException {
        return icon;
    }

    private static final List<Icon> ICONS = new ArrayList<>();

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
        URL imgURL = ChatClient.class.getClassLoader().getResource("assignment_2/icons/" + filename);
        if(imgURL != null) return new ImageIcon(imgURL);
        else System.out.println("Couldn't find file: " + "assignment_2/icons/" + filename);
        return null;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String username = "anonymous";
        String result = JOptionPane.showInputDialog(
                null,
                "enter your nickname",
                username);

        if(!result.equals("")) username = result;


        Icon initialIcon = ICONS.get(0);
        Icon[] icons = ICONS.toArray(new Icon[0]);
        Icon chosenIcon = (Icon) JOptionPane.showInputDialog(
                null,
                "select your avatar",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                icons,
                initialIcon);

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String rmi_id = args[2];

        new ChatClient(username, chosenIcon, hostname, port, rmi_id);
    }
}
