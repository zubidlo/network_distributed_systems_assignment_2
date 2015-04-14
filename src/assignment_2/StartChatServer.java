package assignment_2;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by martin on 14/04/2015.
 */
class StartChatServer {

    Server server;

    StartChatServer() throws RemoteException, AlreadyBoundException {
        server = new ServerImplementation();
        Registry registry = LocateRegistry.createRegistry(Constants.PORT);
        registry.bind(Constants.RMI_ID, server);
        System.out.println("chat server is listening");
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        new StartChatServer();
    }
}
