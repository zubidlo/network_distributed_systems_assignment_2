package assignment_2.interfaces;

import java.rmi.*;
import java.util.List;

/**
 * Server interface
 * Created by martin on 14/04/2015.
 */
public interface Server extends Remote {
    void connect(final Client client) throws RemoteException;
    void disconnect(final Client client) throws RemoteException;
    void send(final Client client) throws RemoteException;
    List<Client> getConnectedClients() throws RemoteException;
}
