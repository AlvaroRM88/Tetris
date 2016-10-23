/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Block;
import model.Board;

/**
 * The JPanel for Tetris game board.
 * 
 * @author Jieun Lee
 * @version 1.1 (03-11-2016)
 */
public class BoardPanel extends JPanel implements Observer {

    /**
     * A generated serial version UID for object Serialization.
     */

    private static final long serialVersionUID = -4483614379842967942L;

    /**
     * The Board.
     */
    private final Board myBoard;

    /**
     * The list of Block array.
     */
    private List<Block[]> myPieceList;

    /**
     * The piece color.
     */
    private Color myPieceColor;

    /**
     * The pixel of each block.
     */
    private double myPixel;

    /**
     * The state of grid mode.
     */
    private boolean myGridMode;

    /**
     * Constructs a game board panel.
     * 
     * @param theBoard The Board.
     */
    public BoardPanel(final Board theBoard) {
        super();
        myBoard = theBoard;
        myPieceList = new ArrayList<Block[]>();
        myPieceColor = Color.ORANGE;
        myPixel = TetrisGUI.INITIAL_PX;
        myGridMode = false;

        setBackground(TetrisGUI.BACK_COLOR);
        setPreferredSize(new Dimension(myBoard.getWidth() * TetrisGUI.INITIAL_PX,
                                       myBoard.getHeight() * TetrisGUI.INITIAL_PX));
        setSize(getPreferredSize());
    }

    /**
     * Returns piece color.
     * 
     * @return myPieceColor.
     */
    public Color getPieceColor() {
        return myPieceColor;
    }

    /**
     * Sets the piece color.
     * 
     * @param theColor The color.
     */
    public void setPieceColor(final Color theColor) {
        myPieceColor = theColor;
    }

    /**
     * Sets grid mode.
     * 
     * @param theValue The state of grid mode.
     */
    public void setGridMode(final boolean theValue) {
        if (theValue) {
            myGridMode = true;
        } else {
            myGridMode = false;
        }
    }

    /**
     * Clears this board panel.
     */
    public void clearPanel() {
        myPieceList.clear();
        repaint();
    }

    /**
     * This method is called whenever the block is changed in the Board class.
     * 
     * @param theObservable Board.
     * @param theArg The list of block array passed to the notifyObservers
     *            method.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theObservable instanceof Board && theArg instanceof List<?>) {
            myPieceList = (List<Block[]>) theArg;
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
        setCurrentSize();
        drawPanel(theGraphics);
        if (myGridMode) {
            drawGrid(theGraphics);
        }
        if (!myPieceList.isEmpty()) {
            drawPiece(theGraphics);
        }
    }

    /**
     * Sets current width and height of this panel for drawing game board panel,
     * Tetris piece, and grids.
     */
    private void setCurrentSize() {
        myPixel = Math.min(getWidth() / myBoard.getWidth(), getHeight() / myBoard.getHeight());
    }

    /**
     * Draws background panel.
     * 
     * @param theGraphics The graphics.
     */
    private void drawPanel(final Graphics theGraphics) {
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final Shape rectangle = new Rectangle2D.Double(0, 0, myBoard.getWidth() * myPixel,
                                                       myBoard.getHeight() * myPixel);
        g2d.setColor(Color.BLACK);
        g2d.fill(rectangle);
        g2d.draw(rectangle);
    }

    /**
     * Draws Tetris piece.
     * 
     * @param theGraphics The graphics.
     */
    private void drawPiece(final Graphics theGraphics) {
        final Graphics2D g2d = (Graphics2D) theGraphics;
        for (int row = 0; row < myPieceList.size(); row++) {
            final Block[] line = myPieceList.get(row);
            for (int column = 0; column < line.length; column++) {
                if (line[column] != null) {
                    final Shape cell = new RoundRectangle2D.Double(column * myPixel, 
                                            myBoard.getHeight() * myPixel 
                                                            - ((row + 1) * myPixel), 
                                            myPixel, myPixel, 7, 7);

                    g2d.setColor(myPieceColor);
                    g2d.fill(cell);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(cell);
                }
            }
        }
    }

    /**
     * Draws grid.
     * 
     * @param theGraphics The graphic.
     */
    private void drawGrid(final Graphics theGraphics) {
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.DARK_GRAY);

        Shape column = null;
        for (int i = 1; i < myBoard.getWidth(); i++) {
            column = new Line2D.Double(myPixel * i, 0, myPixel * i,
                                       myBoard.getHeight() * myPixel);
            g2d.draw(column);
        }

        Shape row = null;
        for (int i = 1; i < myBoard.getHeight(); i++) {
            row = new Line2D.Double(0, myPixel * i, myBoard.getWidth() * myPixel, myPixel * i);
            g2d.draw(row);
        }

        final Shape border = new Rectangle2D.Double(0, 0, myBoard.getWidth() * myPixel,
                                                    myBoard.getHeight() * myPixel);

        g2d.setColor(Color.BLACK);
        g2d.draw(border);
    }

}
