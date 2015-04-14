package assignment_2;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by martin on 14/04/2015.
 */
interface Client extends Remote {
    void notify(final String newMessage) throws RemoteException;
    String getName() throws RemoteException;
}
