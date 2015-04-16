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
    public synchronized void connect(Client client) throws RemoteException {
        connectedClients.add(client);
        updateConnectedClientLists(connectedClients);
        for(Line l : lastTwentyLines)
            client.postMessage(l.icon, l.color, l.name, l.text);
        sendToAllClients(client);
        out.print(String.format("%s is connected. Connected connectedClients:%d%n", client.getUserName(), connectedClients.size()));
    }

    @Override
    public synchronized void disconnect(Client client) throws RemoteException {
        connectedClients.remove(client);
        updateConnectedClientLists(connectedClients);
        sendToAllClients(client);
        out.print(String.format("%s is disconnected. Connected connectedClients:%d%n", client.getUserName(), connectedClients.size()));
    }

    @Override
    public synchronized void send(Client client) throws RemoteException {
        Line line = new Line(client.getIcon(), client.getColor(), client.getUserName(), client.getText());
        lastTwentyLines.add(line);
        if(lastTwentyLines.size() > 20)
            lastTwentyLines = new ArrayList<>(lastTwentyLines.subList(1, 21));
        sendToAllClients(client);
    }

    private void updateConnectedClientLists(List<Client> connectedClients) throws RemoteException{
        for(Client c: connectedClients) c.updateConnectedClientList(connectedClients);
    }

    private void sendToAllClients(Client client) throws RemoteException{
        for(Client c: connectedClients)
            c.postMessage(client.getIcon(), client.getColor(), client.getUserName(), client.getText());
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String rmi_id = args[2];
        new ChatServer(hostname, port, rmi_id);
    }

    class Line {
        final Icon icon;
        final Color color;
        final String name;
        final String text;

        public Line(Icon icon, Color color, String name, String text) {
            this.icon = icon;
            this.color = color;
            this.name = name;
            this.text = text;
        }
    }
}
