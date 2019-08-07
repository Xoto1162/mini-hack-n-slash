package entities;
import utils.Position;

public abstract class Entity {
	/**
	 * le nom de l'entit�
	 */
	protected String nom;
	
	/**
	 * la position de l'entit�
	 */
	protected Position position;
	
	/**
	 * la texture de l'entit�
	 */
	protected String texture;
	
	public Entity(String nom, String texture ,int x, int y) {
		this.nom = nom;
		this.texture = texture;
		position = new Position(x, y);
	}
	
	/**
	 * Permet de r�cup�rer le nom de l'entit�
	 * @return nom
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Permet de r�cup�rer la postion X de l'entit�
	 * @return position.getX()
	 */
	public int getX() {
		return position.getX();
	}
	
	/**
	 * Permet de r�cup�rer la postion Y de l'entit�
	 * @return position.getY()
	 */
	public int getY() {
		return this.position.getY();
	}
	
	/**
	 * Retourne le nom de l'entit�
	 */
	public String toString() {
		return nom;
	}
	
	/**
	 * Permet de replacer l'entit� a une nouvelle position
	 * @param p
	 */
	public void setPosition(Position p){
		this.position = p;
	}
	
	/**
	 * Permet de r�cup�rer la position de l'entit�
	 * @return this.position
	 */
	public Position getPos() {
		return this.position;
	}

	/**
	 * Permet d'afficher une entit� a une position d�finie sur l'�cran
	 * @param position
	 */
	public abstract void drawAt(Position position);

}
