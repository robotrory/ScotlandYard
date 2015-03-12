package solution.views;

import scotlandyard.Colour;
import solution.ScotlandYardModel;
import solution.interfaces.GameControllerInterface;
import solution.interfaces.GameUIInterface;
import solution.interfaces.adapters.GameUIAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by benallen on 10/03/15.
 */
public class PlayerInfoBar extends JPanel {
    private GameControllerInterface mGameControllerInterface;
    public PlayerInfoBar(GameControllerInterface controllerInterface) {
        mGameControllerInterface = controllerInterface;
        controllerInterface.addUpdateListener(new GameAdapter());
        createBar();

    }

    private void createBar() {
        GameControllerInterface controllerInterface = mGameControllerInterface;
        List<Colour> allPlayers = controllerInterface.getPlayerList();
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(800, 300));
        setPreferredSize(new Dimension(800, 300));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 20;
        gbc.weighty = 100;

        PlayerInfoColumn[] playerColumns = new PlayerInfoColumn[allPlayers.size()];

        for(int i = 0; i < allPlayers.size(); i++){

            Colour currentPlayer = allPlayers.get(i);
            playerColumns[i] = new PlayerInfoColumn(currentPlayer, controllerInterface);
            playerColumns[i].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
            gbc.gridx = i;
            add(playerColumns[i],gbc);
        }
    }

    class GameAdapter extends GameUIAdapter {
        @Override
        public void onGameModelUpdated(ScotlandYardModel model) {
            removeAll();
            createBar();

        }
    }

}
