package assignment_2.interfaces;

import assignment_2.interfaces.Client;

import java.rmi.*;

/**
 * Server interface
 * Created by martin on 14/04/2015.
 */
public interface Server extends Remote {
    void register(final Client client) throws RemoteException;
    void unregister(final Client client) throws RemoteException;
    void send(final String message) throws RemoteException;
}
