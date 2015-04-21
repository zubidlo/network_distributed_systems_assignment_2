package assignment_2;

import assignment_2.HelperClasses.*;
import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
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

    private ChatClient(final Server server, final String name, final Icon i)
            throws RemoteException {
        this.server = server;
        username = name;
        userColor = Utils.randColor();
        icon = i;
        chatView = new ChatViewClassic("Chat " + username, this::sendText, this::disconnectAndExit);
        server.connect(this);
    }

    private void disconnectAndExit() {
        try {
            server.disconnect(this);
            chatView.dispose();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void sendText() {
        try {
            server.send(new ChatLine(icon, userColor, username, chatView.getMessageField().getText()));
            chatView.getMessageField().setText("");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void print(final Line chatLine) throws RemoteException {
        chatView.print(chatLine);
    }

    @Override
    public void print(final List<Client> connectedClients) throws RemoteException {
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

    public static void main(String[] args)
            throws RemoteException, NotBoundException, MalformedURLException {

        if(args.length != 3) {
            System.out.println("Usage: java ChatClient hostname port rmi_id");
        }

        Utils.setUIManagerDefaults();

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
        Utils.printRegistryList(Utils.makeRmiUrlString(hostname, port, rmi_id));
        Server server = (Server) Naming.lookup(Utils.makeRmiUrlString(hostname, port, rmi_id));
        new ChatClient(server, username, chosenIcon);
    }
}
