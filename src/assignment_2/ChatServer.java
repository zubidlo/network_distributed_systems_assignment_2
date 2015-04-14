package assignment_2;

import assignment_2.interfaces.*;
import static assignment_2.helperClasses.Constants.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

import static java.lang.System.out;

/**
 * Server implementation
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server {

    private final List<Client> clients;

    ChatServer() throws RemoteException{
        clients = new ArrayList<>();
        bindRegistry();
        System.out.format("chat server is listening at %s:%s%n%n", HOST, String.valueOf(PORT));
    }

    private void bindRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind(RMI_ID, this);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public synchronized void register(Client client) throws RemoteException {
        String msg = String.format("%s is connected%n", client.getName());
        notifyAllClients(msg);
        clients.add(client);
        out.print(msg);
        out.println("Connected clients: " + clients.size());
    }

    @Override
    public synchronized void unregister(Client client) throws RemoteException {
        clients.remove(client);
        String msg = String.format("%s is disconnected%n", client.getName());
        notifyAllClients(msg);
        out.print(msg);
        out.println("Connected clients: " + clients.size());
    }

    @Override
    public synchronized void send(String message) throws RemoteException {
        notifyAllClients(message);
    }

    private void notifyAllClients(final String message) throws RemoteException{
        if(clients.size() > 0)
            for(Client c : clients)
                c.notify(message);
    }

    public static void main(String[] args) throws RemoteException {
        new ChatServer();
    }
}
