package assignment_2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

/**
 * Created by martin on 14/04/2015.
 */
class ServerImplementation extends UnicastRemoteObject implements Server {

    private final List<Client> clients;

    ServerImplementation() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void register(final Client client) throws RemoteException {
        String msg = String.format("%s is connected%n", client.getName());
        notifyAllClients(msg);
        clients.add(client);
        out.print(msg);
        out.println("Connected clients: " + clients.size());
    }

    @Override
    public synchronized void unregister(final Client client) throws RemoteException {
        clients.remove(client);
        String msg = String.format("%s is disconnected%n", client.getName());
        notifyAllClients(msg);
        out.print(msg);
        out.println("Connected clients: " + clients.size());
    }

    @Override
    public void send(final String message) throws RemoteException {
        notifyAllClients(message);
    }

    private void notifyAllClients(final String message) throws RemoteException{
        if(clients.size() > 0)
            for(Client c : clients)
                c.notify(message);
    }
}
