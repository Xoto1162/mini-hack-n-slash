package entities.monsters.traps;

import entities.LivingBeing;
import entities.Trap;
import gameworld.GameWorld;

public class BearTrap extends Trap {

    public BearTrap(int x, int y) {
    	super("Bear Trap", "res/textures/piege/beartrap.png", x, y, 10000, 10, 0);
	}

    /**
     * Permet au pi�ge a ours de faire des d�gats a l'�tre vivant sp�cifi�
     */
	@Override
    public void attaquer(LivingBeing e) {
        if(GameWorld.getPlayer().getPosition().distanceTo(this.getPos()) == 0) {
            e.prendreDegats(this.pointsDAttaque);
            GameWorld.getActualMap().removeEntity(this);
        }
    }

}
