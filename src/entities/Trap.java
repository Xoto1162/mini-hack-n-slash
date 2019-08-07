package entities;
import gameworld.GameWorld;
import main.StdDraw;
import utils.Position;

public class Trap extends Monster {

	public Trap(String nom, String texture, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, texture, x, y, pDV, pAtk, pDef, 0);
	}
	
	/**
	 * Un piege ne se d�place pas
	 */
	@Override
	public void deplacer() {
		
	}

	/**
	 * Permet a un pi�ge de faire des d�gats a l'�tre vivant sp�cifi�
	 * @param e
	 */
	@Override
	public void attaquer(LivingBeing e) {
		if(GameWorld.getPlayer().getPosition().distanceTo(this.getPos()) == 0) {
			e.prendreDegats(this.pointsDAttaque);
		}
	}

	/**
	 * Permet de dessiner un pi�ge a la position sp�cifi�e
	 * @param drawAt
	 */
	public void drawAt(Position drawAt) {
		StdDraw.picture(
				(drawAt.getX() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				(drawAt.getY() + 0.5) / GameWorld.getCamera().getTailleGrille(),
				texture,
				1.0 / GameWorld.getCamera().getTailleGrille(),
				1.0 / GameWorld.getCamera().getTailleGrille(),
				90 - orientation * 45
		);
	}

}
