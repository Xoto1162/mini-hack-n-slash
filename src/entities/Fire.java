package entities;

import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public class Fire{

	public Fire() {}

	/**
	 * Permet d'afficher une texture de feu a la position spécifiée
	 * @param drawAt
	 */
	public static void drawAt(Position drawAt) {
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				"res/textures/fire.png",
				1.0 / GameWorld.getCamera().getTailleGrille(),
				1.0 / GameWorld.getCamera().getTailleGrille()
		);
	}

}
