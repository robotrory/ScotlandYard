package solution.interfaces;

import solution.Models.ScotlandYardModel;

/**
 * Created by rory on 11/03/15.
 */
public interface GameUIInterface {
    public void showGameInterface();
    public void onGameModelUpdated(final ScotlandYardModel model);
}
