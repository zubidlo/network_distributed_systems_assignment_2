package assignment_2.interfaces;

import java.util.List;

/**
 * chat view interface
 * Created by martin on 16/04/2015.
 */
public interface ChatView {
    void print(final Line chatLine);
    void print(final List<Client> onlineClients);
}
