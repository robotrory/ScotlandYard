package solution.interfaces;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.MoveTicket;
import scotlandyard.Ticket;

import java.util.List;
import java.util.Map;

/**
 * Created by rory on 11/03/15.
 */
public interface GameControllerInterface {
    public void addUpdateListener(GameUIInterface listener);

    public Colour getCurrentPlayer();
    public Map<Ticket,Integer> getPlayerTickets(Colour currentPlayer);
    public List<Colour> getPlayerList();
    List<MoveTicket> getValidSingleMovesAtLocation(Colour currentPlayer, int location);

    public void notifyAllPlayersAdded(final int count);
    public void notifyMoveSelected(final Move move);
    public List<MoveTicket> getMrXHistory();

}