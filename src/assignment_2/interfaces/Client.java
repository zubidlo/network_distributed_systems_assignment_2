package assignment_2.interfaces;

import javax.swing.*;
import java.awt.*;
import java.rmi.*;

/**
 * Client interface
 * Created by martin on 14/04/2015.
 */
public interface Client extends Remote {
    void notify(final Client client) throws RemoteException;
    String getUserName() throws RemoteException;
    Color getColor() throws RemoteException;
    String getText() throws RemoteException;
    Icon getIcon() throws RemoteException;
}
