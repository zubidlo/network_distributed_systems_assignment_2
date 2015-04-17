package assignment_2.interfaces;

import assignment_2.Line;

import javax.swing.*;
import java.awt.*;
import java.rmi.*;
import java.util.List;

/**
 * Client interface
 * Created by martin on 14/04/2015.
 */
public interface Client extends Remote {
    void postMessage(Line line) throws RemoteException;
    void updateConnectedClientList(List<Client> connectedClients) throws RemoteException;
    String getUserName() throws RemoteException;
    Color getColor() throws RemoteException;
    String getText() throws RemoteException;
    Icon getIcon() throws RemoteException;
}
