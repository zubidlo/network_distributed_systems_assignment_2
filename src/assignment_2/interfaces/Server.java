package assignment_2.interfaces;

import assignment_2.Line;

import java.rmi.*;

/**
 * Server interface
 * Created by martin on 14/04/2015.
 */
public interface Server extends Remote {
    void connect(Client client) throws RemoteException;
    void disconnect(Client client) throws RemoteException;
    void send(Line line) throws RemoteException;
}
