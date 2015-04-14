package assignment_2;

import assignment_2.helperClasses.*;
import assignment_2.interfaces.*;
import static assignment_2.helperClasses.Constants.*;

import javax.swing.*;
import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
 * Client implementation
 * Created by martin on 14/04/2015.
 */
class ChatClient extends UnicastRemoteObject implements Client {

    private final FrameSkeleton frame;
    private final String username;
    private Server server;

    ChatClient(final String name) throws RemoteException{
        frame = new FrameSkeleton("ChatRoom");
        username = name;
        frame.nameLabel.setText("Welcome " + username);
        registerRemote();
        addListeners();
    }

    private void registerRemote() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            server = (Server) registry.lookup(RMI_ID);
            server.register(this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void unregisterAndExit() {
        try {
            server.unregister(this);
        } catch (RemoteException e1) { e1.printStackTrace(); }
        System.exit(0);
    }

    private void sendMessage(String message) {
        try {
            String msg = String.format("[%s]> %s%n", getName(), message);
            server.send(msg);
            frame.messageField.setText("");
        } catch (RemoteException e) { e.printStackTrace(); }
    }

    private void addListeners() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { unregisterAndExit(); }
        });
        frame.messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    sendMessage(frame.messageField.getText());
            }
        });
        frame.sendButton.addActionListener(e -> sendMessage(frame.messageField.getText()));
        frame.disconnectButton.addActionListener(e -> unregisterAndExit());
    }

    @Override
    public synchronized void notify(String message) throws RemoteException {
        frame.chatTextArea.append(message);
        frame.chatTextArea.revalidate();
    }

    @Override
    public synchronized String getName() throws RemoteException {
        return username;
    }

    public static void main(String[] args) throws RemoteException{
        String name = "anonymous";
        String result = JOptionPane.showInputDialog(null, "enter your name");
        if(!result.equals("")) name = result;
        new ChatClient(name);
    }
}
