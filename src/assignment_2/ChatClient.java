package assignment_2;

import assignment_2.helperClasses.*;
import assignment_2.interfaces.*;
import static assignment_2.helperClasses.Constants.*;

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

    private transient final FrameSkeleton frame;
    private transient Server server;
    private final String username;
    private final Color userColor;
    private String currentText;
    private transient int onlineUsersCount;


    ChatClient(final String name) throws RemoteException, NotBoundException {
        username = name;
        frame = new FrameSkeleton(String.format("Chat[%s]", username));
        userColor = FrameSkeleton.randColor();
        lookUpRemote();
        onlineUsersCount = 0;
        addListeners();
        currentText = "...connected...";
        server.connect(this);
    }

    private void lookUpRemote() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(HOST, PORT);
        server = (Server) registry.lookup(RMI_ID);
    }

    private void addListeners() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                disconnectAndExit();
            }
        });
        frame.messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) sendText();
            }
        });
    }

    private void disconnectAndExit() {
        try {
            currentText = "...disconnected...";
            server.disconnect(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private void sendText() {
        try {
            currentText = frame.messageField.getText();
            server.send(ChatClient.this);
            frame.messageField.setText("");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void notify(Client client) throws RemoteException {
        List<Client> clients = server.getConnectedClients();
        if(clients.size() != onlineUsersCount) {
            frame.refillUsersOnlinePane(clients);
            onlineUsersCount = clients.size();
        }
        frame.addToChatPane(client.getColor(), client.getUserName(), client.getText());
    }

    @Override
    public String getUserName() throws RemoteException {
        return new String(username);
    }

    @Override
    public Color getColor() throws RemoteException {
        return new Color(userColor.getRed(), userColor.getGreen(), userColor.getBlue());
    }

    @Override
    public String getText() throws RemoteException {
        return new String(currentText);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String name = "anonymous";
        String result = JOptionPane.showInputDialog(null, "enter your name");
        if(!result.equals("")) name = result;
        new ChatClient(name);
    }
}
