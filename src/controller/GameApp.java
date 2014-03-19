package controller;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import controller.model.InitModel;
import controller.view.IInitModelAdapter;
import controller.view.InitFrame;

public class GameApp extends JApplet {

	private static final long serialVersionUID = 4305501388520537777L;
	
	private InitFrame view;
	
	private InitModel model;
	
	public GameApp() {
		this(WindowConstants.HIDE_ON_CLOSE);
	}
	
	public GameApp(int closeOp) {
		view = new InitFrame(closeOp, new IInitModelAdapter() {

			@Override
			public void makeTicTacToe(int nRows, int nCols, int inARow,
					int maxTurnTime) {
				model.makeTicTacToe(nRows, nCols, inARow, maxTurnTime);
				
			}

			@Override
			public void makeOthello(int nRows, int nCols, int maxTurnTime) {
				model.makeOthello(nRows, nCols, maxTurnTime);
			}

			@Override
			public void makeGameGrid(int nRows, int nCols, int maxTurnTime) {
				model.makeGameGrid(nRows, nCols, maxTurnTime);
			}
			
		});
		
		model = new InitModel();
	}
	
	public void start() {
		view.start();
	}
	
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				(new GameApp(WindowConstants.EXIT_ON_CLOSE)).start();	
			}
    		
    	});
          
    }
}
