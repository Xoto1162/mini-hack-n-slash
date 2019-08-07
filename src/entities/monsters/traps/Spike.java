package entities.monsters.traps;

import entities.Trap;

public class Spike extends Trap {

    public Spike(int x, int y) {
        super("Spike", "res/textures/piege/spike.png", x, y, 10000, 1, 0);
    }

}
