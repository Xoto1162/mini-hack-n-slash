package camera;

import gameworld.GameWorld;
import map.Cell;
import utils.Position;
public class Camera {

	/**
	 * Taille de la caméra
	 */
	private int camSize;
	
	public Camera(int camSize) {
		this.camSize = camSize;
	}

    /**
     * Donne la position de la cellule du jeu sur laquelle est la souris
     * @param mousePos
     * @return gamePos
     */
	public Position mousePosToGamePos(Position mousePos) {
	    Position gamePos = null;
	    int x = GameWorld.getPlayer().getX() - camSize + mousePos.getX();
	    int y = GameWorld.getPlayer().getY() - camSize + mousePos.getY();
	    int sizeMapX = GameWorld.getActualMap().getCells().length;
	    int sizeMapY = (x >= 0 && x < sizeMapX) ? GameWorld.getActualMap().getCells()[x].length : 0;
	    if (x >= 0 && y >= 0 && x < sizeMapX && y < sizeMapY)
            gamePos = new Position(x, y);
	    return gamePos;
    }
	
	/**
	 * Fais le rendu de la caméra
	 */
	public void renderCamera() {
		Cell[][] cells = GameWorld.getActualMap().getCells();
		
		for(int i = this.camSize; i > - this.camSize - 1; i--) {
			for(int j = this.camSize; j > - this.camSize - 1; j--) {
				Position index = new Position(GameWorld.getPlayer().getX() + i, GameWorld.getPlayer().getY() + j);
				int x = i + camSize;
				int y = j + camSize;
				Position drawPosition = new Position(x, y);
				if(GameWorld.getActualMap().isCellAtPos(index)) {
					cells[index.getX()][index.getY()].drawAt(drawPosition);
				}
				if(i == 0 && j == 0)
					GameWorld.getPlayer().drawAt(new Position(camSize, camSize));
			}
		}
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Redimensionne la caméra en nombre de cellules autour du joueur
	 * @param size
	 */
	public void setCamSize(int size) {
		camSize = size;
	}
	
	/**
	 * Permet de récupérer la taille de la caméra en nombre de cellules autour du joueur
	 * @return camSize;
	 */
	public int getCamSize() {
		return camSize;
	}

	/**
	 * Permet de récupérer la taille de la grille d'affichage
	 * @return (camSize * 2 + 1)
	 */
	public int getTailleGrille() { return getCamSize() * 2 + 1; }
}

