/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Board;

/**
 * The JMenuBar for Tetris.
 * 
 * @author Jieun Lee
 * @version 2.1 (03-11-2016)
 */
public class TetrisMenuBar extends JMenuBar {
    
    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = 717478316726628788L;

    /**
     * A minor spacing of slider.
     */
    private static final int SLIDER_MINOR_SPACING = 1;

    /**
     * A major spacing of slider.
     */
    private static final int SLIDER_MAJOR_SPACING = 5;

    /**
     * A minimum spacing of board size slider.
     */
    private static final int BOARD_SIZE_MIN = 5;

    /**
     * A maximum spacing of board size slider.
     */
    private static final int BOARD_SIZE_MAX = 30;
    
    /**
     * A minimum spacing of level slider.
     */
    private static final int LEVEL_MIN = 1;
    
    /**
     * A maximum spacing of level slider.
     */
    private static final int LEVEL_MAX = 20;
    
    /**
     * The TetrisGUI frame.
     */
    private final TetrisGUI myFrame;
    
    /**
     * The Board.
     */
    private final Board myBoard;
    
    /**
     * The game board panel.
     */
    private final BoardPanel myBoardPanel;
    
    /**
     * The status panel.
     */
    private final StatusPanel myStatusPanel;
    
    /**
     * The Start game menu item.
     */
    private final JMenuItem myStartGame;
    
    /**
     * The speed slider.
     */
    private final JSlider mySpeedSlider;
   
    /**
     * The JButton for board size sliders.
     */
    private final JButton myOkButton;
    
    /**
     * The board width.
     */
    private int myWidth;
    
    /**
     * The board Height.
     */
    private int myHeight;
    
    /**
     * The state of sound mode.
     */
    private boolean mySoundMode;

    /**
     * Constructs Tetris menu bar.
     * 
     * @param theFrame The frame.
     * @param theBoard THe game board.
     * @param theBoardPanel The game board panel.
     * @param theStatusPanel The status panel.
     */
    public TetrisMenuBar(final TetrisGUI theFrame, final Board theBoard,
                         final BoardPanel theBoardPanel, final StatusPanel theStatusPanel) {
        super();
        myFrame = theFrame;
        myBoard = theBoard;
        myBoardPanel = theBoardPanel;
        myStatusPanel = theStatusPanel;
        myStartGame = new JMenuItem("Start Game");
        mySpeedSlider = new JSlider();
        myOkButton = new JButton("OK");
        mySoundMode = true;
        setup();
    }
    
    /**
     * Sets enabled the start button with given value.
     * 
     * @param theValue The truth value.
     */
    public void setEnableStartButton(final boolean theValue) {
        myStartGame.setEnabled(theValue);
    }
    
    
    /**
     * Resets the speed slider value to initial value.
     */
    public void resetSpeedSlider() {
        mySpeedSlider.setValue(LEVEL_MIN);
    }
    
    /**
     * Sets up JMenus.
     */
    private void setup() {
        myWidth = myBoard.getWidth();
        myHeight = myBoard.getHeight();
        add(gameMenu());
        add(optionMenu());
        add(helpMenu());
    }
    
    /**
     * Creates and returns Game menu.
     * 
     * @return Game menu.
     */
    private JMenu gameMenu() {
        final JMenu game = new JMenu("Game");
        
        myStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.start();
            }
        });
        
        final JMenuItem quitGame = new JMenuItem("Quit Game");
        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.stop();
            }
        });
        
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.exit();
            }
        });
        
        game.add(myStartGame);
        game.addSeparator();
        game.add(quitGame);
        game.addSeparator();
        game.add(exit);
        
        return game;
    }
    
    /**
     * Creates and returns Option menu.
     * 
     * @return Option menu.
     */
    private JMenu optionMenu() {
        final JMenu option = new JMenu("Option");
        
        final JCheckBoxMenuItem sound = new JCheckBoxMenuItem("Sound On/Off");
        sound.setSelected(mySoundMode);
        sound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (mySoundMode) {
                    mySoundMode = false;
                } else {
                    mySoundMode = true;
                }
                myFrame.setSoundMode(mySoundMode);
            }
        });
        
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Grid On/Off");
        grid.setSelected(false);
        grid.addActionListener(new GridAction(myBoardPanel, false));
        
        final JMenu boardSize = new JMenu("Board Size");
        boardSize.add(createBoardSizeSlider());
        
        final JMenu chooseLevel = new JMenu("Choose speed");
        chooseLevel.add(createChooseSpeedSlider());
        
        final ColorIcon icon = new ColorIcon(14, 14, myBoardPanel.getPieceColor());
        final JMenuItem chooseColor = new JMenuItem("Choose Piece Color", icon);
        chooseColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final Color result = JColorChooser.showDialog(myBoardPanel, "Color", null);
                if (result != null) {
                    icon.setColor(result);
                    chooseColor.setIcon(icon);
                    myBoardPanel.setPieceColor(result);
                }
            }
        });
        
        option.add(grid);
        option.addSeparator();
        option.add(sound);
        option.addSeparator();
        option.add(boardSize);
        option.addSeparator();
        option.add(chooseLevel);
        option.addSeparator();
        option.add(chooseColor);
        
        return option;
    }
    
    /**
     * Creates and returns a panel for choosing level sliders.
     * 
     * @return The panel for choosing level sliders.
     */
    private JPanel createChooseSpeedSlider() {
        final JPanel panel = new JPanel(new BorderLayout());
        mySpeedSlider.setMinorTickSpacing(SLIDER_MINOR_SPACING);
        mySpeedSlider.setMajorTickSpacing(SLIDER_MAJOR_SPACING);
        mySpeedSlider.setMinimum(LEVEL_MIN);
        mySpeedSlider.setMaximum(LEVEL_MAX);
        mySpeedSlider.setValue(LEVEL_MIN);
        mySpeedSlider.setPaintTicks(true);
        mySpeedSlider.setPaintLabels(true);
        final JLabel label = new JLabel("Speed: 1");
        mySpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent theEvent) {
                final JSlider source = (JSlider) theEvent.getSource();
                final int value = source.getValue();
                label.setText("Speed: " + Integer.toString(value));
                final int delay = value * StatusPanel.LEVEL_UP * 10;
                myFrame.setTimerDelay(delay);
                myStatusPanel.setTimeSpeed(delay);
            }
        });
        panel.add(mySpeedSlider, BorderLayout.CENTER);
        panel.add(label, BorderLayout.EAST);

        
        return panel;
    }
    
    /**
     * Creates and returns a panel for board size sliders.
     * 
     * @return The panel for board size sliders.
     */
    private JPanel createBoardSizeSlider() {
        final JPanel panel = new JPanel(new BorderLayout());
       
        final TitledBorder widthTitle = new TitledBorder("Game Board Column");
        final JPanel widthPanel = new JPanel(new BorderLayout());
        final JSlider widthSlider = new JSlider();
        final JLabel widthLabel = new JLabel(Integer.toString(myWidth));
        widthLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        widthSlider.setMinorTickSpacing(SLIDER_MINOR_SPACING);
        widthSlider.setMajorTickSpacing(SLIDER_MAJOR_SPACING);
        widthSlider.setMinimum(BOARD_SIZE_MIN);
        widthSlider.setMaximum(BOARD_SIZE_MAX);
        widthSlider.setValue(myBoard.getWidth());
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        widthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent theEvent) {
                final JSlider source = (JSlider) theEvent.getSource();
                final int value = source.getValue();
                setWidth(value);
                widthLabel.setText(Integer.toString(value));
            }
        });
        widthPanel.add(widthLabel, BorderLayout.CENTER);
        widthPanel.add(widthSlider, BorderLayout.EAST);
        widthPanel.setBorder(widthTitle);
        
        
        final TitledBorder heightTitle = new TitledBorder("Game Board Row");
        final JPanel heightPanel = new JPanel(new BorderLayout());
        final JSlider heightSlider = new JSlider();
        final JLabel heightLabel = new JLabel("20");
        heightLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        heightSlider.setMinorTickSpacing(SLIDER_MINOR_SPACING);
        heightSlider.setMajorTickSpacing(SLIDER_MAJOR_SPACING);
        heightSlider.setMinimum(BOARD_SIZE_MIN);
        heightSlider.setMaximum(BOARD_SIZE_MAX);
        heightSlider.setValue(myBoard.getHeight());
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent theEvent) {
                final JSlider source = (JSlider) theEvent.getSource();
                final int value = source.getValue();
                setHeight(value);
                heightLabel.setText(Integer.toString(value));
            }
        });
        heightPanel.add(heightLabel, BorderLayout.CENTER);
        heightPanel.add(heightSlider, BorderLayout.EAST);
        heightPanel.setBorder(heightTitle);
        
        final JPanel buttonPanel = new JPanel();
        myOkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myBoard.setWidth(myWidth);
                myBoard.setHeight(myHeight);
                myBoardPanel.clearPanel();
                myBoardPanel.repaint();
                myStatusPanel.clear();
            }
            
        });
        buttonPanel.add(myOkButton);
        
        panel.add(widthPanel, BorderLayout.NORTH);
        panel.add(heightPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Sets the board width.
     * 
     * @param theWidth The board width.
     */
    private void setWidth(final int theWidth) {
        myWidth = theWidth;
    }
    
    /**
     * Sets the board height.
     * 
     * @param theHeight The board Height.
     */
    private void setHeight(final int theHeight) {
        myHeight = theHeight;
    }

    /**
     * Sets enable the button with given truth value.
     * 
     * @param theValue The truth value.
     */
    public void setButtonEnable(final boolean theValue) {
        myOkButton.setEnabled(theValue);
    }

    /**
     * Creates and returns Help menu.
     * 
     * @return Help menu;
     */
    private JMenu helpMenu() {
        final JMenu help = new JMenu("Help");
        
        final String aboutGameTitle = "About Game";
        final JMenuItem aboutGame = new JMenuItem(aboutGameTitle);
        aboutGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final String t = " points x Current level\n";
                final String text =
                                "About Level Up:\n" + "\t\tWhen every " + StatusPanel.LEVEL_UP
                                    + " lines are cleared.\n\n" + "About Score: \n"
                                    + "\t\t1 line  = " + StatusPanel.ONE_LINE_SCORE
                                    + t + "\t\t2 lines = " + StatusPanel.TWO_LINES_SCORE 
                                    + t + "\t\t3 lines = " + StatusPanel.THREE_LINES_SCORE
                                    + t + "\t\t4 lines = " + StatusPanel.FOUR_LINES_SCORE 
                                    + t + "\nAbout Option:\n" + "\t\tBoard Size:\tBoard Size "
                                    + "cannot be changed when the game is in progress.\n\t\t"
                                    + "Choose Speed:\tHigher number is faster."
                                    + "\n\t\tPeice Color: \tChoose color";
                final ImageIcon imageIcon = myFrame.getIcon();
                JOptionPane.showMessageDialog(null, text, aboutGameTitle,
                                              JOptionPane.INFORMATION_MESSAGE, imageIcon);
            }
        });
        
        final String aboutTitle = "About";
        final JMenuItem about = new JMenuItem(aboutTitle);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final String text = "TCSS 305 Tetris" + '\n' + "Winter 2016" + '\n'
                                + "<Jieun Lee>";
                final ImageIcon imageIcon = myFrame.getIcon();
                JOptionPane.showMessageDialog(null, text, aboutTitle,
                                              JOptionPane.INFORMATION_MESSAGE, imageIcon);
            }
        });
        
        help.add(aboutGame);
        help.addSeparator();
        help.add(about);
        
        return help;
    }
    
    /**
     * The ColorIcon object for Tetris Choose color menu item.
     * 
     * @author Jieun Lee
     * @version 1.0 (03-10-2016)
     */
    private class ColorIcon implements Icon {

        /**
         * Default x-coordinate.
         */
        private static final int DEFAULT_X = 18;

        /**
         * Default y-coordinate.
         */
        private static final int DEFAULT_Y = 2;

        /**
         * The width of the ColorIcon.
         */
        private final int myWidth;

        /**
         * The height of the ColorIcon.
         */
        private final int myHeight;

        /**
         * The color of the ColorIcon.
         */
        private Color myColor;

        /**
         * Constructs the ColorIcon.
         * 
         * @param theWidth The width.
         * @param theHeight The height.
         * @param theColor The color.
         */
        ColorIcon(final int theWidth, final int theHeight, final Color theColor) {
            if (theWidth < 0 || theHeight < 0) {
                throw new IllegalArgumentException();
            }
            myWidth = theWidth;
            myHeight = theHeight;
            myColor = theColor;
        }

        /**
         * Sets color.
         * 
         * @param theColor The color.
         */
        public void setColor(final Color theColor) {
            myColor = theColor;
        }

        /**
         * Paints the icon.
         */
        @Override
        public void paintIcon(final Component theComponent, final Graphics theGraphic,
                              final int theX, final int theY) {
            final Color color = theGraphic.getColor();
            theGraphic.setColor(myColor);
            theGraphic.fillRect(DEFAULT_X, DEFAULT_Y, getIconWidth(), getIconHeight());
            theGraphic.setColor(color);
        }

        /**
         * Returns the width of the icon.
         */
        @Override
        public int getIconWidth() {
            return myWidth;
        }

        /**
         * Returns the height of the icon.
         */
        @Override
        public int getIconHeight() {
            return myHeight;
        }

    }

}
