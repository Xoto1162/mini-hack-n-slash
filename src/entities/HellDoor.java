package entities;

import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public class HellDoor extends Entity {

	/**
	 * Le nombre de clés déposées sur la porte de l'enfer
	 */
	private int keys = 0;

	public HellDoor(int x, int y) {
		super("HellDoor", "res/textures/hellDoor.png", x, y);
	}

	/**
	 * Permet de placer une clé sur la porte de l'enfer
	 */
	public void placer() {
		keys++;
		if(keys == 4) {
			GameWorld.hellDoorHasBeenOpened();
		}
	}

	/**
	 * Dessine la porte de l'enfer a la position spécifiée
	 * @param drawAt
	 */
	@Override
	public void drawAt(Position drawAt) {
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				texture,
				1.0 / GameWorld.getCamera().getTailleGrille(),
				1.0 / GameWorld.getCamera().getTailleGrille()
		);
		Fire.drawAt(drawAt);
	}

}
