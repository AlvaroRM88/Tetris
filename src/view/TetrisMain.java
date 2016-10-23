/*
 * TCSS 305 - Winter 2016 
 * Assignment 6A - Tetris
 */

package view;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Runs Tetris by instantiating and starting the TetrisGUI.
 * 
 * @author Jieun Lee
 * @version 1.0 (02-26-2016)
 */
public final class TetrisMain {
    
    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * The main method, runs Tetris. Command line arguments are ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override

            public void run() {
                new TetrisGUI();
            }
        });
    }

}
