package assignment_2.interfaces;

import javax.swing.*;
import java.awt.*;
import java.rmi.*;
import java.util.List;

/**
 * Client interface
 * Created by martin on 14/04/2015.
 */
public interface Client extends Remote {
    void print(final Line chatLine) throws RemoteException;
    void print(final List<Client> connectedClients) throws RemoteException;
    Icon getIcon() throws RemoteException;
    Color getColor() throws RemoteException;
    String getUserName() throws RemoteException;
}
