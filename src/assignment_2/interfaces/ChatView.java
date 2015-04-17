package assignment_2.interfaces;

import assignment_2.Line;

import java.util.List;

/**
 * chat view interface
 * Created by martin on 16/04/2015.
 */
public interface ChatView {
    void postMessage(Line line);
    void updateConnectedUsers(List<Client> onlineClients);
}
