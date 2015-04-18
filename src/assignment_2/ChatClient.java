package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
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
            server.send(new Line(icon, userColor, username, currentText));
            chatView.messageField.setText("");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void postMessage(Line line) throws RemoteException {
        chatView.postMessage(line);
    }

    @Override
    public void updateConnectedClientList(List<Client> connectedClients) throws RemoteException {
        chatView.updateConnectedUsers(connectedClients);
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

    public static void main(String[] args) throws RemoteException, NotBoundException {
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

        new ChatClient(username, chosenIcon, hostname, port, rmi_id);
    }
}
