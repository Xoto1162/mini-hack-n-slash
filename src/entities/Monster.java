package entities;

import java.util.ArrayList;
import java.util.Random;

import entities.items.Cle;
import entities.items.consommables.Bombe;
import entities.items.consommables.Missile;
import entities.items.consommables.potions.HealPotion;
import entities.items.equipement.IronArmour;
import entities.items.equipement.IronSword;
import entities.items.equipement.LeatherArmour;
import entities.items.equipement.WoodenSword;
import gameworld.GameWorld;
import map.Cell;
import map.Map;
import map.cells.FloorCell;
import utils.Position;

public abstract class Monster extends LivingBeing {

	/**
	 * La liste des loots du monstre
	 */
	protected ArrayList<Item> treasure;
	
	/**
	 * Le nombre de points d'expérience gagnés lorsque le monstre est tué
	 */
	protected int experienceGain;

	/**
	 * Permet de savoir si le monstre est mort ou non
	 */
	protected boolean isAlive;

	/**
	 * attente avant nouveau déplacement du monstre
	 */
	protected int cooldownMovement = 5;
	
	/**
	 * attente avant nouvelle attaque du monstre
	 */
	protected int cooldownAttack = 15;

	// constructeur
	public Monster(String nom, String texture, int x, int y, int pDV, int pAtk, int pDef, int experienceGain) {
		super(nom, texture, x, y, pDV, pAtk, pDef);
		this.treasure = new ArrayList<Item>();
		this.isAlive = true;
		this.experienceGain = experienceGain;
		generateTreasure();
	}

	/**
	 * Deplace le monstre de maniere aléatoire
	 * Si le joueur est a moin de 4 case il y a un système d'agression sans IA
	 */
	public void deplacer() {
		Random r = new Random();
		Player j = GameWorld.getPlayer();
		if (j.getPosition().distanceTo(this.position) > 4) {
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
		} else {
			if (this.cooldownMovement == 0) {
				Map map = GameWorld.getActualMap();
				Position joueurPos = GameWorld.getPlayer().getPosition();
				Position monsterPos = this.position;
				if (monsterPos.getX() - joueurPos.getX() > 0 && map.canMoveAt(new Position(monsterPos.getX()-1, monsterPos.getY()))) {
					this.moveTo(new Position(monsterPos.getX() - 1, monsterPos.getY()));
				} else if (monsterPos.getX() - joueurPos.getX() < 0 && map.canMoveAt(new Position(monsterPos.getX()+1, monsterPos.getY()))) {
					this.moveTo(new Position(monsterPos.getX() + 1, monsterPos.getY()));
				} else if (monsterPos.getY() - joueurPos.getY() > 0 && map.canMoveAt(new Position(monsterPos.getX(), monsterPos.getY() - 1))) {
					this.moveTo(new Position(monsterPos.getX(), monsterPos.getY() - 1));
				} else if (monsterPos.getY() - joueurPos.getY() < 0 && map.canMoveAt(new Position(monsterPos.getX(), monsterPos.getY() + 1))) {
					this.moveTo(new Position(this.getX(), this.getY() + 1));
				}
				this.cooldownMovement = 30;
			} else {
				this.cooldownMovement--;
			}
		}
	}
	
	/**
	 * Deplace le monstre a la position souhaité
	 * @param pos
	 */
	protected void moveTo(Position pos) {
		GameWorld.getActualMap().removeEntity(this);
		this.position = pos;
		GameWorld.getActualMap().addEntity(this);
	}
	
	/**
	 * Permet au monstre d'attaque un être vivant
	 * @param e
	 */
	@Override
	public void attaquer(LivingBeing e) {
		if(this.cooldownAttack == 0 && e.getPos().distanceTo(this.getPos()) < 2) {
			this.cooldownAttack = 15;
			super.attaquer(e);
		} else if (GameWorld.getPlayer().getPosition().distanceTo(this.getPos()) < 2) {
			this.cooldownAttack --;
		}
	}

	/**
	 * Fait avancer le monstre en fonction de sa position
	 */
	protected void changementPosition() {
		switch (this.orientation) {
		case 0:
			this.position.setY(this.getY() + 1);
			break;
		case 1:
			this.position.setX(this.getX() + 1);
			this.position.setY(this.getY() + 1);
			break;
		case 2:
			this.position.setX(this.getX() + 1);
			break;
		case 3:
			this.position.setX(this.getX() + 1);
			this.position.setY(this.getY() - 1);
			break;
		case 4:
			this.position.setY(this.getY() - 1);
			break;
		case 5:
			this.position.setX(this.getX() - 1);
			this.position.setY(this.getY() - 1);
			break;
		case 6:
			this.position.setX(this.getX() - 1);
			break;
		case 7:
			this.position.setX(this.getX() - 1);
			this.position.setY(this.getY() + 1);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Génère les loots du monstre
	 */
	public void generateTreasure() {
		Random r = new Random();
		float drop = r.nextFloat();
		if(drop > 0.999f) {
			if(r.nextFloat() > 0.5)
				treasure.add(new IronSword());
			else
				treasure.add(new IronArmour());
		}
		else if(drop > 0.98f) {
			if(r.nextFloat() > 0.5)
				treasure.add(new WoodenSword());
			else
				treasure.add(new LeatherArmour());
		}
		else if(drop > 0.4f) {
			float f = r.nextFloat();
			if(f > (float) 2/3)
				treasure.add(new HealPotion(10));
			else if (f > (float) 1/3)
				treasure.add(new Bombe());
			else
				treasure.add(new Missile());
		}
		while(drop > 0.4f) {
			drop = r.nextFloat();
			if(drop > 0.999f) {
				if(r.nextFloat() > 0.5)
					treasure.add(new IronSword());
				else
					treasure.add(new IronArmour());
			}
			else if(drop > 0.98f) {
				if(r.nextFloat() > 0.5)
					treasure.add(new WoodenSword());
				else
					treasure.add(new LeatherArmour());
			}
			else if(drop > 0.4f) {

				float f = r.nextFloat();
			if(f > (float) 2/3)
				treasure.add(new HealPotion(10));
			else if (f > (float) 1/3)
				treasure.add(new Bombe());
			else
				treasure.add(new Missile());
			}
		}
	}
	
	/**
	 * Fais mourrir le monstre
	 */
	@Override
	public void mourir() {
		isAlive = false;
		dropContent();
		GameWorld.getPlayer().gainExperience(this.experienceGain);
	}

	/**
	 * Fais tomber les loots du monstre
	 */
	public void dropContent() {
		for(Item i : treasure) {
			((FloorCell) GameWorld.getActualMap().getCells()[this.getX()][this.getY()]).addItem(i);
		}
	}
	
	/**
	 * Permet d'ajouter une clé aux loots du monstre
	 */
	public void addKey() {
		treasure.add(new Cle());
	}

	/**
	 * Permet de savoir si le monstre est vivant ou non
	 * @return isAlive
	 */
	public boolean isAlive() {
		return this.isAlive;
	}

	
	/**
	 * Permet de récupérer les points de vie du monstre
	 * @return pointDeVie
	 */
	public int getPointsDeVie() {
		return this.pointsDeVie;
	}
}
