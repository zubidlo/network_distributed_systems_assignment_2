package assignment_2;

import assignment_2.interfaces.*;
import static assignment_2.helperClasses.Utilities.*;

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
    private transient int onlineUsersCount;
    private final String username;
    private final Color userColor;
    private final Icon icon;
    private String currentText;


    private ChatClient(final String name, final Icon i) throws RemoteException, NotBoundException {
        username = name;
        chatView = new ChatViewClassic(String.format("Chat[%s]", username));
        userColor = ChatViewClassic.randColor();
        icon = i;
        server = lookUpRemote();
        onlineUsersCount = 0;
        addListeners();
        currentText = " ...connected...";
        server.connect(this);
    }

    private Server lookUpRemote() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(HOST, PORT);
        return  (Server) registry.lookup(RMI_ID);
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
            System.exit(-1);
        }
    }

    @Override
    public void notify(Client client) throws RemoteException {
        List<Client> clients = server.getConnectedClients();
        if(clients.size() != onlineUsersCount) {
            chatView.updateOnlineUsers(clients);
            onlineUsersCount = clients.size();
        }
        chatView.postMessage(client.getIcon(), client.getColor(), client.getUserName(), client.getText());
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

    @Override
    public Icon getIcon() throws RemoteException {
        return icon;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String name = "anonymous";
        String result = JOptionPane.showInputDialog(
                null,
                "enter your nickname",
                name);

        if(!result.equals("")) name = result;


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

        new ChatClient(name, chosenIcon);
    }
}
