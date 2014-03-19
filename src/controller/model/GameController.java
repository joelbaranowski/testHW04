package controller.model;

import java.awt.Dimension;

import gameIO.*;
import view.*;
import model.*;
import java.util.List;

public class GameController {

	private BoardFrame<IMakePlayer> view;
	private GameModel model;

	public GameController(IBoardModel boardModel, int maxTurnTime) {
		view = new BoardFrame<IMakePlayer>(new IModelAdmin<IMakePlayer>() {
			public void reset() {
				model.reset();
			}

			public void exit() {
				model.exit();
			}

			public List<IMakePlayer> getPlayers() {
				return model.getPlayers();
			}

			public void setPlayers(IMakePlayer player1, IMakePlayer player2) {
				model.setPlayers(player1, player2);
			}

			public IMakePlayer addPlayer(String className) { return model.addPlayer(className);  }

			@Override
			public void setCommand(ICommand command) {
				model.setCommand(command);
			}
		});
		model = new GameModel(boardModel, maxTurnTime, 
				new IViewAdmin() {
			public void draw() {
				view.draw();
			}

			public void win(int player) {
				view.win(player);
			}

			public void reset() {
				view.reset();
			}

			@Override
			public void setDimension(Dimension dimension) {
				view.setDimension(dimension);		
			}
		}, new ITurnAdmin() {
			public void takeTurn(IViewRequestor requestor) {
				view.setRequestor(requestor);
				view.enableBtns(true);
			}
		});    	
	}

	public void start()   {
		
		model.start();
		view.start();

	}
}
