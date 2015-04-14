package assignment_2;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by martin on 14/04/2015.
 */
class ClientImplementation extends UnicastRemoteObject implements Client {

    final String username;
    final JTextArea textArea;

    ClientImplementation(String name, JTextArea area) throws RemoteException {
        super();
        username = name;
        textArea = area;
    }

    @Override
    public void notify(String message) throws RemoteException {
        textArea.append(message);
        textArea.revalidate();
    }

    @Override
    public String getName() {
        return username;
    }
}
