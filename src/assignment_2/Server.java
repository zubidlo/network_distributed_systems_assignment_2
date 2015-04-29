package assignment_2;

import assignment_2.Client;

import java.rmi.*;

/**
 * Server interface
 * Created by martin on 14/04/2015.
 */
interface Server extends Remote {
    void connect(Client client) throws RemoteException;
    void disconnect(Client client) throws RemoteException;
    void send(ChatLine chatLine) throws RemoteException;
}
