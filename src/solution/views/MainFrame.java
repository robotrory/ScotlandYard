package solution.views;

import solution.Constants;
import solution.DataUpdateListener;

import javax.swing.*;

/**
 * Created by rory on 09/03/15.
 */
public class MainFrame extends JFrame implements DataUpdateListener {

    private final PlayerCountLayout playerCountLayout;
    //    private final PlayerManagementLayout mManagementFrame;
    MainFrameListener mainFrameListener;
    private GameLayout mGameLayout;

    public void showGameUI() {
        remove(playerCountLayout);
        add(mGameLayout);
        pack();
    }

    public GameLayout getGameLayout(){
        return mGameLayout;
    }


    public interface MainFrameListener {
        public void onPlayersAdded(final int count);
    }

    public MainFrame () {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mGameLayout = new GameLayout();

        playerCountLayout = new PlayerCountLayout(Constants.MIN_PLAYERS, Constants.MAX_PLAYERS);
        playerCountLayout.setListener(new PlayerCountLayout.PlayerCountListener() {
            @Override
            public void onPlayerCountDecided(int count) {
                mainFrameListener.onPlayersAdded(count);
            }
        });
        getContentPane().add(playerCountLayout);

        pack();

        setVisible(true);
    }

    public void setMainFrameListener(final MainFrameListener mainFrameListener){
        this.mainFrameListener = mainFrameListener;
    }
}