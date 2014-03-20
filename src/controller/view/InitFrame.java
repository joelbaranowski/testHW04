package controller.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class InitFrame extends JFrame {
	
	private static final long serialVersionUID = 669534362216231577L;

	private IInitModelAdapter model;
	
	JPanel jPanel1 = new JPanel();
	JTextField nRowsTF = new JTextField("3");
	JTextField nColsTF = new JTextField("3");
	JButton makeTTTBtn = new JButton("Make TicTacToe!");
	JPanel jPanel2 = new JPanel();
	JButton othelloBtn = new JButton("Make Othello!");
	JTextField inRowTF = new JTextField("3");
	JLabel jLabel1 = new JLabel("in-a-row");
	JLabel jLabel2 = new JLabel("rows, cols:");

	JLabel jLabel3 = new JLabel("Max. turn time (s):");
	JTextField maxTurnTimeTF = new JTextField("60");
	private final JButton btnMakeGamegrid = new JButton("Make GameGrid!");

	public InitFrame(int closeOp, IInitModelAdapter model) {
		this.model = model;
		this.setDefaultCloseOperation(closeOp);
		initGUI();
	}

	private void initGUI()  {
		this.setSize(new Dimension(560, 188));
		nRowsTF.setPreferredSize(new Dimension(50, 21));
		nColsTF.setPreferredSize(new Dimension(50, 21));

		makeTTTBtn.addActionListener(new java.awt.event.ActionListener()  {
			public void actionPerformed(ActionEvent e) {
				model.makeTicTacToe(
						Integer.parseInt(nRowsTF.getText()),
						Integer.parseInt(nColsTF.getText()),
						Integer.parseInt(inRowTF.getText()),
						Integer.parseInt(maxTurnTimeTF.getText())
						);
			}
		});

		othelloBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
					model.makeOthello(
						 Integer.parseInt(nRowsTF.getText()),
						 Integer.parseInt(nColsTF.getText()),
						 Integer.parseInt(maxTurnTimeTF.getText())
						 );
			}
		});

		inRowTF.setMinimumSize(new Dimension(10, 21));
		inRowTF.setPreferredSize(new Dimension(20, 21));

		maxTurnTimeTF.setPreferredSize(new Dimension(30, 21));
		this.getContentPane().add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(jLabel2, null);
		jPanel1.add(nRowsTF, null);
		jPanel1.add(nColsTF, null);
		jPanel1.add(jLabel3, null);
		jPanel1.add(maxTurnTimeTF, null);
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(othelloBtn, null);
		jPanel2.add(makeTTTBtn, null);
		jPanel2.add(inRowTF, null);
		jPanel2.add(jLabel1, null);
		btnMakeGamegrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.makeGameGrid(
						 Integer.parseInt(nRowsTF.getText()),
						 Integer.parseInt(nColsTF.getText()),
						 Integer.parseInt(maxTurnTimeTF.getText())
						 );
			}
		});
		
		jPanel2.add(btnMakeGamegrid);
	}

	public void start(){
		setVisible(true);
	}
}