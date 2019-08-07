package entities.monsters;

import entities.Monster;
import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public class NecromancerChild extends Monster{

	public NecromancerChild(int x, int y, Necromancer father) {
		super("Necromancer Child", "res/textures/necromancer/necromancer", x, y, 12, 4, 1, 10);
	}
	
	/**
	 * Permet de faire le rendu de l'enfant d'un nécromancien
	 */
	@Override
	public void drawAt(Position drawAt) {
		int textureID = (this.orientation == 7) ? 0 : (int) this.orientation / 2;
		
		// Rendu de la texture de l'etre vivant a la coordonnée précisée par la caméra
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				texture + "[" + textureID + "].png",
				0.7 / GameWorld.getCamera().getTailleGrille(),
				0.7 / GameWorld.getCamera().getTailleGrille()
		);
		
		// Rendu de la barre de vie de l'etre vivant, au dessus de lui
		if(this.pointsDeVie > 0) {
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledRectangle(
					(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
					(drawAt.getY() + 1.2) / GameWorld.getCamera().getTailleGrille(),
					(0.4d * ((float) this.pointsDeVie / this.pointsDeVieMax)) / GameWorld.getCamera().getTailleGrille(),
					0.05d / GameWorld.getCamera().getTailleGrille());
			StdDraw.setPenColor(StdDraw.BLACK);
		}
	}
}
