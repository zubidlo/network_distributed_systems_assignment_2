package assignment_2.interfaces;

import java.rmi.*;

/**
 * Client interface
 * Created by martin on 14/04/2015.
 */
public interface Client extends Remote {
    void notify(final String message) throws RemoteException;
    String getName() throws RemoteException;
}
