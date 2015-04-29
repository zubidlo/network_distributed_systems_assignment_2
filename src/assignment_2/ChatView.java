package assignment_2;

import java.util.List;

/**
 * chat view interface
 * Created by martin on 16/04/2015.
 */
interface ChatView {
    void print(ChatLine chatLine);
    void print(List<Client> onlineClients);
}
