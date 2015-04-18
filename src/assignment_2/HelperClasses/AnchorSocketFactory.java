package assignment_2.HelperClasses;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * Created by martin on 18/04/2015.
 */
public class AnchorSocketFactory extends RMISocketFactory {

    private final InetAddress address;

    public AnchorSocketFactory(InetAddress address) {
        this.address = address;
    }
    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 100, address);
        } catch (Exception e) { e.printStackTrace(); }
        return serverSocket;
    }
}
