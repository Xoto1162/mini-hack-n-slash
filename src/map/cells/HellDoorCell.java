package map.cells;

import entities.HellDoor;
import map.Cell;
import utils.Position;

public class HellDoorCell extends Cell{
	
	protected HellDoor hellDoor;
	
	public HellDoorCell(Position pos, String texture, HellDoor hellDoor) {
		super(pos, "HellDoorCell", texture);
		this.hellDoor = hellDoor;
	}
	
	@Override
	public void drawAt(Position drawAt) {
		super.drawAt(drawAt);
		this.hellDoor.drawAt(drawAt);
	}
	
	public HellDoor getHellDoor() {
		return hellDoor;
	}
	
}
