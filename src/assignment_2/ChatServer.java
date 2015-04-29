package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.System.out;

/**
 * Server implementation
 * Created by martin on 14/04/2015.
 */
class ChatServer extends UnicastRemoteObject implements Server, Serializable {

    private final static Icon SERVER_ICON = Icons.createIcon("server.jpg");
    private final List<Client> connectedClients;
    private final List<ChatLine> lastTwentyChatLines;

    private ChatServer() throws RemoteException {
        connectedClients = new CopyOnWriteArrayList<>();
        lastTwentyChatLines = new CopyOnWriteArrayList<>();
    }

    @Override
    public void connect(Client c) throws RemoteException {
        sendToAllClients(new ChatLine(
                        c.getIcon(),
                        c.getColor(),
                        c.getUserName(),
                        "...connected...")
        );

        connectedClients.add(c);
        updateConnectedClientLists(connectedClients);
        for(ChatLine chatLine : lastTwentyChatLines)
            c.print(chatLine);

        c.print(new ChatLine(
                SERVER_ICON,
                Color.darkGray,
                "SERVER",
                String.format("%s, welcome to our chat room!", c.getUserName())));

        out.format("%s is connected. Connected users:%d%n",
                c.getUserName(),
                connectedClients.size());
    }

    @Override
    public void disconnect(Client c) throws RemoteException {
        connectedClients.remove(c);
        updateConnectedClientLists(connectedClients);
        sendToAllClients(new ChatLine(
                        c.getIcon(),
                        c.getColor(),
                        c.getUserName(),
                        "...disconnected...")
        );

        out.format("%s is disconnected. Connected connectedClients:%d%n",
                c.getUserName(),
                connectedClients.size());
    }

    @Override
    public void send(ChatLine chatLine) throws RemoteException {
        lastTwentyChatLines.add(chatLine);
        if(lastTwentyChatLines.size() > 20)
            lastTwentyChatLines.retainAll(lastTwentyChatLines.subList(1, 21));
        sendToAllClients(chatLine);
    }

    private void updateConnectedClientLists(List<Client> connectedClients) throws RemoteException{
        for(Client c: connectedClients) c.print(connectedClients);
    }

    private void sendToAllClients(ChatLine chatLine) {
        if(connectedClients.size() > 0)
            connectedClients.forEach(c -> {
                try {
                    c.print(chatLine);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
    }

    public static void main(String[] args)
            throws RemoteException,
            AlreadyBoundException,
            MalformedURLException,
            UnknownHostException {
        if(args.length != 1) {
            out.println("Usage: java -Djava.security.manager -Djava.security.policy=chat.policy assignment_2.ChatServer port");
            System.exit(-1);
        }
        int port = Integer.parseInt(args[0]);

        String rmiString = Utils.makeRmiUrlString(Utils.ipAddress(), port, ChatServer.class.getSimpleName());
        out.printf("%nrun client with 'java -Djava.security.manager -Djava.security.policy=chat.policy assignment_2.ChatClient %s'%n", rmiString);
        LocateRegistry.createRegistry(port);
        System.setProperty("java.rmi.server.hostname", Utils.ipAddress());
        Naming.rebind(rmiString, new ChatServer());
    }
}
