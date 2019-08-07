package entities;
import utils.Position;

public class Item extends Entity {


	public Item(String nom, String texture, int x, int y) {
		super(nom, texture,x, y);
	}
	
	/**
	 * Permet de r�cup�rer le nom de l'item
	 * @return nom
	 */
	public String toString(){
		return nom;
	}
	
	/**
	 * permet de dessiner un Item a la position sp�cifi�e
	 * @param drawAt
	 */
	@Override
	public void drawAt(Position drawAt) {
		// TODO Auto-generated method stub
		
	}
}
