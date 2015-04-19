package assignment_2.interfaces;

import java.util.List;

/**
 * chat view interface
 * Created by martin on 16/04/2015.
 */
public interface ChatView {
    void print(Line chatLine);
    void print(List<Client> onlineClients);
}
