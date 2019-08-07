package entities;

import java.util.Random;

import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public abstract class LivingBeing extends Entity {

	/**
	 * l'orientation du personnage
	 * 7 0 1
	 * 6   2
	 * 5 4 3
	 */
	protected int orientation = 2;

	/**
	 * Point de vie actuel du joueur
	 */
	protected int pointsDeVie;

	/**
	 * Point de vie maximum du joueur
	 */
	protected int pointsDeVieMax;

	/**
	 * Point d'attaque du joueur
	 */
	protected int pointsDAttaque;

	/**
	 * Défense du joueur
	 */
	protected int pointsDeDefense;

	public LivingBeing(String nom, String texture, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, texture, x, y);
		pointsDeVie = pDV;
		pointsDeVieMax = pDV;
		pointsDAttaque = pAtk;
		pointsDeDefense = pDef;
	}

	/**
	 * Fais mourir l'entité
	 */
	public abstract void mourir();

	/**
	 * Permet d'attaquer le un être vivant spécifique
	 * @param e
	 */
	public void attaquer(LivingBeing e) {
		e.prendreDegats(this.pointsDAttaque);
	}
	
	/**
	 * retourne la position correspondant à la case en face du joueur
	 * @param sens
	 * @return Position
	 */
	public Position getCible(int sens) {
		Position cible = new Position(getX(), getY());
		switch (orientation) {
		case 0:
			cible.setY(getY() + sens);
			break;
		case 1:
			cible.setX(getX() + sens);
			cible.setY(getY() + sens);
			break;
		case 2:
			cible.setX(getX() + sens);
			break;
		case 3:
			cible.setX(getX() + sens);
			cible.setY(getY() - sens);
			break;
		case 4:
			cible.setY(getY() - sens);
			break;
		case 5:
			cible.setX(getX() - sens);
			cible.setY(getY() - sens);
			break;
		case 6:
			cible.setX(getX() - sens);
			break;
		case 7:
			cible.setX(getX() - sens);
			cible.setY(getY() + sens);
			break;
		default:
			break;
		}
		return cible;
	}

	/**
	 * Fais diminuer les points de vie de l'être vivant en fonction de l'attaque de son opposant
	 * @param atk
	 */
	public void prendreDegats(int atk) {
		if (pointsDeVie > 0) {
			Random r = new Random();
			int damage = (int) ((atk - pointsDeDefense) * (1 + ((r.nextFloat() * 2 - 1) / 3)));
			if(damage > 0) {
				this.pointsDeVie -= damage;
				if(this.pointsDeVie <= 0) {
					this.mourir();
				}
			}
		}
	}
	
	/**
	 * Permet de dessiner un être vivant a une position spécifiée
	 * @param drawAt
	 */
	public void drawAt(Position drawAt) {
		int textureID = (this.orientation == 7) ? 0 : (int) this.orientation / 2;
		
		
		// Rendu de la texture de l'etre vivant a la coordonnée précisée par la caméra
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				texture + "[" + textureID + "].png",
				1.0 / GameWorld.getCamera().getTailleGrille(),
				1.0 / GameWorld.getCamera().getTailleGrille()
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
