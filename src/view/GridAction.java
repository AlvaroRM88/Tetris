/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;


/**
 * The GridAction occurs when grid mode is selected on the Tetris menu bar.
 * 
 * @author Jieun Lee
 * @version 1.0 (03-04-2016)
 */
public class GridAction extends AbstractAction {
    
    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = -3554724859043114573L;
    
    /**
     * The game board panel.
     */
    private final BoardPanel myBoardPanel;
    
    /**
     * The grid mode state.
     */
    private boolean myIsSelected;
    
    /**
     * Constructs the GridAction.
     * 
     * @param theBoardPanel The game board panel.
     * @param theValue The truth value of grid mode.
     */
    public GridAction(final BoardPanel theBoardPanel, final boolean theValue) {
        super();
        myBoardPanel = theBoardPanel;
        myIsSelected = theValue;
        putValue(Action.SELECTED_KEY, true);
    }

    /**
     * Invoked when an action occurs.
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        if (myIsSelected) {
            myIsSelected = false;
        } else {
            myIsSelected = true;
        }
        
        myBoardPanel.setGridMode(myIsSelected);
        myBoardPanel.repaint();
    }
}
