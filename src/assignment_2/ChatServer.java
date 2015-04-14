package assignment_2;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

import static java.lang.System.out;

/**
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server {

    private final List<Client> clients;

    ChatServer() throws RemoteException, AlreadyBoundException {
        clients = new ArrayList<>();
        Registry registry = LocateRegistry.createRegistry(Constants.PORT);
        registry.bind(Constants.RMI_ID, this);
        System.out.println("chat server is listening");
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
    public void send(String message) throws RemoteException {
        notifyAllClients(message);
    }

    private void notifyAllClients(final String message) throws RemoteException{
        if(clients.size() > 0)
            for(Client c : clients)
                c.notify(message);
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        new ChatServer();
    }
}
