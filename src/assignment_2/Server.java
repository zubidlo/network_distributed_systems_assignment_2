package assignment_2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by martin on 14/04/2015.
 */
interface Server extends Remote {
    void register(final Client newClient) throws RemoteException;
    void unregister(final Client client) throws RemoteException;
    void send(final String message) throws RemoteException;
}
