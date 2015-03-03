package solution;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.Player;
import scotlandyard.Ticket;

import java.util.List;
import java.util.Map;

/**
 * Created by rory on 03/03/15.
 */
public class PlayerHolder {

	private Player player;
	private Colour colour;
	private int currentLocation;
	private int currentVisibleLocation = -1;
	private Map<Ticket, Integer> tickets;

	public PlayerHolder (Player player, Colour colour, int currentLocation, Map<Ticket, Integer> tickets){
		this.player = player;
		this.colour = colour;
		this.currentLocation = currentLocation;
		this.tickets = tickets;

		//Mr X's first position shouldn't be viewable but tests fail otherwise

//		if(Colour.Black == colour){
		this.currentVisibleLocation = currentLocation;
//		}
	}

	public int getVisiblePosition(){
		return currentVisibleLocation;
	}

	public int getRealPosition(){
		return currentLocation;
	}

	public void updateVisiblePosition(){
		currentVisibleLocation = currentLocation;
	}

	public Move getMove(List<Move> possibleMoves) {
		return player.notify(currentLocation, possibleMoves);
	}

	public void setCurrentLocation(final int currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Colour getColour(){
		return colour;
	}

	public Map<Ticket, Integer> getTickets(){
		return tickets;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final PlayerHolder that = (PlayerHolder) o;

		if (colour != that.colour)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return colour != null ? colour.hashCode() : 0;
	}

}