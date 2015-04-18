package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.*;
import java.util.List;

import static java.lang.System.out;
import static assignment_2.HelperClasses.Utils.*;

/**
 * Server implementation
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server, Serializable {

    private final static Icon SERVER_ICON = Icons.createIcon("server.jpg");
    private final List<Client> connectedClients;
    private List<Line> lastTwentyLines;

    private ChatServer() throws RemoteException {
        connectedClients = new ArrayList<>();
        lastTwentyLines = new ArrayList<>(20);
    }

    @Override
    public synchronized void connect(Client c) throws RemoteException {
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        connectedClients.add(c);
        updateConnectedClientLists(connectedClients);
        for(Line line : lastTwentyLines)
            c.postMessage(line);

        c.postMessage(new Line(
                SERVER_ICON,
                Color.darkGray,
                "SERVER",
                String.format("%s, welcome to our chat room!", c.getUserName())));

        out.format("%s is connected. Connected users:%d%n", c.getUserName(), connectedClients.size());
    }

    @Override
    public synchronized void disconnect(Client c) throws RemoteException {
        connectedClients.remove(c);
        updateConnectedClientLists(connectedClients);
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        out.format("%s is disconnected. Connected connectedClients:%d%n", c.getUserName(), connectedClients.size());
    }

    @Override
    public synchronized void send(Line line) throws RemoteException {
        lastTwentyLines.add(line);
        if(lastTwentyLines.size() > 20)
            lastTwentyLines = new ArrayList<>(lastTwentyLines.subList(1, 21));
        sendToAllClients(line);
    }

    private void updateConnectedClientLists(List<Client> connectedClients) throws RemoteException{
        for(Client c: connectedClients) c.updateConnectedClientList(connectedClients);
    }

    private void sendToAllClients(Line line) {
        if(connectedClients.size() > 0)
            connectedClients.forEach(c -> {
                try { c.postMessage(line); } catch (RemoteException e) { e.printStackTrace(); }
            });
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException, UnknownHostException {
        int port = Integer.parseInt(args[0]);
        String rmi_id = args[1];

        out.println(LocateRegistry.createRegistry(port).toString());
        System.setProperty("java.rmi.server.hostname", ipAddress());
        Naming.rebind(makeRmiUrlString(ipAddress(), port, rmi_id), new ChatServer());
    }
}
