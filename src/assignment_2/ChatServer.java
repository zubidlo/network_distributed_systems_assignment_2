package assignment_2;

import assignment_2.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.util.List;

import static java.lang.System.out;

/**
 * Server implementation
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server, Serializable {

    private final List<Client> connectedClients;
    private List<Line> lastTwentyLines;

    private ChatServer(String hostname, int port, String rmi_id) throws RemoteException, AlreadyBoundException {
        connectedClients = new ArrayList<>();
        lastTwentyLines = new ArrayList<>(20);
        bindRegistry(port, rmi_id);
        System.out.format("chat server is listening at %s:%s%n%n", hostname, String.valueOf(port));
    }

    private void bindRegistry(int port, String rmi_id) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.bind(rmi_id, this);
    }

    @Override
    public synchronized void connect(Client c) throws RemoteException {
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        connectedClients.add(c);
        updateConnectedClientLists(connectedClients);
        for(Line line : lastTwentyLines)
            c.postMessage(line);
        c.postMessage(new Line(Icons.getByFilename("server.png"), Color.black, "SERVER", "Welcome to our chat room!"));
        out.print(String.format("%s is connected. Connected connectedClients:%d%n", c.getUserName(), connectedClients.size()));
    }

    @Override
    public synchronized void disconnect(Client c) throws RemoteException {
        connectedClients.remove(c);
        updateConnectedClientLists(connectedClients);
        sendToAllClients(new Line(c.getIcon(), c.getColor(), c.getUserName(), c.getText()));
        out.print(String.format("%s is disconnected. Connected connectedClients:%d%n", c.getUserName(), connectedClients.size()));
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

    private void sendToAllClients(Line line) throws RemoteException{
        for(Client c: connectedClients)
            c.postMessage(line);
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String rmi_id = args[2];
        new ChatServer(hostname, port, rmi_id);
    }
}
