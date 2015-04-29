package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.rmi.*;
import java.util.List;

/**
 * Client interface
 * Created by martin on 14/04/2015.
 */
interface Client extends Remote {
    void print(ChatLine chatLine) throws RemoteException;
    void print(List<Client> connectedClients) throws RemoteException;
    Icon getIcon() throws RemoteException;
    Color getColor() throws RemoteException;
    String getUserName() throws RemoteException;
}
