package assignment_2;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.*;

/**
 * Created by martin on 14/04/2015.
 */
public class StartChatClient extends FrameSkeleton {

    private final JTextArea chatTextArea;
    private final JTextField messageField;
    private final Server server;
    private final Client client;
    private static final Color COLOR = new Color(255, 255, 170);
    private static final Border BORDER = new EmptyBorder(0, 0, 0, 0);

    StartChatClient(final String name) throws RemoteException, NotBoundException {
        super("ChatRoom");
        Registry registry = LocateRegistry.getRegistry(Constants.HOST, Constants.PORT);
        chatTextArea = new JTextArea(10, 50);
        messageField = new JTextField(40);
        server = (Server) registry.lookup(Constants.RMI_ID);
        client = new ClientImplementation(name, chatTextArea);
        buildContentPane();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { unregisterAndExit(); }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        server.register(client);
    }

    private void unregisterAndExit() {
        try { server.unregister(client); }
        catch (RemoteException e1) { e1.printStackTrace(); }
        System.exit(0);
    }

    private void buildContentPane() throws RemoteException {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getNorthPanel(), BorderLayout.NORTH);
        getContentPane().add(getCenterPanel(), BorderLayout.CENTER);
        getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel getNorthPanel() throws RemoteException {
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.white);
        JLabel nameLabel = new JLabel("Welcome " + client.getName());
        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(COLOR);
        disconnectButton.addActionListener(e -> unregisterAndExit());
        northPanel.add(nameLabel);
        northPanel.add(disconnectButton);
        return northPanel;
    }

    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.white);
        chatTextArea.setEditable(false);
        chatTextArea.setBorder(BORDER);
        chatTextArea.setBackground(COLOR);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        scrollPane.setBorder(BORDER);
        centerPanel.add(scrollPane);
        return centerPanel;
    }

    private JPanel getSouthPanel(){
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.white);
        JLabel messageLabel = new JLabel("Message:");
        messageField.setBorder(BORDER);
        messageField.setBackground(COLOR);
        messageField.addKeyListener(new KeyAdapter() {
                                        public void keyPressed(KeyEvent e) {
                                            if(e.getKeyCode() == KeyEvent.VK_ENTER)
                                                sendMessage(messageField.getText());
                                        }
                                    });
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage(messageField.getText()));
        sendButton.setBackground(COLOR);
        southPanel.add(messageLabel);
        southPanel.add(messageField);
        southPanel.add(sendButton);
        return southPanel;
    }

    private void sendMessage(String message) {
        try {
            String msg = String.format("[%s]> %s%n", client.getName(), message);
            server.send(msg);
            messageField.setText("");
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String name = "anonymous";
        String result = JOptionPane.showInputDialog(null, "enter your name");
        if(!result.equals("")) name = result;
        new StartChatClient(name);
    }
}
