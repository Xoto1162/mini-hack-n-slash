package entities;
import utils.Position;

public abstract class Entity {
	/**
	 * le nom de l'entité
	 */
	protected String nom;
	
	/**
	 * la position de l'entité
	 */
	protected Position position;
	
	/**
	 * la texture de l'entité
	 */
	protected String texture;
	
	public Entity(String nom, String texture ,int x, int y) {
		this.nom = nom;
		this.texture = texture;
		position = new Position(x, y);
	}
	
	/**
	 * Permet de récupérer le nom de l'entité
	 * @return nom
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Permet de récupérer la postion X de l'entité
	 * @return position.getX()
	 */
	public int getX() {
		return position.getX();
	}
	
	/**
	 * Permet de récupérer la postion Y de l'entité
	 * @return position.getY()
	 */
	public int getY() {
		return this.position.getY();
	}
	
	/**
	 * Retourne le nom de l'entité
	 */
	public String toString() {
		return nom;
	}
	
	/**
	 * Permet de replacer l'entité a une nouvelle position
	 * @param p
	 */
	public void setPosition(Position p){
		this.position = p;
	}
	
	/**
	 * Permet de récupérer la position de l'entité
	 * @return this.position
	 */
	public Position getPos() {
		return this.position;
	}

	/**
	 * Permet d'afficher une entité a une position définie sur l'écran
	 * @param position
	 */
	public abstract void drawAt(Position position);

}
