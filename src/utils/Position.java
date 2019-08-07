package utils;

public class Position {
	/**
	 * Position en x
	 */
	private int positionX;

	/**
	 * Position en y
	 */
	private int positionY;
	
	public Position(int x, int y){
		positionX=x;
		positionY=y;
	}

	/**
	 * Retourne la position en X
	 * @return positionX
	 */
	public int getX(){
		return positionX;
	}

	/**
	 * Retourne la position en y
	 * @return positionY
	 */
	public int getY(){
		return positionY;
	}

	/**
	 * Indique si deux position sont les même
	 * @param p
	 * @return true si meme position sinon false
	 */
	public boolean equals(Position p){
		return (this.positionX==p.positionX && this.positionY==p.positionY);
	}

	/**
	 * Calcule la distance avec la position pos
	 * @param pos
	 * @return la distance entre la position et pos
	 */
	public double distanceTo(Position pos) {
		return Math.sqrt(Math.pow(this.getX() - pos.getX(), 2) + Math.pow(this.getY() - pos.getY(), 2));
	}

	/**
	 * Permet de modifier l'attribut positionY
	 * @param y
	 */
	public void setY(int y) {
		// TODO Auto-generated method stub
		this.positionY = y;
	}

	/**
	 * Permet de modifier l'attribut positionX
	 * @param x
	 */
	public void setX(int x) {
		// TODO Auto-generated method stub
		this.positionX = x;
	}

	/**
	 * retourne la position sous forme de chaine de caractère
	 * @return
	 */
	public String toString(){
		String s = "";
		s += "(" + getX() + "," + getY() + ")";
		return s;
	}
}
