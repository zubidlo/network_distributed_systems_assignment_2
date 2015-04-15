package assignment_2;

import assignment_2.interfaces.*;
import static assignment_2.helperClasses.Constants.*;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

import static java.lang.System.out;

/**
 * Server implementation
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server, Serializable {

    private final List<Client> clients;

    ChatServer() throws RemoteException, AlreadyBoundException {
        clients = new ArrayList<>();
        bindRegistry();
        System.out.format("chat server is listening at %s:%s%n%n", HOST, String.valueOf(PORT));
    }

    private void bindRegistry() throws RemoteException, AlreadyBoundException {

        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind(RMI_ID, this);
    }

    @Override
    public synchronized void connect(Client client) throws RemoteException {
        clients.add(client);
        notifyAllClients(client);
        out.print(String.format("%s is connected. Connected clients:%d%n", client.getUserName(), clients.size()));
    }

    @Override
    public synchronized void disconnect(Client client) throws RemoteException {
        clients.remove(client);
        notifyAllClients(client);
        out.print(String.format("%s is disconnected. Connected clients:%d%n", client.getUserName(), clients.size()));
    }

    @Override
    public synchronized void send(Client client) throws RemoteException {
        notifyAllClients(client);
    }

    @Override
    public List<Client> getConnectedClients() throws RemoteException {
        return new ArrayList<Client>(clients);
    }

    private void notifyAllClients(final Client client) throws RemoteException{
        if(clients.size() > 0)
            for(Client c : clients)
                c.notify(client);
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        new ChatServer();
    }
}
