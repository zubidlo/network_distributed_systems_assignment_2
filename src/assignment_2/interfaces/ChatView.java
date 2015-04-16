package assignment_2.interfaces;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * chat view interface
 * Created by martin on 16/04/2015.
 */
public interface ChatView {
    void postMessage(Icon icon, Color userColor, String username, String text);
    void updateConnectedUsers(List<Client> onlineClients);
}
