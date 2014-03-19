package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Pop-up dialog to notify user of win/lose/draw situations.
 */
public class GameOverDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8454313106564106229L;
	JPanel panel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JLabel jLabel1 = new JLabel();
    JPanel jPanel1 = new JPanel();
    JButton quitBtn = new JButton("Quit");
    JButton restartBtn = new JButton("Restart");

    /**
     * Constructor for the class.
     * @param owner The TicTacToeFrame that is using this dialog.
     */
    public GameOverDialog(BoardFrame<?> owner) {
        super(owner);
        try {
            initGUI();
            pack();
            this.setLocation(owner.getLocation().x +
                             owner.getWidth() / 2 - this.getWidth() / 2,
                             owner.getLocation().y +
                             owner.getHeight() / 2 - this.getHeight() / 2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes the GUI elements.
     */
    private void initGUI() throws Exception {
        panel1.setLayout(borderLayout1);
        jLabel1.setBackground(Color.red);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 24));
        jLabel1.setOpaque(true);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setHorizontalTextPosition(SwingConstants.LEFT);
        jLabel1.setText("The computer has won!");
        quitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitBtn_actionPerformed(e);
            }
        });
        restartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartBtn_actionPerformed(e);
            }
        });
        this.setResizable(false);
        this.getContentPane().setBackground(SystemColor.control);
        this.setModal(true);
        this.setTitle("Game Over.");
        jPanel1.setBackground(Color.cyan);
        getContentPane().add(panel1);
        panel1.add(jLabel1, BorderLayout.NORTH);
        panel1.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(quitBtn, null);
        jPanel1.add(restartBtn, null);
    }

    /**
     * Displays a draw game message.
     */
    void draw() {
        jLabel1.setText("It's a Draw!");
        setVisible(true);
    }

    /**
     * Displays a message that a player has won.
     * @param s The name of the player that has won.
     */
    void playerWon(String s) {
        jLabel1.setText(s + " has won!");
        setVisible(true);
    }

    /**
     * Action performed for the Quit button.
     * The dialog is closes and the frame is asked to exit.
     */
    private void quitBtn_actionPerformed(ActionEvent e)  {
        dispose();
        ((BoardFrame<?>)this.getOwner()).exit();
    }

    /**
     * The action performed for the Restart button.
     * The dialog is closed and the frame is reset.
     * @param e
     */
    private void restartBtn_actionPerformed(ActionEvent e) {
        dispose();
        ((BoardFrame<?>)this.getOwner()).resetAll();
    }
}
