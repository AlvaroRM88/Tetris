/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Block;
import model.Board;
import model.Point;
import model.TetrisPiece;

/**
 * The JPanel for the next TetrisPiece.
 * 
 * @author Jieun Lee
 * @version 1.0 (03-11-2016)
 */
public class NextPiecePanel extends JPanel implements Observer {

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = -6951183113237575094L;

    /**
     * The default divider for dividing row and column.
     */
    private static final int DEFAULT_LINE = 6;

    /**
     * The default pixel.
     */
    private static final int DEFAULT_PX = 24;

    /**
     * The Default TetrisPiece.
     */
    private static final TetrisPiece DEFAULT_PIECE = null;

    /**
     * The next Tetris piece.
     */
    private TetrisPiece myNextPiece;

    /**
     * Constructs next Tetris piece panel.
     * 
     * @param theBoard The Board.
     */
    public NextPiecePanel(final Board theBoard) {
        super();
        myNextPiece = DEFAULT_PIECE;
        setPreferredSize(new Dimension(DEFAULT_PX * DEFAULT_LINE, DEFAULT_PX * DEFAULT_LINE));
        setBackground(Color.BLACK);
        setSize(getPreferredSize());
        theBoard.addObserver(this);
    }

    /**
     * Clears this panel.
     */
    public void clearPanel() {
        myNextPiece = DEFAULT_PIECE;
    }

    /**
     * This method is called whenever the TetrisPiece is changed in the Board
     * class.
     * 
     * @param theObservable Board.
     * @param theArg The TetrisPiece passed to the notifyObservers method.
     */
    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theObservable instanceof Board && theArg instanceof TetrisPiece) {
            myNextPiece = (TetrisPiece) theArg;
        }
        repaint();
    }

    /**
     * Paint component for this JPanel.
     * 
     * @param theGraphics the Graphics object to protect.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        if (myNextPiece != null) {
            drawNextPiece(theGraphics);
        }
    }

    /**
     * Draws the next Tetris piece.
     * 
     * @param theGraphics The graphics.
     */
    private void drawNextPiece(final Graphics theGraphics) {
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final Point[] pointList = myNextPiece.getPoints().clone();
        for (int i = 0; i < pointList.length; i++) {
            final int x = (pointList[i].x() + 1) * DEFAULT_PX;
            final int y = ((-1 * pointList[i].y()) + myNextPiece.getWidth() + 1) * DEFAULT_PX;
            final Shape rectangle =
                            new RoundRectangle2D.Double(x, y, DEFAULT_PX, DEFAULT_PX, 5, 5);
            Color color;
            if (myNextPiece.getBlock() == Block.I) {
                color = Color.CYAN;
            } else if (myNextPiece.getBlock() == Block.J) {
                color = Color.BLUE;
            } else if (myNextPiece.getBlock() == Block.L) {
                color = Color.ORANGE;
            } else if (myNextPiece.getBlock() == Block.O) {
                color = Color.YELLOW;
            } else if (myNextPiece.getBlock() == Block.S) {
                color = Color.GREEN;
            } else if (myNextPiece.getBlock() == Block.T) {
                color = Color.MAGENTA;
            } else {
                color = Color.RED;
            }
            
            g2d.setColor(color);
            g2d.fill(rectangle);
            g2d.setColor(Color.BLACK);
            g2d.draw(rectangle);
        }
    }

}
