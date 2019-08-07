package entities.items.consommables;
import entities.Player;
import entities.items.Consommable;

public abstract class Potion extends Consommable {

	public Potion(String nom, String texture, int x, int y) {
		super(nom, texture, x, y);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Permet d'utiliser la potion sur le joueur
	 * @param j
	 */
	public abstract void use(Player j);
}
