package entities.items;

import java.util.ArrayList;

import entities.Entity;
import entities.Item;
import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public class Chest extends Entity{
	
	/**
	 * Liste contenant les Items pr�sent dans le coffre
	 */
	private ArrayList<Item> items;

	public Chest(int x, int  y, ArrayList<Item> items) {
		super("Chest", "res/textures/chest.jpg", x, y);
		this.items = items;
	}

	/**
	 * Permet de dessiner un coffre a la position sp�cifi�e
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
	}
	
	/**
	 * Permet au joueur de r�cup�rer l'int�gralit� des objets du coffre
	 */
	public void loot() {
		for(Item i : items) {
			GameWorld.getPlayer().ramasser(i);
			System.out.println("Vous avez ramass� 1 " + i.getNom());
		}
	}

}
