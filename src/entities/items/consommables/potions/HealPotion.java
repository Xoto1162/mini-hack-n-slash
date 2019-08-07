package entities.items.consommables.potions;

import entities.Player;
import entities.items.consommables.Potion;

public class HealPotion extends Potion{

	/**
	 * Le nombre de points de vie rendus par la potion de soin
	 */
	private int healAmount;
	
	public HealPotion(int x, int y, int healAmount) {
		super("Potion de soin", "notexture", x, y);
		this.healAmount = healAmount;
	}
	
	public HealPotion(int healAmount) {
		super("Potion de soin", "notexture", 0, 0);
		this.healAmount = healAmount;
	}

	/**
	 * Soigne le joueur j de healAmount pv
	 * @param j
	 */
	public void heal(Player j) {
		j.heal(this.healAmount);
	}

	/**
	 * Utilise la potion sur le joueur j
	 * @param j
	 */
	@Override
	public void use(Player j) {
		this.heal(j);
	}
}
