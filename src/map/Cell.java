package map;

import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public abstract class Cell {

	/**
	 * Cellule de vide
	 */
	static final int VOID = 0;
	
	/**
	 * Cellule de sol
	 */
	static final int FLOOR = 1;
	
	/**
	 * Cellule de mur
	 */
	static final int WALL = 2;
	
	/**
	 * Cellule de téléportation
	 */
	static final int TELEPORTATION = 3;
	
	/**
	 * Cellule avec un démon
	 */
	static final int DEMON = 4;
	
	/**
	 * Cellule avec un nécromancien
	 */
	static final int NECROMANCER = 5;
	
	/**
	 * Cellule avec un zombie
	 */
	static final int ZOMBIE = 6;
	
	/**
	 * Cellule avec un piège a pics
	 */
	static final int SPIKE = 7;
	
	/**
	 * Cellule avec un piège a ours
	 */
	static final int BEARTRAP = 8;
	
	/**
	 * Cellule avec la porte des enfers
	 */
	static final int HELLDOOR = 666;

	/**
	 * La position de la cellule
	 */
	protected Position pos;
	
	/**
	 * La texture de la cellule
	 */
	protected String texture;
	
	/**
	 * Le type de la cellule
	 */
	protected String cellType;

	public Cell(Position pos, String cellType, String texture) {
		this.pos = pos;
		this.cellType = cellType;
		this.texture = texture;
	}

	/**
	 * Permet de récupérer la position de la cellule
	 * @return pos
	 */
	public Position getCellPos() {
		return this.pos;
	}
	
	/**
	 * Permet de récupérer le type de la cellule
	 * @return cellType
	 */
	public String getCellType() {
		return this.cellType;
	}
	
	/**
	 * Permet de dessiner la cellule a une position spécifiée
	 * @param drawAt
	 */
	public void drawAt(Position drawAt) {
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				texture,
				1.0 / GameWorld.getCamera().getTailleGrille(),
				1.0 / GameWorld.getCamera().getTailleGrille()
		);
	}
}
