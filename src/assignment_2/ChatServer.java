package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
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

    private final List<Client> connectedClients;
    private List<Line> lastTwentyLines;
    private static Icon serverIcon = Icons.createIcon("server.jpg");

    private ChatServer(String hostname, int port, String rmi_id) throws RemoteException, AlreadyBoundException, MalformedURLException {
        connectedClients = new ArrayList<>();
        lastTwentyLines = new ArrayList<>(20);
        bindRegistry(hostname, port, rmi_id);
        log(String.format("Listening at %s:%s%n%n", hostname, String.valueOf(port)));
    }

    private void bindRegistry(String hostname, int port, String rmi_id) throws RemoteException, AlreadyBoundException, MalformedURLException {
        Registry registry = LocateRegistry.createRegistry(port);
        Naming.rebind(makeRmiUrlString(hostname, port, rmi_id), this);
    }

    private void log(String logMesage) {
        out.print(logMesage);
    }

    @Override
    public synchronized void connect(Client c) throws RemoteException {
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        connectedClients.add(c);
        updateConnectedClientLists(connectedClients);
        for(Line line : lastTwentyLines)
            c.postMessage(line);

        c.postMessage(new Line(
                serverIcon,
                Color.darkGray,
                "SERVER",
                String.format("%s, welcome to our chat room!", c.getUserName())));

        log(String.format("%s is connected. Connected users:%d%n", c.getUserName(), connectedClients.size()));
    }

    @Override
    public synchronized void disconnect(Client c) throws RemoteException {
        connectedClients.remove(c);
        updateConnectedClientLists(connectedClients);
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        log(String.format("%s is disconnected. Connected connectedClients:%d%n", c.getUserName(), connectedClients.size()));
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

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String rmi_id = args[2];
        new ChatServer(hostname, port, rmi_id);
    }
}
