package u21629944;

/**
 *
 * @author Dominique Da Silva
 * u21629944
 */

import u21629944.gui.TransactionGUI;

public class U21629944 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TransactionGUI();
            }
        });
    }
    
}
