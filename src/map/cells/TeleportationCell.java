package map.cells;

import gameworld.GameWorld;
import map.Cell;
import utils.Position;

public class TeleportationCell extends Cell {

	/**
	 * Identifiant de la map vers laquelle on va �tre t�l�port�
	 */
	protected int mapId;

	/**
	 * Position vers laquelle on va etre t�l�port�
	 */
	protected Position spawn;

	public TeleportationCell(Position pos, String texture, int mapId, Position spawn) {
		super(pos, "TeleportationCell", texture);
		this.mapId = mapId;
		this.spawn = spawn;
	}

	/**
	 * T�l�porte le joueur vers la nouvelle map
	 */
	public void teleport() {
		GameWorld.setActualMap(this.mapId);
		GameWorld.getPlayer().setPosition(this.spawn);
		System.out.println("MAP ID : " + this.mapId);
		System.out.println("SPAWN POS : " + this.spawn.toString());
	}
}
