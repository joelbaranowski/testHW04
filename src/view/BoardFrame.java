package view;

import gameIO.*;

import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class BoardFrame<TPlayer> extends JFrame implements IView<TPlayer> { 

	private static final long serialVersionUID = 1557813045878367620L;
	private JPanel contentPane;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private JButton resetBtn = new JButton("Reset");
    private JPanel jPanel2 = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();
    private GridLayout gridLayout2 = new GridLayout();
    private JPanel jPanel3 = new JPanel();

    private GridLayout gridLayout3 = new GridLayout();
    private JButton startBtn = new JButton("Start");
    private JLabel jLabel1 = new JLabel("Starting player (X):");
    private JButton[][] btnArray;
    private String[] symbolStr = {"1", "2"};

    private IModelAdmin<TPlayer> modelAdmin;
    private IViewRequestor requestor;
    private JLabel statusLbl = new JLabel(" Remaining time:");
    private JComboBox<TPlayer> player1CBx = new JComboBox<TPlayer>();
    private JLabel jLabel2 = new JLabel("Second player (Y):");
    private JComboBox<TPlayer> player2CBx = new JComboBox<TPlayer>();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel5 = new JPanel();
    private GridLayout gridLayout4 = new GridLayout();
    private JLabel jLabel3 = new JLabel("Add player");
    private JTextField addPlayerTF = new JTextField("comp202prof.SBW_DXN_strategy1");

    public BoardFrame(IModelAdmin<TPlayer> modelAdmin) {
        this.modelAdmin = modelAdmin;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            initGUI();
        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

    public ICommand getCommand() {
        return new ICommand() {
            public void setTokenAt(int row, int col, int player) {
            	if(player == -2)
            		btnArray[row] [col].setText(symbolStr[0]);
            	else if(player == -1)
            		btnArray[row][col].setText(symbolStr[1]);
            	else
            		btnArray[row] [col].setText("" + player);
                statusLbl.setText(" ");
            }

            public void clearTokenAt(int row, int col) {
                btnArray[row] [col].setText(" ");
            }

            public void setMessage(String s) {
                statusLbl.setText(s);
            }
        };
    }

    /** Component initialization */
    private void initGUI() throws Exception  {
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(1059, 471));
        this.setTitle("Frame Title");
        resetBtn.setEnabled(false);
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBtn_actionPerformed(e);
            }
        });
        jPanel2.setLayout(gridLayout1);
        jPanel2.setBackground(Color.black);
        jPanel1.setLayout(gridLayout2);
        gridLayout2.setRows(-1);
        gridLayout2.setColumns(1);
        gridLayout2.setHgap(10);
        gridLayout2.setVgap(5);
        jPanel3.setLayout(gridLayout3);
        gridLayout3.setRows(-1);
        gridLayout3.setColumns(1);
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startBtn_actionPerformed(e);
            }
        });
        borderLayout1.setHgap(20);
        borderLayout1.setVgap(20);
        statusLbl.setPreferredSize(new Dimension(3, 20));
        jPanel4.setLayout(gridLayout4);
        gridLayout4.setRows(2);
        gridLayout4.setColumns(1);
        addPlayerTF.setPreferredSize(new Dimension(500, 21));
        addPlayerTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlayerTF_actionPerformed(e);
            }
        });
        contentPane.add(jPanel1, BorderLayout.WEST);
        contentPane.add(jPanel5, BorderLayout.NORTH);
        jPanel5.add(jLabel3, null);
        jPanel5.add(addPlayerTF, null);
        jPanel1.add(jPanel4, null);
        jPanel4.add(resetBtn, null);
        jPanel4.add(startBtn, null);
        jPanel1.add(jPanel3, null);
        jPanel3.add(jLabel1, null);
        jPanel3.add(player1CBx, null);
        jPanel3.add(jLabel2, null);
        jPanel3.add(player2CBx, null);
        contentPane.add(jPanel2, BorderLayout.CENTER);
        contentPane.add(statusLbl, BorderLayout.SOUTH);
    }

    /** Overridden so we can exit when window is closed */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            exit();
        }
    }

    /**
     * Closes the frame and exits the model, returning to the controller.
     */
    public void exit() {
        dispose();
        modelAdmin.exit();
    }

    /**
     * Resets the frame to its starting state.
     */
    public void reset() {
        clearBtns();
        enableBtns(false);
        resetBtn.setEnabled(false);
        startBtn.setEnabled(true);
        statusLbl.setText("");
    }

    /**
     * Clears the text from all the buttons.
     */
    void clearBtns() {
        for (int i = 0; i < btnArray.length; i++) {
            for (int j = 0; j < btnArray[i].length; j++) btnArray[i] [j].setText("");
        }
    }

    /**
     * Enables the buttons so that the user can input a move.
     */
    public void enableBtns(boolean state) {
        for (int i = 0; i < btnArray.length; i++) {
            for (int j = 0; j < btnArray[i].length; j++) btnArray[i] [j].setEnabled(state);
        }
    }

    /**
     * Resets the frame and the model.
     */
    public void resetAll() {
        reset();
        modelAdmin.reset();
    }

    private void resetBtn_actionPerformed(ActionEvent e) {
        resetAll();
    }

    public void setRequestor(IViewRequestor requestor) {
        this.requestor = requestor;
    }



    private void startBtn_actionPerformed(ActionEvent e) {
        startBtn.setEnabled(false);
        resetBtn.setEnabled(true);
        modelAdmin.setPlayers(player1CBx.getItemAt(player1CBx.getSelectedIndex()), player2CBx.getItemAt(player2CBx.getSelectedIndex()));
    }

    public void draw() {
    	SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
		        new GameOverDialog(BoardFrame.this).draw();
			}
    	});

    }

    public void start() {
    	modelAdmin.setCommand(getCommand());
    	
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2,
                         (screenSize.height - frameSize.height) / 2);
        
        setPlayers(modelAdmin.getPlayers());
        
        resetAll();
        setVisible(true);
    }

    public void win(final int player) {
    	SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				new GameOverDialog(BoardFrame.this).playerWon("Player \""+ symbolStr[player]+"\"");
			}
    	});
        
    }

    public void setPlayers(List<TPlayer> players) {
        for(int i=0; i<players.size();i++) {
            player1CBx.addItem(players.get(i));
            player2CBx.addItem(players.get(i));
        }
    }

    /**
     * Dynamically fill the button grid
     */
    public void setDimension(Dimension size) {
        int nRows = size.height;
        int nCols = size.width;
        gridLayout1.setRows(nRows);
        gridLayout1.setColumns(nCols);
        gridLayout1.setHgap(5);
        gridLayout1.setVgap(5);
        btnArray = new JButton[nRows] [nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col<nCols; col++) {
                btnArray[row][col] = new JButton();
                final int thisRow = row;
                final int thisCol = col;
                btnArray[thisRow][thisCol].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        enableBtns(false);
                        requestor.setTokenAt(thisRow, thisCol, new IRejectCommand() {
                            public void execute() {
                                statusLbl.setText("There is already an " +
                                        btnArray[thisRow][thisCol].getText() +
                                        " at (" + thisRow + "," + thisCol + ")!");
                          }
                      });
                }
              });
              btnArray[thisRow][thisCol].setBackground(Color.cyan);
              btnArray[thisRow][thisCol].setFont(new java.awt.Font("Dialog", 1, 50));
              jPanel2.add(btnArray[thisRow][thisCol], null);
          }
        }
        validate();
    }

    /**
     * Asks the model to add a new ComputerPlayer given the String in the
     * addPlayerTF textfield that holds the fully qualified class name of the INextMoveStrategy.
     * The new player is added to both combo boxes.
     * @param e
     */
    private void addPlayerTF_actionPerformed(ActionEvent e) {
        TPlayer newPlayer = modelAdmin.addPlayer(addPlayerTF.getText());
        if(null!=newPlayer) {
            player1CBx.addItem(newPlayer);
            player2CBx.addItem(newPlayer);
        }
        addPlayerTF.setText("");
    }
}
