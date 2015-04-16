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
    void postMessage(Icon icon, Color userColor, String username, String text) throws RemoteException;
    void updateConnectedClientList(List<Client> connectedClients) throws RemoteException;
    String getUserName() throws RemoteException;
    Color getColor() throws RemoteException;
    String getText() throws RemoteException;
    Icon getIcon() throws RemoteException;
}
