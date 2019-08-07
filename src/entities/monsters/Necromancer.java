package entities.monsters;

import java.util.ArrayList;
import java.util.Random;

import entities.LivingBeing;
import entities.Monster;
import entities.Player;
import gameworld.GameWorld;
import map.Cell;
import map.cells.FloorCell;
import utils.Position;

public class Necromancer extends Monster{
	
	private ArrayList<NecromancerChild> necromancerSons;
	
	private int spawnCooldown = 0;

	public Necromancer(int x, int y) {
		super("Necromancer", "res/textures/necromancer/necromancer", x, y, 25, 2, 1, 30);
		necromancerSons = new ArrayList<NecromancerChild>();
	}
	
	/**
	 * Permet au Nécromancien d'attaquer l'être vivant spécifié
	 * @param e
	 */
	@Override
	public void attaquer(LivingBeing e) {
		if(this.cooldownAttack <= 0 && GameWorld.getPlayer().getPosition().distanceTo(this.getPos()) < 2) {
			this.cooldownAttack = 15;
			super.attaquer(e);
		} else if (this.spawnCooldown <= 0) {
			this.spawnNecromancerSon();
			this.spawnCooldown = 1000;
		} else {
			this.spawnCooldown--;
			this.cooldownAttack --;
		}
	}
	
	/**
	 * Deplace le necromancien de maniere aléatoire
	 */
	@Override
	public void deplacer() {
		Random r = new Random();
		Player j = GameWorld.getPlayer();
		if (j.getPosition().distanceTo(this.position) > 2) {
			if (this.cooldownMovement == 0) {
				GameWorld.getActualMap().removeEntity(this);
				this.orientation = r.nextInt(8);
				Position cible = this.getCible(1);
				Cell targetCell = GameWorld.getActualMap().getCells()[cible.getX()][cible.getY()];
				if (targetCell.getCellType() == "FloorCell" && ((FloorCell) targetCell).getMonster() == null) {
					changementPosition();
				}
				
				GameWorld.getActualMap().addEntity(this);
				this.cooldownMovement = 40 + r.nextInt(60);
			} else {
				this.cooldownMovement--;
			}
		}
	}
	
	/**
	 * Permet de faire apparaitre un enfant de nécromancien a côté de son père
	 */
	public void spawnNecromancerSon() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				int x = this.getX() - 1 + i;
				int y = this.getY() - 1 + j;
				Cell indexCell = GameWorld.getActualMap().getCells()[x][y];
				if(indexCell.getCellType() == "FloorCell" && ((FloorCell) indexCell).getMonster() == null) {
					NecromancerChild c = new NecromancerChild(x, y, this);
					this.necromancerSons.add(c);
					GameWorld.getActualMap().addEntity(c);
					return;
				}
			}
		}
	}
	
	/**
	 * Fais mourir le Nécromancien ainsi que tout ses enfants
	 */
	@Override
	public void mourir() {
		isAlive = false;
		dropContent();
		GameWorld.getPlayer().gainExperience(this.experienceGain);
		for(NecromancerChild nc : necromancerSons) {
			nc.mourir();
		}
	}
}
