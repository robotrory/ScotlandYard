package solution.views;

import solution.Constants;
import solution.interfaces.GameControllerInterface;
import solution.interfaces.adapters.GameUIAdapter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by benallen on 18/03/15.
 */
public class ScreenView extends JPanel {
    private final GameControllerInterface mControllerInterface;

    private GridBagConstraints mGridLayout = null;

    class GameAdapter extends GameUIAdapter {
        @Override
        public void showGameInterface() {
            callScreen(2);
        }
    }
    public ScreenView(final GameControllerInterface controllerInterface) {
        setOpaque(false);
        mControllerInterface = controllerInterface;
        controllerInterface.addUpdateListener(new GameAdapter());
        callScreen(0);

    }
    public void callScreen(final int screenToDisplay){
        manageScreens(4);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                manageScreens(screenToDisplay);
            }
        });
    }
    private void manageScreens(int screenToDisplay){
        clearScreen();
        setupScreen();
        switch (screenToDisplay){  // 0 = intro, 1 = add players, 2 = gameplay, 3 = gameover
            case 0:
                showIntroView();
                break;
            case 1:
                showPlayerAddView();
                break;
            case 2:
                showGameView();
                break;
            case 3:
                showGameOverView();
                break;
            case 4:
                showLoadingView();
                break;
        }
        endSetupScreen();
    }

    private void showGameView() {
        System.out.println("Showing Game View");
        GameLayout gameLayout = new GameLayout(mControllerInterface, new PlayerInfoBar.PlayerInfoBarListener(){
            @Override
            public void onMenuBtnPress() {
                callScreen(0);
            }
            @Override
            public void onSaveBtnPress() {
                showSaveOptions();
            }
        });
        add(gameLayout, mGridLayout);
    }

    public void clearScreen(){
        revalidate();
        removeAll();
    }
    public void showIntroView(){
        System.out.println("Showing Intro Screen");
        IntroView introView = new IntroView();
        introView.setListener(new IntroView.IntroViewListener(){
            @Override
            public void onPlayBtnPress() {
                callScreen(1);
            }
            @Override
            public void onLoadBtnPress() { showLoadOptions(); }
        });
        add(introView, mGridLayout);

    }
    public void showLoadingView(){
        System.out.println("Showing Loading View");
        LoadingView loadingView = new LoadingView();
        add(loadingView, mGridLayout);
    }
    public void showPlayerAddView(){
        System.out.println("Showing Add Player View");
        // Set up player add view
        PlayerAddView playerAddView = new PlayerAddView(Constants.MIN_PLAYERS, Constants.MAX_PLAYERS);

        playerAddView.setListener(new PlayerAddView.PlayerCountListener() {
            @Override
            public void onPlayerCountDecided(int count) {
                mControllerInterface.notifyAllPlayersAdded(count);
            }
        });
        add(playerAddView, mGridLayout);
    }
    public void showGameOverView(){
        // Set up game over view
        GameOverView endOfGame = new GameOverView(mControllerInterface);
        endOfGame.setPreferredSize(new Dimension(1000, 800));
        endOfGame.setOpaque(false);
        endOfGame.setVisible(false);
        add(endOfGame, mGridLayout);
    }
    private void setupScreen(){

        // Set layout
        setLayout(new GridBagLayout());
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        setPreferredSize(new Dimension(width, height));

        // Form the layout
        GridBagConstraints gbc = new GridBagConstraints();

        // Setup the grid
        gbc.gridy = gbc.gridx = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = gbc.weightx = 100;
        mGridLayout = gbc;
    }
    private void endSetupScreen(){
        setVisible(false);
        repaint();
        setVisible(true);

    }
    private void showSaveOptions(){
        final JFileChooser fc = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fc.setCurrentDirectory(workingDirectory);
        int returnVal = fc.showSaveDialog(getParent());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Saving: " + file.getName() + ".");
            //mControllerInterface.saveGame(file);
        } else {
            System.out.println("Save command cancelled by user.");
        }
    }
    private void showLoadOptions(){
        final JFileChooser fc = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fc.setCurrentDirectory(workingDirectory);

        int returnVal = fc.showOpenDialog(getParent());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + ".");


            //Custom button text
            Object[] options = {"Yes, please",
                    "Nope"};
            int response = JOptionPane.showOptionDialog(getParent(),
                    "Would you like to replay the saved game?",
                    "Replay",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            callScreen(2);
            mControllerInterface.loadGame(file, response == JOptionPane.YES_OPTION);

        } else {
            System.out.println("Open command cancelled by user.");
        }
    }
}
