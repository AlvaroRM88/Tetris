/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import model.Board;
import sound.SoundPlayer;

/**
 * The graphical user interface for the Tetris.
 * 
 * @author Jieun Lee
 * @version 1.2 (03-11-2016)
 */
public class TetrisGUI extends JFrame implements Observer {
    
    /**
     * The Initial pixel.
     */
    public static final int INITIAL_PX = 36;
    
    /**
     * The background color.
     */
    public static final Color BACK_COLOR = new Color(0, 0, 102);
    
    /**
     * The title font color.
     */
    public static final Color TITLE_FONT_COLOR = Color.YELLOW;

    /**
     * The Default title font size.
     */
    public static final int TITLE_FONT_SIZE = 16;

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = 6440148499445464731L;
    
    /**
     * A ToolKit.
     */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();

    /**
     * The Dimension of the screen.
     */
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();
    
    /**
     * The dimension of the minimum size of the frame.
     */
    private static final Dimension MINIMUM_SIZE = new Dimension(490, 680);
    
    /**
     * The initial delay time.
     */
    private static final int INITIAL_DELAY = 1000;
    
    /**
     * The background music during game.
     */
    private static final String SOUND_BGM = "sound/game.wav";
    
    /**
     * The sound effect when Tetris piece moves.
     */
    private static final String SOUND_MOVE = "sound/move.wav";

    /**
     * The sound effect when Tetris piece drops.
     */
    private static final String SOUND_DROP = "sound/drop.wav";
    
    /**
     * The sound effect when Tetris piece rotates.
     */
    private static final String SOUND_ROTATION = "sound/rotation.wav";

    /**
     * The Board.
     */
    private final Board myBoard;
    
    /**
     * The SoundPlayer.
     */
    private final SoundPlayer myPlayer;

    /**
     * The menu bar.
     */
    private final TetrisMenuBar myMenuBar;

    /**
     * The status panel.
     */
    private final StatusPanel myStatusPanel;

    /**
     * The game board panel.
     */
    private final BoardPanel myBoardPanel;

    /**
     * The Timer.
     */
    private final Timer myTimer;
    
    /**
     * The current Timer delay.
     */
    private int myCurrentDelay;

    /**
     * The state of the game is in progress.
     */
    private boolean myIsProgress;

    /**
     * The state of the game is over.
     */
    private boolean myIsGameOver;
    
    /**
     * The state of the game is paused.
     */
    private boolean myIsPause;
    
    /**
     * The state of the sound mode.
     */
    private boolean mySoundMode;
    
    /**
     * The image Icon.
     */
    private ImageIcon myIcon;

    /**
     * Constructs TetrisGUI.
     */
    public TetrisGUI() {
        super("Tetris");
        myBoard = new Board();
        myPlayer = new SoundPlayer();
        myBoardPanel = new BoardPanel(myBoard);
        myStatusPanel = new StatusPanel(this, myBoard, myPlayer);
        myMenuBar = new TetrisMenuBar(this, myBoard, myBoardPanel, myStatusPanel);
        myTimer = new Timer(INITIAL_DELAY, new TimerListener());
        mySoundMode = true;
        myCurrentDelay = INITIAL_DELAY;
       
        setup();
    }
    
    /**
     * Returns truth value of sound mode.
     * 
     * @return The truth value of sound mode.
     */
    public boolean isSoundMode() {
        return mySoundMode;
    }
    
    /**
     * Returns image icon.
     * 
     * @return The Image Icon.
     */
    public ImageIcon getIcon() {
        return myIcon;
    }
    
    /**
     * Sets Timer delay and Timer.
     * 
     * @param theDelay The amount of delay that will be reduced.
     */
    public void setTimerDelay(final int theDelay) {
        myCurrentDelay = INITIAL_DELAY - theDelay;
        myTimer.setDelay(myCurrentDelay);
    }
    
    /**
     * Sets sound mode with given truth value.
     * 
     * @param theValue The truth Value.
     */
    public void setSoundMode(final boolean theValue) {
        mySoundMode = theValue;
        if (!myIsPause) {
            if (mySoundMode) {
                myPlayer.loop(SOUND_BGM);
            } else {
                myPlayer.stop(SOUND_BGM);
            }
        }
    }

    /**
     * Starts the game.
     */
    public void start() {
        if (!myIsPause) {
            myStatusPanel.clear();
            myBoard.newGame();
            myTimer.setDelay(myCurrentDelay);
            myTimer.start();
            myIsProgress = true;
            myIsGameOver = false;
            myIsPause = false;
            myMenuBar.setEnableStartButton(!myIsProgress);
            myMenuBar.setButtonEnable(!myIsProgress);
            if (mySoundMode) {
                myPlayer.loop(SOUND_BGM);
            }
        }
    }

    /**
     * Stops the game and clears the game boards.
     */
    public void stop() {
        myTimer.stop();
        myPlayer.stopAll();
        myIsGameOver = true;
        myIsProgress = false;
        myIsPause = false;
        myMenuBar.setEnableStartButton(!myIsProgress);
        myMenuBar.setButtonEnable(!myIsProgress);
        myMenuBar.resetSpeedSlider();
        showGameOver();
    }

    /**
     * Disposes this frame.
     */
    public void exit() {
        dispose();
    }

    /**
     * This method is called whenever the state of game over is changed.
     * 
     * @param theObservable Board.
     * @param theArg The boolean type of argument passed to the notifyObservers
     *            method.
     */
    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theObservable instanceof Board && theArg instanceof Boolean) {
            myIsGameOver = (boolean) theArg;
            stop();
        }
    }
    
    /**
     * Sets up the GUI.
     */
    private void setup() {
        final JPanel mainPanel = new JPanel(new BorderLayout()); 

        myBoard.addObserver(myBoardPanel);
        myBoard.addObserver(myStatusPanel);
        myBoard.addObserver(this);

        final JPanel titledBoardPanel = new JPanel(new BorderLayout());
        titledBoardPanel.add(myBoardPanel, BorderLayout.CENTER);
        titledBoardPanel.setBackground(BACK_COLOR);
        final Font titleFont = new Font(null, Font.BOLD, TetrisGUI.TITLE_FONT_SIZE);

        final TitledBorder title = new TitledBorder("GAME BOARD");
        title.setTitleFont(titleFont);
        titledBoardPanel.setBorder(title);
        title.setTitleColor(Color.YELLOW);

        mainPanel.add(myStatusPanel, BorderLayout.EAST);
        mainPanel.add(titledBoardPanel, BorderLayout.CENTER);

        setJMenuBar(myMenuBar);
        add(mainPanel, BorderLayout.CENTER);
        addKeyListener(new  KeyListener());

        try {
            myIcon = new ImageIcon(ImageIO.read(new File("image/icon.png")));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        
        setIconImage(myIcon.getImage());
        
        setMinimumSize(MINIMUM_SIZE);

        pack();
        
        setPreferredSize(getPreferredSize());

        setLocation(SCREEN_SIZE.width / 2 - getWidth() / 2,
                    (int) (SCREEN_SIZE.getHeight() / 2 - getHeight() / 2));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    /**
     * Shows a message that this game is over.
     */
    private void showGameOver() {
        if (myIsGameOver) {
            ImageIcon icon = null;
            try {
                icon = new ImageIcon(ImageIO.read(new File("image/gameover.png")));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            final int input = JOptionPane.showConfirmDialog(null, null, "Game Over",
                                                            JOptionPane.CLOSED_OPTION,
                                                            JOptionPane.INFORMATION_MESSAGE,
                                                            icon);
            if (input == JOptionPane.OK_OPTION) {
                myBoardPanel.clearPanel();
                myTimer.setDelay(INITIAL_DELAY);
            }
        }
    }
    
    
    /**
     * Pauses the game.
     */
    private void pause() {
        myTimer.stop();
        if (mySoundMode) {
            myPlayer.pause(SOUND_BGM);
        }
        myIsProgress = false;
        myIsGameOver = false;
        myIsPause = true;
    }
    
    /**
     * Resumes the game.
     */
    private void resume() {
        myTimer.start();
        if (mySoundMode) {
            myPlayer.loop(SOUND_BGM);
        }
        myIsProgress = true;
        myIsGameOver = false;
        myIsPause = false;
    }
    
    /**
     * Plays when sound mode is true and piece is dropped.
     */
    private void dropSound() {
        if (mySoundMode) {
            myPlayer.play(SOUND_DROP);
        }
    }
    
    /**
     * Plays when sound mode is true and piece is rotated.
     */
    private void rotateSound() {
        if (mySoundMode) {
            myPlayer.play(SOUND_ROTATION);
        }
    }

    /**
     * Plays when sound mode is true and piece is moved.
     */
    private void moveSound() {
        if (mySoundMode) {
            myPlayer.play(SOUND_MOVE);
        }
    }

    /**
     * This inner class KeyListener is for game operating keys.
     * 
     * @author Jieun Lee
     * @version 1.0 (03-11-2016)
     */
    private class KeyListener extends KeyAdapter {

        /**
         * Invoked when a key has been typed. This event occurs when a key press
         * is followed by a key release.
         */
        @Override
        public void keyTyped(final KeyEvent theEvent) {
            final int keyChar = theEvent.getKeyChar();
            if (keyChar == 'q') {
                stop();
            }
            if (myIsProgress) {
                if (keyChar == KeyEvent.VK_SPACE) {
                    myBoard.drop();
                    dropSound();
                } else if (keyChar == 'z') {
                    myBoard.rotateCCW();
                    rotateSound();
                } else if (keyChar == 'p') {
                    pause();
                }
            } else {
                if (keyChar == KeyEvent.VK_ENTER) {
                    myPlayer.stopAll();
                    start();
                } else if (!myIsGameOver && keyChar == 'p') {
                    resume();
                }
            }
        }

        /**
         * Invoked when a key has been pressed.
         */
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (myIsProgress) {
                final int keyCode = theEvent.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    myBoard.left();
                    moveSound();
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    myBoard.right();
                    moveSound();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    myBoard.down();
                    moveSound();
                } else if (keyCode == KeyEvent.VK_UP) {
                    myBoard.rotateCW();
                    rotateSound();
                } 
            }
        }
    }

    /**
     * This inner class TimerListener is for Timer.
     * 
     * @author Jieun Lee
     * @version 1.0 (03-04-2016)
     */
    private class TimerListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();
        }
    }
    
}
