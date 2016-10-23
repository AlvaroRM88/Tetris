/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.Board;
import sound.SoundPlayer;

/**
 * The StatusPanel represents number of cleared line, level, score, instruction
 * of game operating keys, and next Tetris piece.
 * 
 * @author Jieun Lee
 * @version 2.0 (03-11-2016)
 */
public class StatusPanel extends JPanel implements Observer {
    
    /**
     * The number of completed lines to one level up.
     */
    public static final int LEVEL_UP = 5;
    
    /**
     * The score when one line is completed.
     */
    public static final int ONE_LINE_SCORE = 40;
    
    /**
     * The score when two lines are completed.
     */
    public static final int TWO_LINES_SCORE = 100;
    
    /**
     * The score when three lines are completed.
     */
    public static final int THREE_LINES_SCORE = 300;
    
    /**
     * The score when four lines are completed.
     */
    public static final int FOUR_LINES_SCORE = 1200;

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = 8542384479983800313L;
    
    /**
     * The initial number of completed line and score.
     */
    private static final int INITIAL_STATUS = 0;
    
    /**
     * The starting text that has 'html' for line, score, and level labels.
     */
    private static final String DEFAULT_START_HTML_TEXT =
                    "<html><font color='white' size=6><b>";
    /**
     * The ending text that has 'html' for line, score, and level labels.
     */
    private static final String DEFAULT_END_HTML_TEXT = "</b></font></html>";

    /**
     * The starting text that has 'html' for next level label.
     */
    private static final String LEVEL_UP_START_HTML_TEXT =
                    "<html><font color='yellow' size=5><b>";

    /**
     * The ending text that has 'html' for next level label.
     */
    private static final String LEVEL_UP_END_HTML_TEXT =
                    " </b></font><font color='white'> more line(s) to level up</font>"
                                                         + DEFAULT_END_HTML_TEXT;
    /**
     * The frame.
     */
    private final TetrisGUI myFrame;

    /**
     * The next Tetris piece panel.
     */
    private final NextPiecePanel myNextPanel;

    /**
     * The title font.
     */
    private final Font myTitleFont;

    /**
     * The JLbale for line.
     */
    private final JLabel myLineLabel;
    
    /**
     * The JLabel for level.
     */
    private final JLabel myLevelLabel;
    
    /**
     * The JLabel for score.
     */
    private final JLabel myScoreLabel;
    
    /**
     * The JLabel for indication the remaining line to next level.
     */
    private final JLabel myNextLevelLabel;
    
    /**
     * The SoundPlayer.
     */
    private final SoundPlayer myPlayer;

    /**
     * The number of line.
     */
    private int myLine;
    
    /**
     * The level of the game.
     */
    private int myLevel;
    
    /**
     * The time speed.
     */
    private int myTimeSpeed;
    
    /**
     * The score.
     */
    private int myScore;
    
    /**
     * Construct the status panel.
     * 
     * @param theFrame The Frame.
     * @param theBoard The game board.
     * @param thePlayer The SoundPlayer.
     */
    public StatusPanel(final TetrisGUI theFrame, final Board theBoard,
                       final SoundPlayer thePlayer) {
        super(new BorderLayout());
        myFrame = theFrame;
        myNextPanel = new NextPiecePanel(theBoard);
        myPlayer = thePlayer;
        myTitleFont = new Font(null, Font.BOLD, TetrisGUI.TITLE_FONT_SIZE);
        myLineLabel = new JLabel(DEFAULT_START_HTML_TEXT + Integer.toString(INITIAL_STATUS)
                                 + DEFAULT_END_HTML_TEXT);
        myLevelLabel = new JLabel(DEFAULT_START_HTML_TEXT
                                  + Integer.toString(INITIAL_STATUS + 1)
                                  + DEFAULT_END_HTML_TEXT);
        myScoreLabel = new JLabel(DEFAULT_START_HTML_TEXT + Integer.toString(INITIAL_STATUS)
                                  + DEFAULT_END_HTML_TEXT);
        myNextLevelLabel = new JLabel(LEVEL_UP_START_HTML_TEXT + Integer.toString(LEVEL_UP)
                                      + LEVEL_UP_END_HTML_TEXT);
        add(mainPanel());
    }
    
    /**
     * Sets the time speed.
     * 
     * @param theSpeed The time speed.
     */
    public void setTimeSpeed(final int theSpeed) {
        myTimeSpeed = theSpeed;
    }
    
    /**
     * Clears the next Tetris piece panel.
     */
    public void clear() {
        myNextPanel.clearPanel();
        myNextPanel.repaint();
        myLine = 0;
        myLevel = 1;
        myScore = 0;
        myLineLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(myLine)
                            + DEFAULT_END_HTML_TEXT);
        myLevelLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(myLevel)
                             + DEFAULT_END_HTML_TEXT);
        myScoreLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(myScore)
                             + DEFAULT_END_HTML_TEXT);
        myNextLevelLabel.setText(LEVEL_UP_START_HTML_TEXT + Integer.toString(LEVEL_UP)
                                 + LEVEL_UP_END_HTML_TEXT);
    }

    /**
     * This method is called whenever the completeRows is changed in the Board
     * class.
     * 
     * @param theObservable Board.
     * @param theArg The array of comleateRows passed to the notifyObservers method.
     */
    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theObservable instanceof Board && theArg instanceof Integer[]) {
            final int line = ((Integer[]) theArg).length;
            myLine += line;
            if (myFrame.isSoundMode()) {
                myPlayer.play("sound/lineClear.wav");
            }
            updateLine(myLine);
            updateScore(line, myLevel);
            myLevel = (myLine / LEVEL_UP) + 1;
            updateLevel(myLevel);
            updateTimer(myLevel);
            updateNextLevel(myLine);
        }
    }
    
    /**
     * Creates and returns status panel that has next piece panel, score, line,
     * level, key option.
     * 
     * @return The status panel.
     */
    private JPanel mainPanel() {

        final JPanel panel = new JPanel();
        final JPanel main = new JPanel(new BorderLayout());
        final JPanel sub = new JPanel(new BorderLayout());
        sub.add(nextPiecePanel(), BorderLayout.CENTER);
        
        panel.setBackground(TetrisGUI.BACK_COLOR);

        final JPanel update = new JPanel(new BorderLayout());
        update.add(sub, BorderLayout.CENTER);
        update.add(updatedInfoPanel(), BorderLayout.SOUTH);

        main.add(update, BorderLayout.NORTH);
        main.add(keyPanel(), BorderLayout.CENTER);

        panel.add(main);
        
        return panel;
    }

    /**
     * Creates and returns Next panel that shows next Tetris piece.
     * 
     * @return The Next panel.
     */
    private JPanel nextPiecePanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(TetrisGUI.BACK_COLOR);
        
        final TitledBorder title = new TitledBorder("NEXT");
        title.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        title.setTitleFont(myTitleFont);
        
        panel.add(myNextPanel, BorderLayout.CENTER);
        panel.setBorder(title);

        return panel;
    }

    /**
     * Creates and returns Info panel that has score, line and level.
     * 
     * @return The info panel.
     */
    private JPanel updatedInfoPanel() {
        final JPanel panel = new JPanel(new GridLayout(4, 1));

        final TitledBorder scoreTitle = new TitledBorder("SCORE");
        scoreTitle.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        scoreTitle.setTitleFont(myTitleFont);
        final JPanel scorePane = new JPanel();
        scorePane.setBackground(TetrisGUI.BACK_COLOR);
        scorePane.add(myScoreLabel);
        scorePane.setBorder(scoreTitle);

        final TitledBorder levelTitle = new TitledBorder("LEVEL");
        levelTitle.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        levelTitle.setTitleFont(myTitleFont);
        final JPanel levelPane = new JPanel();
        levelPane.setBackground(TetrisGUI.BACK_COLOR);
        levelPane.add(myLevelLabel);
        levelPane.setBorder(levelTitle);

        final TitledBorder lineTitle = new TitledBorder("LINE");
        lineTitle.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        lineTitle.setTitleFont(myTitleFont);
        final JPanel linePane = new JPanel();
        linePane.setBackground(TetrisGUI.BACK_COLOR);
        linePane.add(myLineLabel);
        linePane.setBorder(lineTitle);
        
        final TitledBorder nextTitle = new TitledBorder("LEVEL UP");
        nextTitle.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        nextTitle.setTitleFont(myTitleFont);
        final JPanel nextPane = new JPanel();
        nextPane.setBackground(TetrisGUI.BACK_COLOR);
        nextPane.add(myNextLevelLabel);
        nextPane.setBorder(nextTitle);
        
        panel.add(scorePane);
        panel.add(levelPane);
        panel.add(linePane);
        panel.add(nextPane);

        return panel;
    }

    /**
     * Creates and returns the instruction of game operating keys.
     * 
     * @return The instruction of game operating keys.
     */
    private JPanel keyPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(TetrisGUI.BACK_COLOR);
        final TitledBorder title = new TitledBorder("KEYS");
        title.setTitleColor(TetrisGUI.TITLE_FONT_COLOR);
        title.setTitleFont(myTitleFont);

        final JLabel start = new JLabel("<html><font color='white'><b>Start Game: </b>"
                        + "Enter <p><b>End Game: </b>(lower) q <p> "
                        + "<b>Pause/Resme: </b>(lower) p <p><p>"
                        + "<b>Move Left: </b>← <p>"
                        + "<b>Move Right: </b>→<p>"
                        + "<b>Move Down: </b>↓<p>"
                        + "<b>Move Drop: </b>Space<p>"
                        + "<b>Rotate CW: </b>↑<p>"
                        + "<b>Rotate CCW: </b>(lower) z <br>"
                        + " \t<br></font></html>");
        final JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.add(start, BorderLayout.CENTER);
        startPanel.setBackground(TetrisGUI.BACK_COLOR);

        panel.add(startPanel, BorderLayout.WEST);
        panel.setBorder(title);

        return panel;
    }

    
    /**
     * Updates the number of total completed lines.
     * 
     * @param theLine The completed lines.
     */
    private void updateLine(final int theLine) {
        myLineLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(theLine)
                            + DEFAULT_END_HTML_TEXT);
    }
    
    /**
     * Updates the current level.
     * 
     * @param theLevel The level.
     */
    private void updateLevel(final int theLevel) {
        myLevelLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(theLevel)
                             + DEFAULT_END_HTML_TEXT);
    }
    
    /**
     * Updates the the total score.
     * 
     * @param theLine The number of the completed lines.
     * @param theLevel The current level.
     */
    private void updateScore(final int theLine, final int theLevel) {
        final int score;
        if (theLine == 1) {
            score = theLevel * ONE_LINE_SCORE;
        } else if (theLine == 2) {
            score = theLevel * TWO_LINES_SCORE;
        } else if (theLine == (2 + 1)) {
            score = theLevel * THREE_LINES_SCORE;
        } else {
            score = theLevel * FOUR_LINES_SCORE;
        }
        myScore += score;
        myScoreLabel.setText(DEFAULT_START_HTML_TEXT + Integer.toString(myScore)
                             + DEFAULT_END_HTML_TEXT);
    }

    /**
     * Updates the delay time for timer when levels up.
     * 
     * @param theLevel The current level.
     */
    private void updateTimer(final int theLevel) {
        if (theLevel > 1) {
            final int delay = myTimeSpeed + (theLevel * LEVEL_UP * 10);
            myFrame.setTimerDelay(delay);
        }
    }
    
    /**
     * Updates number of left line(s) to clear for the one level up.
     * 
     * @param theLine the total cleared line.
     */
    private void updateNextLevel(final int theLine) {
        final int leftLine = LEVEL_UP - (theLine % LEVEL_UP);
        myNextLevelLabel.setText(LEVEL_UP_START_HTML_TEXT 
                        + Integer.toString(leftLine) + LEVEL_UP_END_HTML_TEXT);
    }
    
}
