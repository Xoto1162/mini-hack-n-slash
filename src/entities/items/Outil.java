package entities.items;
import entities.Item;

public abstract class Outil extends Item {

	private int bonus;
	
	public Outil(String nom, String texture, int x, int y, int bonus) {
		super(nom, texture, x, y);
		this.bonus = bonus;
	}
	
	public Outil(String nom, String texture, int x, int y, int bonus, boolean estPose){
		super(nom, texture, x, y);
	}
	
	/**
	 * Permet de récupérer le bonus de l'outil
	 * @return bonus
	 */
	public int getBonus(){
		return this.bonus;
	}

}
