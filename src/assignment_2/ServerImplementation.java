package assignment_2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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
        nofifyAllClients(String.format("%s is connected%n", client.getName()));
        clients.add(client);
    }

    @Override
    public synchronized void unregister(final Client client) throws RemoteException {
        client.notify(String.format("Bye bye %s%n", client.getName()));
        clients.remove(client);
        nofifyAllClients(String.format("%s is disconnected%n", client.getName()));
    }

    @Override
    public void send(final String message) throws RemoteException {
        nofifyAllClients(message);
    }

    private void nofifyAllClients(final String message) throws RemoteException{
        if(clients.size() > 0)
            for(Client c : clients)
                c.notify(message);
    }
}
